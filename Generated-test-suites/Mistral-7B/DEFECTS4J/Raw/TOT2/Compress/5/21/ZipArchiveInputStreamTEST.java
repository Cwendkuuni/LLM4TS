import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ZipArchiveInputStreamTest {

    @Test
    public void testConstructors() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertNotNull(zipArchiveInputStream);

        ZipEncoding encoding = ZipEncodingHelper.UTF8;
        ZipArchiveInputStream zipArchiveInputStreamUTF8 = new ZipArchiveInputStream(inputStream, encoding, true);
        Assert.assertNotNull(zipArchiveInputStreamUTF8);
    }

    @Test
    public void testGetNextZipEntry_NormalCase() throws Exception {
        String zipFileContent = "PK\u0003\u0004\u0014\u0000\u0000\u0001local\u0001\u0000\u0000\u0000\u0000\u0000\u0001\u0000\u0000\u0000\u0001\u0000\u0000\u0000text.txt\u0000\u0002\u0001\u0000\u0001\u0000\u0000\u0000\u0001\u0000\u0000\u0000\u0002\u0000\u0000\u0000";
        InputStream inputStream = new ByteArrayInputStream(zipFileContent.getBytes());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
        Assert.assertEquals("local", zipArchiveEntry.getName());
        zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(zipArchiveEntry);
    }

    @Test
    public void testGetNextZipEntry_NullInput() {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(null);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
    }

    @Test
    public void testGetNextZipEntry_EOF() throws Exception {
        String zipFileContent = "PK\u0003\u0004\u0014\u0000\u0000\u0001local\u0001\u0000\u0000\u0000\u0000\u0000\u0001\u0000\u0000\u0000\u0001\u0000\u0000\u0000text.txt\u0000\u0002\u0001\u0000\u0001\u0000\u0000\u0000\u0001\u0000\u0000\u0000\u0002\u0000\u0000\u0000";
        InputStream inputStream = new ByteArrayInputStream(zipFileContent.getBytes());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
        zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(zipArchiveEntry);

        ZipArchiveInputStream zipArchiveInputStreamClone = new ZipArchiveInputStream(inputStream);
        Assert.assertNull(zipArchiveInputStreamClone.getNextZipEntry());
    }

    @Test
    public void testGetNextZipEntry_InvalidSignature() throws Exception {
        String zipFileContent = "PK\u0003\u0004\u0014\u0000\u0000\u0001local\u0001\u0000\u0000\u0000\u0000\u0000\u0001\u0000\u0000\u0000\u0001\u0000\u0000\u0000text.txt\u0005\u0002\u0001\u0000\u0001\u0000\u0000\u0000\u0001\u0000\u0000\u0000\u0002\u0000\u0000\u0000";
        InputStream inputStream = new ByteArrayInputStream(zipFileContent.getBytes());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(zipArchiveEntry);
    }

    @Test
    public void testGetNextZipEntry_NullName() throws Exception {
        String zipFileContent = "PK\u0003\u0004\u0014\u0000\u0000\u0001\u0000\u0000\u0000\u0001\u0000\u0000\u0000\u0001\u0000\u0000\u0000";
        InputStream inputStream = new ByteArrayInputStream(zipFileContent.getBytes());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(zipArchiveEntry);
    }

    @Test
    public void testRead() throws Exception {
        File testFile = new File("test.zip");
        ZipFile zipFile = new ZipFile(testFile);
        InputStream inputStream = zipFile.getInputStream("test.txt");
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] expectedContent = new byte[]{'T', 'e', 's', 't', '.', 't', 'x', 't'};
        byte[] buffer = new byte[expectedContent.length];

        int bytesRead = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(expectedContent.length, bytesRead);
        Assert.assertArrayEquals(expectedContent, buffer);

        // Test reading with a different byte array size
        byte[] buffer2 = new byte[expectedContent.length + 1];
        int bytesRead2 = zipArchiveInputStream.read(buffer2);
        Assert.assertEquals(-1, bytesRead2);

        zipArchiveInputStream.close();
        zipFile.close();
        testFile.delete();
    }

    @Test
    public void testClose() throws Exception {
        File testFile = new File("test.zip");
        ZipFile zipFile = new ZipFile(testFile);
        InputStream inputStream = zipFile.getInputStream("test.txt");
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        zipArchiveInputStream.close();

        try {
            zipArchiveInputStream.read();
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }

        zipFile.close();
        testFile.delete();
    }

    @Test(expected = IOException.class)
    public void testRead_EOF() throws Exception {
        File testFile = new File("test.zip");
        ZipFile zipFile = new ZipFile(testFile);
        InputStream inputStream = zipFile.getInputStream("test.txt");
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] buffer = new byte[5];
        zipArchiveInputStream.read(buffer);
        zipArchiveInputStream.read(buffer);
        zipArchiveInputStream.read(buffer);
        zipArchiveInputStream.read(buffer);
        zipArchiveInputStream.read(buffer);

        // EOF exception should be thrown here
        zipArchiveInputStream.read(buffer);
    }

    @Test(expected = IOException.class)
    public void testRead_InputClosed() throws Exception {
        File testFile = new File("test.zip");
        ZipFile zipFile = new ZipFile(testFile);
        InputStream inputStream = zipFile.getInputStream("test.txt");
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        zipArchiveInputStream.close();

        zipArchiveInputStream.read(new byte[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRead_NegativeBufferSize() throws Exception {
        File testFile = new File("test.zip");
        ZipFile zipFile = new ZipFile(testFile);
        InputStream inputStream = zipFile.getInputStream("test.txt");
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        zipArchiveInputStream.read(new byte[]{-1});
    }
}