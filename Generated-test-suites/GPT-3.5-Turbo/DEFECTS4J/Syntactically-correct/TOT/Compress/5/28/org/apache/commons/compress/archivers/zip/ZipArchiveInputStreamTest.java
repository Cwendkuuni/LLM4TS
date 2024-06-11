import org.junit.Test;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipException;

import static org.junit.Assert.*;

public class ZipArchiveInputStreamTest {

    private byte[] zipData = new byte[] {80, 75, 3, 4, 10, 0, 0, 0, 0, 0, 0, 0};

    @Test
    public void testGetNextZipEntry() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipData);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ZipArchiveEntry zipEntry = zipArchiveInputStream.getNextZipEntry();

        assertNull(zipEntry);

        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextZipEntryWithInvalidZipData() throws IOException {
        byte[] invalidZipData = new byte[] {80, 75, 1, 2, 3, 4, 10, 0, 0, 0} ;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidZipData);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        try {
            zipArchiveInputStream.getNextZipEntry();
            fail("Expected ZipException to be thrown");
        } catch (ZipException e) {
            assertEquals("Invalid zip file. Signature mismatch.", e.getMessage());
        }

        zipArchiveInputStream.close();
    }

    @Test
    public void testRead() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipData);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] buffer = new byte[100];
        int bytesRead = zipArchiveInputStream.read(buffer);

        assertEquals(-1, bytesRead);

        zipArchiveInputStream.close();
    }

    @Test
    public void testReadWithClosedStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipData);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.close();

        byte[] buffer = new byte[100];

        try {
            zipArchiveInputStream.read(buffer);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("The stream is closed", e.getMessage());
        }
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipData);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        zipArchiveInputStream.close();

        try {
            zipArchiveInputStream.getNextZipEntry();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("The stream is closed", e.getMessage());
        }
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipData);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        long skippedBytes = zipArchiveInputStream.skip(10);

        assertEquals(10, skippedBytes);

        zipArchiveInputStream.close();
    }

    @Test
    public void testSkipWithNegativeValue() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipData);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        try {
            zipArchiveInputStream.skip(-10);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        zipArchiveInputStream.close();
    }

}