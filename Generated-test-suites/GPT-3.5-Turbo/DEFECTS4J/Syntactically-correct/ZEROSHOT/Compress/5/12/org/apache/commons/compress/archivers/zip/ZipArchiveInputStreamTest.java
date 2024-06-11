import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import static org.junit.Assert.*;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipArchiveInputStream;

    @Before
    public void setUp() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{80, 75, 3, 4, 20, 0, 0, 0, 8, 0, 23, 78, 40, 81, 123, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
    }

    @Test
    public void getNextZipEntry() throws IOException {
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertNotNull(entry);
        assertEquals(8, entry.getMethod());
        assertEquals(15620, entry.getSize());
    }

    @Test
    public void getNextEntry() throws IOException {
        ArchiveEntry entry = zipArchiveInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals(8, entry.getMethod());
        assertEquals(15620, entry.getSize());
    }

    @Test
    public void read() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(10, bytesRead);
    }

    @Test(expected = IOException.class)
    public void close() throws IOException {
        zipArchiveInputStream.close();
        // Subsequent read should throw IOException
        byte[] buffer = new byte[10];
        zipArchiveInputStream.read(buffer, 0, buffer.length);
    }

    @Test
    public void skip() throws IOException {
        long skipped = zipArchiveInputStream.skip(5);
        assertEquals(5, skipped);
    }

    @Test
    public void matches() {
        byte[] signature = {80, 75, 3, 4};
        assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
    }
}