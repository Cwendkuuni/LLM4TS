
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipInputStream;
    private InputStream input;

    @Before
    public void setUp() {
        // Initialize the test input stream
        byte[] testData = "Test data".getBytes();
        input = new ByteArrayInputStream(testData);

        // Initialize the ZipArchiveInputStream
        zipInputStream = new ZipArchiveInputStream(input);
    }

    @After
    public void tearDown() throws IOException {
        // Close the ZipArchiveInputStream
        zipInputStream.close();
        input.close();
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ArchiveEntry entry = zipInputStream.getNextZipEntry();
        assertNotNull(entry);
        assertEquals(ZipArchiveEntry.class, entry.getClass());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = zipInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals(ZipArchiveEntry.class, entry.getClass());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = zipInputStream.read(buffer, 0, buffer.length);
        assertEquals(9, bytesRead);
        assertEquals("Test data", new String(buffer).trim());
    }

    @Test(expected = IOException.class)
    public void testReadClosed() throws IOException {
        zipInputStream.close();
        byte[] buffer = new byte[10];
        zipInputStream.read(buffer, 0, buffer.length);
    }

    @Test
    public void testSkip() throws IOException {
        long skippedBytes = zipInputStream.skip(4);
        assertEquals(4, skippedBytes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegative() throws IOException {
        zipInputStream.skip(-1);
    }

    @Test
    public void testMatches() {
        byte[] signature = "PK\003\004".getBytes();
        assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
    }

    @Test(timeout = 1000)
    public void testClose() throws IOException {
        zipInputStream.close();
    }

    @Test
    public void testCloseEntry() throws IOException {
        ArchiveEntry entry = zipInputStream.getNextEntry();
        assertNotNull(entry);
        zipInputStream.closeEntry();
        assertNull(zipInputStream.getNextEntry());
    }

    @Test
    public void testFill() throws IOException {
        zipInputStream.fill();
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] buffer = new byte[10];
        zipInputStream.readFully(buffer);
        assertEquals("Test data", new String(buffer).trim());
    }

}