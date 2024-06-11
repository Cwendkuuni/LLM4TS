import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] arData = "!<arch>\n".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(arData);
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);

        ArArchiveEntry entry = arStream.getNextArEntry();
        assertNotNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] arData = "!<arch>\n".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(arData);
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);

        ArchiveEntry entry = arStream.getNextEntry();
        assertNotNull(entry);
    }

    @Test
    public void testRead() throws IOException {
        byte[] data = "test".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);

        int value = arStream.read();
        assertEquals('t', value);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = "test".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);

        byte[] buffer = new byte[4];
        int bytesRead = arStream.read(buffer);
        
        assertEquals(4, bytesRead);
        assertArrayEquals(data, buffer);
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] data = "test".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);

        byte[] buffer = new byte[4];
        int bytesRead = arStream.read(buffer, 1, 2);
        
        assertEquals(2, bytesRead);
        assertEquals('e', buffer[1]);
        assertEquals('s', buffer[2]);
    }

    @Test
    public void testMatches() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        boolean result = ArArchiveInputStream.matches(signature, 8);
        assertTrue(result);
    }

}