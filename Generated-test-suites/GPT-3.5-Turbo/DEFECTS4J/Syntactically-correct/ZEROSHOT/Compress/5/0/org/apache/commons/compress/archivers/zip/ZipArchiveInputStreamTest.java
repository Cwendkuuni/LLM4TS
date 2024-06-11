import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveInputStreamTest {

    @Test
    public void testConstructor() {
        InputStream inputStream = new FileInputStream("test.zip");
        ZipArchiveInputStream zais = new ZipArchiveInputStream(inputStream);
        assertNotNull(zais);
    }

    @Test
    public void testGetNextZipEntry() {
        InputStream inputStream = new FileInputStream("test.zip");
        ZipArchiveInputStream zais = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry entry = zais.getNextZipEntry();
        assertNotNull(entry);
    }

    @Test
    public void testGetNextEntry() {
        InputStream inputStream = new FileInputStream("test.zip");
        ZipArchiveInputStream zais = new ZipArchiveInputStream(inputStream);
        ArchiveEntry entry = zais.getNextEntry();
        assertNotNull(entry);
    }

    @Test
    public void testRead() {
        InputStream inputStream = new FileInputStream("test.zip");
        ZipArchiveInputStream zais = new ZipArchiveInputStream(inputStream);
        byte[] buffer = new byte[1024];
        int bytesRead = zais.read(buffer, 0, buffer.length);
        assertTrue(bytesRead > 0);
    }

    @Test
    public void testClose() {
        InputStream inputStream = new FileInputStream("test.zip");
        ZipArchiveInputStream zais = new ZipArchiveInputStream(inputStream);
        zais.close();
        assertTrue(zais.isClosed());
    }

    @Test
    public void testSkip() {
        InputStream inputStream = new FileInputStream("test.zip");
        ZipArchiveInputStream zais = new ZipArchiveInputStream(inputStream);
        long skipped = zais.skip(1024);
        assertTrue(skipped > 0);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[4];
        System.arraycopy(ZipArchiveOutputStream.LFH_SIG, 0, signature, 0, 4);
        boolean result = ZipArchiveInputStream.matches(signature, 4);
        assertTrue(result);
    }
}