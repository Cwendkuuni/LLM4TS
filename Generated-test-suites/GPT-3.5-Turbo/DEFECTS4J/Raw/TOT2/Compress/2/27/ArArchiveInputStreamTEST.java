import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] data = "!<arch>\n".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArArchiveInputStream ais = new ArArchiveInputStream(in);
        assertNotNull(ais.getNextArEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] data = "!<arch>\n".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArArchiveInputStream ais = new ArArchiveInputStream(in);
        assertNotNull(ais.getNextEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] data = "test".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArArchiveInputStream ais = new ArArchiveInputStream(in);
        assertEquals('t', ais.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = "test".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArArchiveInputStream ais = new ArArchiveInputStream(in);
        byte[] buffer = new byte[2];
        assertEquals(2, ais.read(buffer));
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] data = "test".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArArchiveInputStream ais = new ArArchiveInputStream(in);
        byte[] buffer = new byte[4];
        assertEquals(4, ais.read(buffer, 0, 4));
    }

    @Test
    public void testMatches() {
        byte[] data = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(data, data.length));
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = "test".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArArchiveInputStream ais = new ArArchiveInputStream(in);
        ais.close();
        assertTrue(ais.isClosed());
    }

}