import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

public class ZipArchiveInputStreamTest {
   
    @Test
    public void testGetNextZipEntry() throws IOException {
        byte[] zipData = new byte[] {80, 75, 3, 4, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipData);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        assertNotNull(zipArchiveInputStream.getNextZipEntry());
        assertNull(zipArchiveInputStream.getNextZipEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] zipData = new byte[] {80, 75, 3, 4, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipData);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        byte[] buffer = new byte[10];
        assertEquals(-1, zipArchiveInputStream.read(buffer, 0, 10));
    }

    @Test
    public void testClose() throws IOException {
        byte[] zipData = new byte[] {80, 75, 3, 4, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipData);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.close();
        try {
            zipArchiveInputStream.getNextZipEntry();
            fail("Should have thrown IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testSkip() throws IOException {
        byte[] zipData = new byte[] {80, 75, 3, 4, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipData);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        assertEquals(0, zipArchiveInputStream.skip(10));
    }
}