import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class Penulis {
    private String nama;

    public Penulis(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }
}


class Kategori {
    private String namaKategori;
    private List<Buku> daftarBuku;

    public Kategori(String namaKategori) {
        this.namaKategori = namaKategori;
        this.daftarBuku = new ArrayList<>();
    }

    public void tambahBuku(Buku buku) {
        this.daftarBuku.add(buku);
    }

    public void displayInfo() {
        System.out.println("  Kategori: " + this.namaKategori);
        System.out.println("  Daftar Buku (" + this.daftarBuku.size() + " buku):");
        for (Buku buku : daftarBuku) {
            buku.displayInfo();
        }
    }

    public String getNamaKategori() {
        return namaKategori;
    }
}

class Buku {
    private String judul;
    private String sinopsis;
    private Kategori kategori;
    private List<Penulis> penulisList;
    private int terjualBulanIni;


    public Buku(String judul, String sinopsis, Kategori kategori, List<Penulis> penulisList) {
        this.judul = judul;
        this.sinopsis = sinopsis;
        this.kategori = kategori;
        this.penulisList = new ArrayList<>(penulisList);
        this.terjualBulanIni = 0;
    }

    public int hitungKataSinopsis() {
        if (this.sinopsis == null || this.sinopsis.trim().isEmpty()) {
            return 0;
        }
        return this.sinopsis.trim().split("\\s+").length;
    }

    public double cekTingkatKesamaan(Buku bukuLain) {
        if (bukuLain == null) return 0.0;

        int totalAttribute = 4;
        double atributSama = 0;

        if (this.judul != null && this.judul.equalsIgnoreCase(bukuLain.judul)) atributSama++;
        if (this.sinopsis != null && this.sinopsis.equalsIgnoreCase(bukuLain.sinopsis)) atributSama++;
        if (this.kategori != null && bukuLain.kategori != null &&
            this.kategori.getNamaKategori().equalsIgnoreCase(bukuLain.kategori.getNamaKategori())) atributSama++;

        if (this.penulisList.size() == bukuLain.penulisList.size()) {
            boolean penulisSama = true;
            for (int i = 0; i < this.penulisList.size(); i++) {
                if (!this.penulisList.get(i).getNama().equalsIgnoreCase(bukuLain.penulisList.get(i).getNama())) {
                    penulisSama = false;
                    break;
                }
            }
            if (penulisSama) atributSama++;
        }

        return (atributSama / totalAttribute) * 100.0;
    }

    public Buku copy() {
        return new Buku(this.judul, this.sinopsis, this.kategori, new ArrayList<>(this.penulisList));
    }

    public void displayInfo() {
        System.out.println("    - Judul    : " + this.judul);
        System.out.println("      Sinopsis : " + this.sinopsis + " (" + hitungKataSinopsis() + " kata)");

        List<String> namaPenulis = new ArrayList<>();
        for (Penulis p : penulisList) {
            namaPenulis.add(p.getNama());
        }
        System.out.println("      Penulis  : " + String.join(", ", namaPenulis) + "\n");
    }
      public void setTerjualBulanIni(int terjualBulanIni) {
        this.terjualBulanIni = terjualBulanIni;
    }

