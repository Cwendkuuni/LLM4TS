import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

public class ZipArchiveInputStreamTest {

    @Test
    public void testConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test
    public void testConstructorWithEncodingAndUseUnicodeExtraFields() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String encoding = "UTF-8";
        boolean useUnicodeExtraFields = true;
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, encoding, useUnicodeExtraFields);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextZipEntryWithValidLocalFileHeader() throws IOException {
        String testZipFile = "testZipFile.zip";
        InputStream inputStream = ZipFile.open(testZipFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
        zipArchiveInputStream.closeEntry();
        zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(zipArchiveEntry);
        zipArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryWithInvalidLocalFileHeader() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[10]); // Invalid signature
        outputStream.close();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextZipEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryWithCentralDirectory() throws IOException {
        String testZipFile = "testZipFile.zip";
        InputStream inputStream = ZipFile.open(testZipFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        while (zipArchiveEntry != null) {
            ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
            Assert.assertNotNull(zipArchiveEntry);
            zipArchiveInputStream.closeEntry();
        }
        zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(zipArchiveEntry);
        zipArchiveInputStream.close();
    }

    @Test
    public void testRead() throws IOException {
        String testZipFile = "testZipFile.zip";
        InputStream inputStream = ZipFile.open(testZipFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
        zipArchiveInputStream.closeEntry();

        byte[] buffer = new byte[100];
        int read = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertTrue(read > 0);

        zipArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadWithClosedStream() throws IOException {
        String testZipFile = "testZipFile.zip";
        InputStream inputStream = ZipFile.open(testZipFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.close();
        int read = zipArchiveInputStream.read(new byte[100], 0, 100);
    }

    @Test
    public void testClose() throws IOException {
        String testZipFile = "testZipFile.zip";
        InputStream inputStream = ZipFile.open(testZipFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.closeEntry();
        zipArchiveInputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        String testZipFile = "testZipFile.zip";
        InputStream inputStream = ZipFile.open(testZipFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.closeEntry();

        long skip = zipArchiveInputStream.skip(Long.MAX_VALUE);
        Assert.assertTrue(skip > 0);

        zipArchiveInputStream.close();
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[ZipArchiveOutputStream.LFH_SIG.length];
        Arrays.fill(signature, (byte) 0x04);
        Arrays.fill(signature, 2, signature.length, (byte) 0x03);
        Arrays.fill(signature, 4, signature.length, (byte) 0x4B);
        Arrays.fill(signature, 5, signature.length, (byte) 0x50);
        Assert.assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testCloseEntry() throws IOException {
        String testZipFile = "testZipFile.zip";
        InputStream inputStream = ZipFile.open(testZipFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.closeEntry();
        Assert.assertNull(zipArchiveEntry);
        zipArchiveInputStream.close();
    }

    @Test
    public void testFill() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.fill();
        Assert.assertTrue(zipArchiveInputStream.inf.needsInput());
        zipArchiveInputStream.close();
    }
}