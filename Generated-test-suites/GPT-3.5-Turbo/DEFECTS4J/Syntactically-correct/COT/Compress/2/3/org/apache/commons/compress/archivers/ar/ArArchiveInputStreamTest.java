import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;

    @Before
    public void setUp() {
        byte[] testData = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x66, 0x6f, 0x6f};
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        arInputStream = new ArArchiveInputStream(input);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("foo", entry.getName());
        assertEquals(3, entry.getSize());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("foo", entry.getName());
        assertEquals(3, entry.getSize());
    }

    @Test
    public void testRead() throws IOException {
        int value = arInputStream.read();
        assertEquals(0x66, value);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[3];
        int bytesRead = arInputStream.read(buffer);
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0x66, 0x6f, 0x6f}, buffer);
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        byte[] buffer = new byte[6];
        int bytesRead = arInputStream.read(buffer, 1, 3);
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0x00, 0x66, 0x6f, 0x00, 0x00, 0x00}, buffer);
    }
    
    @Test
    public void testMatches() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        boolean result = ArArchiveInputStream.matches(signature, 8);
        assertTrue(result);
    }

    @Test
    public void testClose() throws IOException {
        arInputStream.close();
        assertTrue(arInputStream.isClosed());
    }
}