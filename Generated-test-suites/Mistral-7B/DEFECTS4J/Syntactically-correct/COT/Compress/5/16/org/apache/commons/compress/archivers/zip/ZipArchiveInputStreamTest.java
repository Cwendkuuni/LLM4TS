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
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {

    @Test
    public void testConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextZipEntryWithValidFileHeader() throws IOException {
        String testFile = "testFile.zip";
        InputStream inputStream = this.getClass().getResourceAsStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryWithInvalidFileHeader() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry(); // This should throw an exception
        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextZipEntryWithCentralDirectory() throws IOException {
        String testFile = "testFile.zip";
        InputStream inputStream = this.getClass().getResourceAsStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry(); // This should return null, as we have reached the central directory
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testReadWithStoredMethod() throws IOException {
        String testFile = "testFile.zip";
        InputStream inputStream = this.getClass().getResourceAsStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        byte[] buffer = new byte[ZipArchiveInputStream.BUFFER_SIZE];
        byte[] expected = new byte[zipArchiveEntry.getSize()];
        InputStream entryInputStream = zipArchiveInputStream.getInputStream();
        int bytesRead = 0;
        while ((bytesRead = entryInputStream.read(buffer)) != -1) {
            System.arraycopy(buffer, 0, expected, 0, bytesRead);
        }
        entryInputStream.close();
        int remainingBytesToRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, remainingBytesToRead);
        Assert.assertArrayEquals(expected, buffer);
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testReadWithDeflatedMethod() throws IOException {
        String testFile = "testFile.zip";
        InputStream inputStream = this.getClass().getResourceAsStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        byte[] buffer = new byte[ZipArchiveInputStream.BUFFER_SIZE];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        zipArchiveInputStream.setOutput(outputStream);
        zipArchiveEntry.getInputStream().close();
        zipArchiveInputStream.closeEntry();
        byte[] expected = outputStream.toByteArray();
        int remainingBytesToRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, remainingBytesToRead);
        Assert.assertEquals(expected.length, outputStream.size());
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadWithInvalidStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        zipArchiveInputStream.setOutput(outputStream);
        zipArchiveInputStream.read();
    }

    @Test
    public void testReadWithEOF() throws IOException {
        String testFile = "testFile.zip";
        InputStream inputStream = this.getClass().getResourceAsStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        byte[] buffer = new byte[ZipArchiveInputStream.BUFFER_SIZE];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertNotEquals(-1, bytesRead);
        zipArchiveInputStream.closeEntry();
        bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, bytesRead);
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = EOFException.class)
    public void testSkipWithEndOfFile() throws IOException {
        String testFile = "testFile.zip";
        InputStream inputStream = this.getClass().getResourceAsStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.skip(Long.MAX_VALUE);
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testSkipWithValidLength() throws IOException {
        String testFile = "testFile.zip";
        InputStream inputStream = this.getClass().getResourceAsStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        long skipBytes = zipArchiveEntry.getSize() - ZipArchiveInputStream.BUFFER_SIZE;
        zipArchiveInputStream.skip(skipBytes);
        zipArchiveInputStream.closeEntry();
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipWithNegativeLength() throws IOException {
        String testFile = "testFile.zip";
        InputStream inputStream = this.getClass().getResourceAsStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.skip(-1);
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCloseWithStreamAlreadyClosed() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.close();
        zipArchiveInputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        String testFile = "testFile.zip";
        InputStream inputStream = this.getClass().getResourceAsStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.close();
        inputStream.close();
    }
}