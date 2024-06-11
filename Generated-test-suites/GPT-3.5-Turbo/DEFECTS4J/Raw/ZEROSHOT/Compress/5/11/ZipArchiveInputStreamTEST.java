import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipFile;

public class ZipArchiveInputStreamTest {

    private final String testFilePath = "pathToFile";

    @Test
    public void testGetNextZipEntry() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(testFilePath);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(fileInputStream);

        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();

        Assert.assertNotNull(entry);
        Assert.assertEquals("Expected file name", entry.getName(), "testFile.zip");
    }

    @Test
    public void testRead() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(testFilePath);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(fileInputStream);

        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();

        byte[] buffer = new byte[entry.getSize()];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);

        Assert.assertEquals(buffer.length, bytesRead);
    }

    @Test
    public void testClose() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(testFilePath);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(fileInputStream);

        zipArchiveInputStream.close();

        try {
            zipArchiveInputStream.getNextZipEntry();
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testSkip() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(testFilePath);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(fileInputStream);

        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();

        long skipped = zipArchiveInputStream.skip(100L);

        Assert.assertEquals(100L, skipped);
        Assert.assertEquals(entry.getSize() - 100L, zipArchiveInputStream.read(new byte[entry.getSize() - 100L], 0, (int) (entry.getSize() - 100L)));
    }

    @Test
    public void testMatches() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(testFilePath);
        byte[] signature = new byte[4];
        fileInputStream.read(signature);

        boolean matches = ZipArchiveInputStream.matches(signature, 4);

        Assert.assertTrue(matches);
    }
}