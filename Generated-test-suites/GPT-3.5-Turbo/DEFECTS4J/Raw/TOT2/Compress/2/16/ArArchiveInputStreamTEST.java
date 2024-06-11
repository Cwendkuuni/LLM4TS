import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] data = {
                0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x0a,
                0x1e, 0x44, 0x0a, 0x11, 0x30, 0x30, 0x30, 0x30,
                0x30, 0x30, 0x30, 0x31, 0x30, 0x30, 0x30, 0x30,
                0x30, 0x30, 0x30, 0x30, 0x0a, 0x1e, 0x45, 0x0a,
                0x11, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30,
                0x32, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30,
                0x30, 0x0a, 0x60, 0x0a, 0x11, 0x30, 0x30, 0x30,
                0x30, 0x30, 0x30, 0x30, 0x33, 0x30, 0x30, 0x30,
                0x30, 0x30, 0x30, 0x30, 0x30, 0x0a, 0x00, 0x0a
        };

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        ArArchiveEntry entry = arInput.getNextArEntry();

        assertNotNull(entry);
        assertEquals("    00000001", entry.getName());
        assertEquals(30, entry.getSize());

        entry = arInput.getNextArEntry();

        assertNotNull(entry);
        assertEquals("    00000002", entry.getName());
        assertEquals(30, entry.getSize());

        entry = arInput.getNextArEntry();

        assertNotNull(entry);
        assertEquals("    00000003", entry.getName());
        assertEquals(30, entry.getSize());

        entry = arInput.getNextArEntry();

        assertNull(entry);
    }

    @Test
    public void testRead() throws IOException {
        byte[] data = {
                0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a
        };

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        assertEquals(0x21, arInput.read());
        assertEquals(0x3c, arInput.read());
        assertEquals(0x61, arInput.read());
        assertEquals(0x72, arInput.read());
        assertEquals(0x63, arInput.read());
        assertEquals(0x68, arInput.read());
        assertEquals(0x3e, arInput.read());
        assertEquals(0x0a, arInput.read());

        assertEquals(-1, arInput.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = {
                0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a
        };

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        byte[] buffer = new byte[8];
        assertEquals(8, arInput.read(buffer));

        assertArrayEquals(data, buffer);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] data = {
                0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a
        };

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        byte[] buffer = new byte[8];
        assertEquals(3, arInput.read(buffer, 2, 3));

        assertEquals(0, buffer[0]);
        assertEquals(0, buffer[1]);
        assertEquals(0x21, buffer[2]);
        assertEquals(0x3c, buffer[3]);
        assertEquals(0x61, buffer[4]);
        assertEquals(0, buffer[5]);
        assertEquals(0, buffer[6]);
        assertEquals(0, buffer[7]);
    }

    @Test
    public void testMatches() {
        byte[] signature = {
                0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a
        };
        int length = 8;

        assertTrue(ArArchiveInputStream.matches(signature, length));
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        assertFalse(arInput.isClosed());

        arInput.close();

        assertTrue(arInput.isClosed());
    }

}