import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] data = new byte[100];
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        ArArchiveEntry entry = stream.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] data = new byte[100];
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        ArchiveEntry entry = stream.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = new byte[100];
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        stream.close();
        assertTrue(stream.isClosed());
    }

    @Test
    public void testRead() throws IOException {
        byte[] data = new byte[100];
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        int readByte = stream.read();
        assertEquals(0, readByte);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = new byte[100];
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        byte[] buffer = new byte[10];
        int bytesRead = stream.read(buffer);
        assertEquals(10, bytesRead);
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        byte[] data = new byte[100];
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        byte[] buffer = new byte[10];
        int bytesRead = stream.read(buffer, 1, 8);
        assertEquals(8, bytesRead);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, 8));
    }
}