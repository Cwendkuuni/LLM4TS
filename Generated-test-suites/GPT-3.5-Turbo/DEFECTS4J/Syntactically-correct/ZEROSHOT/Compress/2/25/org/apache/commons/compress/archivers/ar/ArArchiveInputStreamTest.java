import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream archiveInputStream;
    private InputStream inputStream;

    @Before
    public void setUp() throws Exception {
        byte[] testData = "abcdefg".getBytes();
        inputStream = new ByteArrayInputStream(testData);
        archiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws Exception {
        archiveInputStream.close();
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArchiveEntry entry = archiveInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("Test", entry.getName());
        assertEquals(7, entry.getSize());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = archiveInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("Test", entry.getName());
        assertEquals(7, entry.getSize());
    }

    @Test
    public void testClose() throws IOException {
        archiveInputStream.close();
        assertTrue(archiveInputStream.isClosed());
    }

    @Test
    public void testRead() throws IOException {
        int data = archiveInputStream.read();
        assertEquals((byte) 'a', (byte) data);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[3];
        int bytesRead = archiveInputStream.read(buffer);
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[] { 'a', 'b', 'c' }, buffer);
    }

    @Test
    public void testReadByteArrayIntInt() throws IOException {
        byte[] buffer = new byte[3];
        int bytesRead = archiveInputStream.read(buffer, 1, 2);
        assertEquals(2, bytesRead);
        assertArrayEquals(new byte[] { 0, 'a', 'b' }, buffer);
    }

    @Test
    public void testMatches() throws IOException {
        byte[] signature = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        boolean matches = ArArchiveInputStream.matches(signature, 8);
        assertTrue(matches);
    }
}