    public String getJudul() { return judul; }
    public String getSinopsis() { return sinopsis; }
    public Kategori getKategori() { return kategori; }
    public List<Penulis> getPenulisList() { return penulisList; }
    public void bacaFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String baris = br.readLine();
            if (baris != null){
                String[] data = baris.split(";");
                if (data.length >= 2) {
                    this.judul = data[0].trim();
                    this.penulisList.clear(); 
                    this.penulisList.add(new Penulis(data[1].trim()));
                }
            }
        } 
        catch (IOException e) {
            System.out.println("Error membaca file: " + e.getMessage());
        }
    }
    public void simpanFile(String namaFile) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(namaFile));
            
            String namaPenulis = this.penulisList.isEmpty() ? "Anonim" : this.penulisList.get(0).getNama();
            
            writer.write(this.judul + "; " + namaPenulis);
            writer.close();
            System.out.println("-> Sukses menyimpan data buku ke file: " + namaFile);
        } catch (Exception e) {
            System.out.println("-> Gagal menyimpan file: " + e.getMessage());
        }
    }

    public double hitungRoyalti(double hargaBuku) {
        double totalPenjualan = hargaBuku * this.terjualBulanIni;
        return totalPenjualan * 0.10;
    }

    public double hitungRoyalti(double hargaBuku, double persen) {
        double totalPenjualan = hargaBuku * this.terjualBulanIni;
        return totalPenjualan * (persen / 100.0);
    }
}


class Perpustakaan {
    private String nama;
    private List<Kategori> daftarKategori;

    public Perpustakaan(String nama) {
        this.nama = nama;
        this.daftarKategori = new ArrayList<>();
    }

    public void tambahKategori(Kategori kategori) {
        this.daftarKategori.add(kategori);
    }

    public void displayAllData() {
        System.out.println("========================================");
        System.out.println("Sistem Data " + this.nama);
        System.out.println("========================================");
        System.out.println("Total Kategori: " + this.daftarKategori.size());
        System.out.println();

        for (Kategori kategori : daftarKategori) {
            kategori.displayInfo();
            System.out.println();
        }
    }
}


public class MainPerpus {
    
    private static Buku registrasiBuku(String judul, String sinopsis, Kategori kategori, Penulis... penulis) {
        Buku bukuBaru = new Buku(judul, sinopsis, kategori, Arrays.asList(penulis));
        kategori.tambahBuku(bukuBaru);
        return bukuBaru;
    }

