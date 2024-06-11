import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {
    private ArArchiveInputStream arInputStream;

    @Before
    public void setUp() {
        // Create a dummy input stream for testing
        byte[] data = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        arInputStream = new ArArchiveInputStream(input);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("ar", entry.getName());
        assertEquals(0, entry.getLength());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arInputStream.getNextEntry();
        assertTrue(entry instanceof ArArchiveEntry);
        ArArchiveEntry arEntry = (ArArchiveEntry) entry;
        assertEquals("ar", arEntry.getName());
        assertEquals(0, arEntry.getLength());
    }

    @Test
    public void testRead() throws IOException {
        int data = arInputStream.read();
        assertEquals(0x21, data);
    }

    @Test
    public void testReadBytes() throws IOException {
        byte[] buffer = new byte[8];
        int bytesRead = arInputStream.read(buffer);
        assertEquals(8, bytesRead);
        assertArrayEquals(new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a}, buffer);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, 8));
    }

    @Test
    public void testClose() throws IOException {
        assertFalse(arInputStream.isClosed());
        arInputStream.close();
        assertTrue(arInputStream.isClosed());
    }
}