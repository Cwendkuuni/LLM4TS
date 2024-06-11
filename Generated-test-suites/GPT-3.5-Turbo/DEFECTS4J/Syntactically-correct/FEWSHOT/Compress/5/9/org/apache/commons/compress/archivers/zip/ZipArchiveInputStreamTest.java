import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiveInputStreamTest {

    private InputStream inputStream;

    @Before
    public void setUp() {
        inputStream = getClass().getResourceAsStream("test.zip");
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);

        ArchiveEntry entry1 = zipInputStream.getNextEntry();
        assertEquals("file1.txt", entry1.getName());
        assertTrue(entry1 instanceof ZipArchiveEntry);

        ArchiveEntry entry2 = zipInputStream.getNextEntry();
        assertEquals("file2.txt", entry2.getName());
        assertTrue(entry2 instanceof ZipArchiveEntry);

        ArchiveEntry entry3 = zipInputStream.getNextEntry();
        assertNull(entry3);

        zipInputStream.close();
    }

    @Test
    public void testRead() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);

        zipInputStream.getNextEntry();
        byte[] buffer = new byte[1024];
        int read = zipInputStream.read(buffer);
        assertEquals("This is file 1.", new String(buffer, 0, read));

        zipInputStream.getNextEntry();
        read = zipInputStream.read(buffer);
        assertEquals("This is file 2.", new String(buffer, 0, read));

        zipInputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);
        zipInputStream.close();
        assertNull(zipInputStream.getNextEntry());
    }

    @Test(expected = EOFException.class)
    public void testReadFully() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);

        zipInputStream.getNextEntry();
        byte[] buffer = new byte[1024];
        zipInputStream.readFully(buffer);
        assertEquals("This is file 1.", new String(buffer));

        zipInputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);

        zipInputStream.getNextEntry();
        long skipped = zipInputStream.skip(5);
        assertEquals(5, skipped);

        byte[] buffer = new byte[1024];
        int read = zipInputStream.read(buffer);
        assertEquals("is file 1.", new String(buffer, 0, read));

        zipInputStream.close();
    }

    @Test
    public void testMatches() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("test.zip");
        byte[] signature = new byte[4];
        fileInputStream.read(signature);

        boolean matches = ZipArchiveInputStream.matches(signature, signature.length);
        assertTrue(matches);

        fileInputStream.close();
    }
}