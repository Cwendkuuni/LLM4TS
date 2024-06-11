
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;
    private InputStream input;

    @Before
    public void setUp() {
        // Create a sample AR archive input stream for testing
        String header = "!<arch>\n";
        String entryHeader = "TEST\t1/1/70\t0\t0\t1000\t0\t\t\t100\t\t\t\t\t\t\t\t\t\n";
        byte[] testData = "This is a test entry.".getBytes();
        String trailer = "`\n";

        String archiveData = header + entryHeader + new String(testData) + trailer;
        input = new ByteArrayInputStream(archiveData.getBytes());
        arInputStream = new ArArchiveInputStream(input);
    }

    @After
    public void tearDown() throws IOException {
        // Close the archive input stream after each test
        if (arInputStream != null) {
            arInputStream.close();
        }

        if (input != null) {
            input.close();
        }
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("TEST", entry.getName());
        assertEquals(100, entry.getSize());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArArchiveEntry entry = arInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("TEST", entry.getName());
        assertEquals(100, entry.getSize());
    }

    @Test
    public void testRead() throws IOException {
        int bytesRead = arInputStream.read();
        assertEquals('T', bytesRead);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = arInputStream.read(buffer);
        assertEquals(10, bytesRead);
        assertArrayEquals("This is a ".getBytes(), buffer);
    }

    @Test
    public void testReadByteArrayOffLen() throws IOException {
        byte[] buffer = new byte[20];
        int bytesRead = arInputStream.read(buffer, 5, 10);
        assertEquals(10, bytesRead);
        assertArrayEquals(new byte[5], buffer, 0, 5);
        assertArrayEquals("This is a ".getBytes(), buffer, 5, 10);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, 8));
    }

}
