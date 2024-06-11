import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] data = "!<arch>\n".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream ais = new ArArchiveInputStream(input);

        ArArchiveEntry entry = ais.getNextArEntry();
        assertNotNull(entry);
        assertEquals(ArArchiveEntry.HEADER, new String(data));
    }

    @Test
    public void testGetNextArEntryReturnsNull() throws IOException {
        ArArchiveInputStream ais = new ArArchiveInputStream(new ByteArrayInputStream(new byte[10]));

        ArArchiveEntry entry = ais.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testRead() throws IOException {
        byte[] data = "Data".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream ais = new ArArchiveInputStream(input);

        assertEquals('D', ais.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] expected = "Data".getBytes();
        byte[] data = new byte[4];
        ByteArrayInputStream input = new ByteArrayInputStream(expected);
        ArArchiveInputStream ais = new ArArchiveInputStream(input);

        int bytesRead = ais.read(data);
        assertArrayEquals(expected, data);
        assertEquals(4, bytesRead);
    }

    @Test
    public void testMatches() {
        byte[] signature = {'!', '<', 'a', 'r', 'c', 'h', '>', '\n'};
        assertTrue(ArArchiveInputStream.matches(signature, 8));
    }

}