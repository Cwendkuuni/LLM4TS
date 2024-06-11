import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.utils.ZipLong;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;

public class ZipArchiveInputStreamTest {

    @Test
    public void testConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextZipEntryWithValidLocalFileHeader() throws IOException {
        String testFileName = "testFile.zip";
        InputStream inputStream = new FileInputStream(testFileName);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, true);

        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        zipArchiveInputStream.close();

        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryWithInvalidLocalFileHeader() throws IOException {
        ByteArrayOutputStream testStream = new ByteArrayOutputStream();
        testStream.write(0x04);
        testStream.write(0x03);
        testStream.write(0x4b);
        testStream.write(0x50);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);
        testStream.write(0x00);

        InputStream inputStream = new ByteArrayInputStream(testStream.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        zipArchiveInputStream.getNextZipEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryWithCentralDirectory() throws IOException {
        String testFileName = "testFile.zip";
        InputStream inputStream = new FileInputStream(testFileName);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, true);

        // Get the first entry
        zipArchiveInputStream.getNextZipEntry();

        // Central Directory should be the next entry
        ZipArchiveEntry centralDirectoryEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(centralDirectoryEntry);

        zipArchiveInputStream.close();

        inputStream.close();
    }

    @Test
    public void testReadWithStoredMethod() throws IOException {
        String testFileName = "testFile.zip";
        InputStream inputStream = new FileInputStream(testFileName);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] buffer = new byte[256];

        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);

        int readBytes = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(readBytes, entry.getSize());

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testReadWithDeflatedMethod() throws IOException, DataFormatException {
        String testFileName = "testFile.zip";
        InputStream inputStream = new FileInputStream(testFileName);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[256];

        // Get the first entry
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);

        zipArchiveInputStream.inf.reset();
        zipArchiveInputStream.count(entry.getSize());

        while (zipArchiveInputStream.inf.needsInput()) {
            int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
            outputStream.write(buffer, 0, bytesRead);
        }

        byte[] expectedBytes = outputStream.toByteArray();

        int readBytes = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(readBytes, expectedBytes.length);

        Assert.assertArrayEquals(expectedBytes, buffer);

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCloseEntry() throws IOException {
        String testFileName = "testFile.zip";
        InputStream inputStream = new FileInputStream(testFileName);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);

        zipArchiveInputStream.closeEntry();
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadWithInvalidInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] buffer = new byte[256];

        int readBytes = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(readBytes, -1);

        zipArchiveInputStream.close();
    }
}