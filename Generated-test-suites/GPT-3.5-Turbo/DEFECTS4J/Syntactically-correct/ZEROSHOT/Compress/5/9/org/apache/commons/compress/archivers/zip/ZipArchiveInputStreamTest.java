import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipArchiveInputStream;
    private InputStream mockInputStream;

    @Before
    public void setUp() {
        mockInputStream = new ByteArrayInputStream("Test Zip Archive".getBytes());
        zipArchiveInputStream = new ZipArchiveInputStream(mockInputStream);
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertNotNull(entry);
        assertEquals("Test Zip Archive", entry.getName());
        assertEquals(0, entry.getMethod());
        assertEquals(0, entry.getTime());
        assertNull(entry.getExtra());

        ZipArchiveEntry nullEntry = zipArchiveInputStream.getNextZipEntry();
        assertNull(nullEntry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = zipArchiveInputStream.getNextEntry();
        assertNotNull(entry);
        assertTrue(entry instanceof ZipArchiveEntry);
        assertEquals("Test Zip Archive", entry.getName());
        assertEquals(0, ((ZipArchiveEntry) entry).getMethod());
        assertEquals(0, ((ZipArchiveEntry) entry).getTime());
        assertNull(((ZipArchiveEntry) entry).getExtra());

        ArchiveEntry nullEntry = zipArchiveInputStream.getNextEntry();
        assertNull(nullEntry);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = zipArchiveInputStream.read(buffer);
        assertEquals(10, bytesRead);
        assertArrayEquals("Test Zip ".getBytes(), buffer);

        int bytesSkipped = zipArchiveInputStream.skip(5);
        assertEquals(5, bytesSkipped);

        byte[] buffer2 = new byte[10];
        int bytesRead2 = zipArchiveInputStream.read(buffer2, 0, 10);
        assertEquals(5, bytesRead2);
        assertArrayEquals("Archive".getBytes(), buffer2);

        int bytesRead3 = zipArchiveInputStream.read(buffer2, 0, 10);
        assertEquals(-1, bytesRead3);
    }

    @Test
    public void testClose() throws IOException {
        zipArchiveInputStream.close();
        assertTrue(zipArchiveInputStream.isClosed());

        try {
            zipArchiveInputStream.getNextEntry();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("The stream is closed", e.getMessage());
        }
    }

    @Test
    public void testSkip() throws IOException {
        long bytesSkipped = zipArchiveInputStream.skip(5);
        assertEquals(5, bytesSkipped);
    }

    @Test
    public void testMatches() {
        byte[] signature1 = {0x50, 0x4B, 0x03, 0x04};
        assertTrue(ZipArchiveInputStream.matches(signature1, 4));

        byte[] signature2 = {0x50, 0x4B, 0x01, 0x02};
        assertFalse(ZipArchiveInputStream.matches(signature2, 4));

        byte[] signature3 = {0x50, 0x4B, 0x03};
        assertFalse(ZipArchiveInputStream.matches(signature3, 3));
    }
}