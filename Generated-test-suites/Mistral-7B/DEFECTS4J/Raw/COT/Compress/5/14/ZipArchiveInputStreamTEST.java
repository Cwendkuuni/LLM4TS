import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class ZipArchiveInputStreamTest {

    @Test
    public void testConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextZipEntry_NormalFile() throws IOException {
        String testFile = "testfile.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntry_NullInputStream() {
        InputStream inputStream = null;
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextZipEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntry_EmptyFile() throws IOException {
        String testFile = "emptyfile.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntry_InvalidFileSignature() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{"0x01", "0x02", "0x03", "0x04", "0x01", "0x02"}); // Invalid signature
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntry_CentralDirectoryReached() throws IOException {
        String testFile = "centraldirectory.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        // Get all entries in the central directory
        while (zipArchiveEntry != null) {
            zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        }
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testRead_StoredData() throws IOException {
        String testFile = "storeddata.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        byte[] buffer = new byte[1024];
        int bytesRead = 0;

        // Read data from the entry
        while ((bytesRead = zipArchiveInputStream.read(buffer)) > 0) {
            // Do something with the data
        }

        Assert.assertFalse(zipArchiveInputStream.hasNextEntry());
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testRead_DeflatedData() throws IOException {
        String testFile = "deflateddata.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        byte[] buffer = new byte[1024];
        int bytesRead = 0;

        // Ensure the Inflater is not empty
        zipArchiveInputStream.inf.inflate(buffer, 0, buffer.length);

        // Read data from the entry
        while ((bytesRead = zipArchiveInputStream.read(buffer)) > 0) {
            // Do something with the data
        }

        Assert.assertFalse(zipArchiveInputStream.hasNextEntry());
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = EOFException.class)
    public void testRead_EndOfStream() throws IOException {
        String testFile = "testfile.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        byte[] buffer = new byte[1024];
        int bytesRead = 0;

        // Read all data from the entry
        while ((bytesRead = zipArchiveInputStream.read(buffer)) > 0) {
            // Do something with the data
        }

        // Attempt to read data after the end of the stream
        zipArchiveInputStream.read(buffer);
    }

    @Test(expected = IOException.class)
    public void testRead_ClosedStream() throws IOException {
        String testFile = "testfile.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.close();
        zipArchiveInputStream.read(new byte[1024]);
    }

    @Test(expected = IOException.class)
    public void testRead_NullBuffer() throws IOException {
        String testFile = "testfile.zip";
        InputStream inputStream = new FileInputStream(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.read(null);
    }
}