    public static void main(String[] args) {
        Perpustakaan perpusX = new Perpustakaan("Perpustakaan Kampus Brawijaya");

        Kategori tekno = new Kategori("Teknologi");
        Kategori fiksi = new Kategori("Fiksi");
        Kategori sejarah = new Kategori("Sejarah");
        Kategori filsafat = new Kategori("Filsafat");
        Kategori politik = new Kategori("Politik");
        Kategori agama = new Kategori("Agama");
        Kategori psikologi = new Kategori("Psikologi");

        Penulis penulis1 = new Penulis("Erich Gamma");
        Penulis penulis2 = new Penulis("Richard Helm");
        Penulis penulis3 = new Penulis("Ralph Johnson");
        Penulis penulis4 = new Penulis("John Vlissides");
        Penulis penulis5 = new Penulis("Robert C. Martin");
        Penulis penulis6 = new Penulis("Dee Lestari");
        Penulis penulis9 = new Penulis("George Orwell");

        
        String sinopsisTekno = "Buku ini sangat direkomendasikan untuk programmer yang ingin meningkatkan kualitas dan efisiensi dalam penulisan kode perangkat lunak sehari-hari agar lebih bersih dan terstruktur.";
        String sinopsisFiksi = "Sebuah mahakarya fiksi yang membawa pembacanya ke dalam dunia imajinasi tanpa batas dengan konflik yang mendalam, karakter yang kuat, dan alur tak terduga.";
        String sinopsisSejarah = "Buku ini menyajikan analisis mendalam tentang peristiwa-peristiwa penting dalam sejarah dunia, memberikan wawasan baru dan perspektif yang segar.";
        String sinopsisFilsafat = "Buku ini mengajak pembaca untuk merenungkan pertanyaan-pertanyaan mendalam tentang eksistensi, pengetahuan, dan moralitas melalui lensa filsafat klasik dan modern.";
        String sinopsisPolitik = "Buku ini memberikan analisis tajam tentang dinamika politik global, mengeksplorasi isu-isu kontemporer dan memberikan wawasan tentang masa depan politik dunia.";
        String sinopsisAgama = "Buku ini menyajikan pemahaman mendalam tentang berbagai tradisi keagamaan di dunia, mengeksplorasi ajaran, praktik, dan dampaknya terhadap masyarakat.";
        String sinopsisPsikologi = "Buku ini mengupas tuntas berbagai teori dan konsep psikologi, memberikan wawasan tentang perilaku manusia, emosi, dan proses mental yang kompleks.";

        
        Buku bukuTekno1 = registrasiBuku("Design Patterns", sinopsisTekno, tekno, penulis1, penulis2, penulis3, penulis4);
        Buku bukuTekno2 = registrasiBuku("Clean Code", sinopsisTekno, tekno, penulis5);
        Buku bukuTekno3 = registrasiBuku("Refactoring", sinopsisTekno, tekno, penulis5);
        Buku bukuTekno4 = registrasiBuku("The Pragmatic Programmer", sinopsisTekno, tekno, penulis5);
        Buku bukuTekno5 = registrasiBuku("Code Complete", sinopsisTekno, tekno, penulis5);
        
        Buku bukuFiksi1 = registrasiBuku("Supernova: Ksatria, Puteri, dan Bintang Jatuh", sinopsisFiksi, fiksi, penulis6);
        Buku bukuFiksi2 = registrasiBuku("1984", sinopsisFiksi, fiksi, penulis9);
        Buku bukuFiksi3 = registrasiBuku("Animal Farm", sinopsisFiksi, fiksi, penulis9);
        Buku bukuFiksi4 = registrasiBuku("Brave New World", sinopsisFiksi, fiksi, penulis9);
        Buku bukuFiksi5 = registrasiBuku("Fahrenheit 451", sinopsisFiksi, fiksi, penulis9);

        Buku bukuSejarah1 = registrasiBuku("Sapiens: A Brief History of Humankind", sinopsisSejarah, sejarah, new Penulis("Yuval Noah Harari"));
        Buku bukuSejarah2 = registrasiBuku("Guns, Germs, and Steel", sinopsisSejarah, sejarah, new Penulis("Jared Diamond"));
        Buku bukuSejarah3 = registrasiBuku("The Silk Roads", sinopsisSejarah, sejarah, new Penulis("Peter Frankopan"));
        Buku bukuSejarah4 = registrasiBuku("A People's History of the United States", sinopsisSejarah, sejarah, new Penulis("Howard Zinn"));
        Buku bukuSejarah5 = registrasiBuku("The History of the Ancient World", sinopsisSejarah, sejarah, new Penulis("Susan Wise Bauer"));

        Buku bukuFilsafat1 = registrasiBuku("Meditations", sinopsisFilsafat, filsafat, new Penulis("Marcus Aurelius"));
        Buku bukuFilsafat2 = registrasiBuku("The Republic", sinopsisFilsafat, filsafat, new Penulis("Plato"));
        Buku bukuFilsafat3 = registrasiBuku("Beyond Good and Evil", sinopsisFilsafat, filsafat, new Penulis("Friedrich Nietzsche"));
        Buku bukuFilsafat4 = registrasiBuku("Critique of Pure Reason", sinopsisFilsafat, filsafat, new Penulis("Immanuel Kant"));
        Buku bukuFilsafat5 = registrasiBuku("Being and Time", sinopsisFilsafat, filsafat, new Penulis("Martin Heidegger"));

        Buku bukuPolitik1 = registrasiBuku("The Prince", sinopsisPolitik, politik, new Penulis("Niccolò Machiavelli"));
        Buku bukuPolitik2 = registrasiBuku("The Art of War", sinopsisPolitik, politik, new Penulis("Sun Tzu"));
        Buku bukuPolitik3 = registrasiBuku("On Liberty", sinopsisPolitik, politik, new Penulis("John Stuart Mill"));
        Buku bukuPolitik4 = registrasiBuku("The Communist Manifesto", sinopsisPolitik, politik, new Penulis("Karl Marx"));
        Buku bukuPolitik5 = registrasiBuku("The Road to Serfdom", sinopsisPolitik, politik, new Penulis("Friedrich Hayek"));

        Buku bukuAgama1 = registrasiBuku("The Bible", sinopsisAgama, agama, new Penulis("Various Authors"));
        Buku bukuAgama2 = registrasiBuku("The Quran", sinopsisAgama, agama, new Penulis("Various Authors"));
        Buku bukuAgama3 = registrasiBuku("The Bhagavad Gita", sinopsisAgama, agama, new Penulis("Vyasa"));
        Buku bukuAgama4 = registrasiBuku("The Tao Te Ching", sinopsisAgama, agama, new Penulis("Laozi"));
        Buku bukuAgama5 = registrasiBuku("The Dhammapada", sinopsisAgama, agama, new Penulis("Buddha"));

        Buku bukuPsikologi1 = registrasiBuku("Thinking, Fast and Slow", sinopsisPsikologi, psikologi, new Penulis("Daniel Kahneman"));
        Buku bukuPsikologi2 = registrasiBuku("The Social Animal", sinopsisPsikologi, psikologi, new Penulis("David Brooks"));
        Buku bukuPsikologi3 = registrasiBuku("Man's Search for Meaning", sinopsisPsikologi, psikologi, new Penulis("Viktor E. Frankl"));
        Buku bukuPsikologi4 = registrasiBuku("Influence: The Psychology of Persuasion", sinopsisPsikologi, psikologi, new Penulis("Robert B. Cialdini"));
        Buku bukuPsikologi5 = registrasiBuku("The Power of Habit", sinopsisPsikologi, psikologi, new Penulis("Charles Duhigg"));


        perpusX.tambahKategori(tekno);
        perpusX.tambahKategori(fiksi);
        perpusX.tambahKategori(sejarah);
        perpusX.tambahKategori(filsafat);
        perpusX.tambahKategori(politik);
        perpusX.tambahKategori(agama);
        perpusX.tambahKategori(psikologi);

        
        System.out.println("=== PENGUJIAN FITUR BARU OOP ===");
        
        Buku bukuTekno1_Copy = bukuTekno1.copy();
        System.out.println("Berhasil menyalin buku : " + bukuTekno1_Copy.getJudul());
        
        double kesamaanCopy = bukuTekno1.cekTingkatKesamaan(bukuTekno1_Copy);
        System.out.println("Tingkat kesamaan (Buku Asli vs Copy)  : " + kesamaanCopy + "%");

        double kesamaanBeda = bukuTekno1.cekTingkatKesamaan(bukuTekno2);
        System.out.println("Tingkat kesamaan (Buku 1 vs Buku 2)   : " + kesamaanBeda + "%\n");
        perpusX.displayAllData();
        System.out.println("\n=== PENGUJIAN FITUR I/O DAN ROYALTI ===");
        
       
        bukuFiksi1.setTerjualBulanIni(100); 
        double hargaBukuFiksi = 85000;      
        
      
        System.out.println("Penjualan " + bukuFiksi1.getJudul() + " : " + 100 + " buku");
        System.out.println("Royalti Standar (10%) : Rp " + bukuFiksi1.hitungRoyalti(hargaBukuFiksi));
        System.out.println("Royalti Spesial (15%) : Rp " + bukuFiksi1.hitungRoyalti(hargaBukuFiksi, 15));
        
       
        System.out.println("\nMenyimpan data bukuFiksi1 ke txt...");
        bukuFiksi1.simpanFile("buku_fiksi_output.txt");

        System.out.println("\nMembaca data dari txt...");
        Buku bukuDariFile = new Buku("Kosong", "Tidak ada", fiksi, Arrays.asList(new Penulis("Kosong")));
      
        bukuDariFile.bacaFile("dummy.txt"); 
        
        System.out.println("Hasil baca dari file:");
        bukuDariFile.displayInfo();
    
    }
}