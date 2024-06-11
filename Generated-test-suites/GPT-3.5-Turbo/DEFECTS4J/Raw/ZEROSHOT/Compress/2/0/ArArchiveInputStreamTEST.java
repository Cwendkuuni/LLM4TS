import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;

    @Before
    public void setUp() {
        // create a test input stream
        String testData = "!<arch>\n" + 
                          "testdata             10     0     0     644     9        \\n";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        arInputStream = new ArArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("testdata", entry.getName());
        assertEquals(10, entry.getLength());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arInputStream.getNextEntry();
        assertNotNull(entry);
        assertTrue(entry instanceof ArArchiveEntry);
        ArArchiveEntry arEntry = (ArArchiveEntry) entry;
        assertEquals("testdata", arEntry.getName());
        assertEquals(10, arEntry.getLength());
    }

    @Test
    public void testRead() throws IOException {
        int data = arInputStream.read();
        assertEquals('!', data);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[10];
        int read = arInputStream.read(buffer);
        assertEquals(10, read);
        assertArrayEquals("!<arch>\nte".getBytes(), buffer);
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] buffer = new byte[20];
        int read = arInputStream.read(buffer, 5, 10);
        assertEquals(10, read);
        assertArrayEquals("     !<arch>\nte".getBytes(), buffer);
    }

    @Test
    public void testClose() throws IOException {
        assertFalse(arInputStream.isClosed());
        arInputStream.close();
        assertTrue(arInputStream.isClosed());
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, 8));
    }

    @Test
    public void testInvalidMatches() {
        byte[] signature = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertFalse(ArArchiveInputStream.matches(signature, 7));
    }
}