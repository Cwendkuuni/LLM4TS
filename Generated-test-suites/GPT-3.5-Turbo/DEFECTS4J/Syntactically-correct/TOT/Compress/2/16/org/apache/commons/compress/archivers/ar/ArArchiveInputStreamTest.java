
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arArchiveInputStream;
    private ByteArrayInputStream input;
    private ByteArrayOutputStream output;

    @Before
    public void setUp() {
        byte[] data = createArFileData();
        input = new ByteArrayInputStream(data);
        output = new ByteArrayOutputStream();
        arArchiveInputStream = new ArArchiveInputStream(input);
    }

    @After
    public void tearDown() throws IOException {
        arArchiveInputStream.close();
        input.close();
        output.close();
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArchiveEntry entry = arArchiveInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("file1.txt", entry.getName());
        assertEquals(10L, entry.getSize());

        entry = arArchiveInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("file2.txt", entry.getName());
        assertEquals(20L, entry.getSize());

        entry = arArchiveInputStream.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arArchiveInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("file1.txt", entry.getName());
        assertEquals(10L, entry.getSize());

        entry = arArchiveInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("file2.txt", entry.getName());
        assertEquals(20L, entry.getSize());

        entry = arArchiveInputStream.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testClose() throws IOException {
        arArchiveInputStream.close();
        assertTrue(arArchiveInputStream.isClosed());
    }

    @Test
    public void testRead() throws IOException {
        int readByte = arArchiveInputStream.read();
        assertEquals("First byte read should be 'f'", 'f', readByte);

        byte[] buffer = new byte[10];
        arArchiveInputStream.read(buffer);
        assertEquals("Buffer length should be 10", 10, buffer.length);
        assertArrayEquals("Buffer should contain 'irst line '", "irst line ".getBytes(), buffer);

        int readBytes = arArchiveInputStream.read(buffer, 0, 5);
        assertEquals("Number of bytes read should be 5", 5, readBytes);
        assertArrayEquals("Buffer should contain 'of te'", "of te".getBytes(), buffer);

        arArchiveInputStream.read(buffer);
        assertEquals("Buffer length should be 10", 10, buffer.length);
        assertArrayEquals("Buffer should contain 'st content'", "st content".getBytes(), buffer);
    }

    @Test
    public void testMatches() {
        assertFalse(ArArchiveInputStream.matches("invalidSignature".getBytes(), 8));
        assertFalse(ArArchiveInputStream.matches("wrongSignature".getBytes(), 7));
        assertFalse(ArArchiveInputStream.matches(new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e}, 8));
        assertTrue(ArArchiveInputStream.matches(new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a}, 8));
        assertFalse(ArArchiveInputStream.matches(new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x00}, 9));
    }

    private byte[] createArFileData() {
        StringBuilder sb = new StringBuilder();
        sb.append("!<arch>\n");
        sb.append("file1.txt       10        1457032072  1000  100  644  \n");
        sb.append("irst line of te");
        sb.append("file2.txt       20        1457032072  1000  100  755  \n");
        sb.append("st content     ");
        return sb.toString().getBytes();
    }
}
