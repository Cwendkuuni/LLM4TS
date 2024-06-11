import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class ZipArchiveInputStreamTest {

    @Test
    public void testValidZipFile() throws IOException {
        try (InputStream inputStream = this.getClass().getResourceAsStream("/test.zip")) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
            ZipArchiveEntry entry;
            while ((entry = zipArchiveInputStream.getNextZipEntry()) != null) {
                // Process the entry as needed
                System.out.println("Processing entry: " + entry.getName());
                Assert.assertNotNull(entry.getName());
                Assert.assertEquals(-1, entry.getSize()); // DEFLATE algorithm used
                Assert.assertNull(entry.getExtra()); // Unused extra field

                byte[] buffer = new byte[4096];
                int length;
                while ((length = zipArchiveInputStream.read(buffer)) > 0) {
                    // Process the data as needed
                }
                zipArchiveInputStream.closeEntry();
            }
            zipArchiveInputStream.close();
        }
    }

    @Test(expected = IOException.class)
    public void testMalformedZipFile() throws IOException {
        try (InputStream inputStream = this.getClass().getResourceAsStream("/malformed.zip")) {
            new ZipArchiveInputStream(inputStream);
            fail("Should have thrown an IOException");
        }
    }

    @Test(expected = IOException.class)
    public void testEmptyZipFile() throws IOException {
        try (InputStream inputStream = this.getClass().getResourceAsStream("/empty.zip")) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
            Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
            zipArchiveInputStream.close();
        }
    }
}