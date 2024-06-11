import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] data = "!<arch>\n".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream stream = new ArArchiveInputStream(input);

        ArArchiveEntry entry = stream.getNextArEntry();

        assertNotNull(entry);
        assertEquals("", entry.getName());
        assertEquals(0, entry.getSize());

        stream.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] data = "!<arch>\n".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream stream = new ArArchiveInputStream(input);

        ArchiveEntry entry = stream.getNextEntry();

        assertNotNull(entry);
        assertTrue(entry instanceof ArArchiveEntry);

        stream.close();
    }

    @Test
    public void testRead() throws IOException {
        byte[] data = "!<arch>\n".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream stream = new ArArchiveInputStream(input);

        int result = stream.read();

        assertEquals(33, result);

        stream.close();
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = "!<arch>\n".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        byte[] buffer = new byte[8];

        int result = stream.read(buffer);

        assertEquals(8, result);
        assertArrayEquals(data, buffer);

        stream.close();
    }

    @Test
    public void testReadByteArrayOffLen() throws IOException {
        byte[] data = "!<arch>\n".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        byte[] buffer = new byte[8];

        int result = stream.read(buffer, 1, 6);

        assertEquals(6, result);
        assertArrayEquals(new byte[]{0, 33, 60, 97, 114, 99, 104, 62}, buffer);

        stream.close();
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        int length = signature.length;

        assertTrue(ArArchiveInputStream.matches(signature, length));
    }
}