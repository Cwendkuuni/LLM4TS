
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipArchiveInputStream;

    @Before
    public void setUp() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        assertNull(zipArchiveInputStream.getNextZipEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        assertNull(zipArchiveInputStream.getNextEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        assertEquals(-1, zipArchiveInputStream.read(buffer, 0, 10));
    }

    @Test(expected = IOException.class)
    public void testRead_ClosedStream() throws IOException {
        zipArchiveInputStream.close();
        byte[] buffer = new byte[10];
        zipArchiveInputStream.read(buffer, 0, 10);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRead_InvalidBufferIndex() throws IOException {
        byte[] buffer = new byte[10];
        zipArchiveInputStream.read(buffer, 10, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkip_NegativeValue() throws IOException {
        zipArchiveInputStream.skip(-10);
    }

    @Test
    public void testSkip() throws IOException {
        assertEquals(0, zipArchiveInputStream.skip(10));
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[10];
        assertFalse(ZipArchiveInputStream.matches(signature, 5));
    }

    @Test
    public void testClose() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.close(); // Should not throw exception
    }

    @Test
    public void testCloseEntry() throws IOException {
        zipArchiveInputStream.closeEntry();
    }
}
