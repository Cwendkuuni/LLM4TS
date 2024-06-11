import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testConstructor() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        assertNotNull(arArchiveInputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        assertNull(arArchiveInputStream.getNextArEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        assertNull(arArchiveInputStream.getNextEntry());
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.close();
        assertTrue(arArchiveInputStream.isClosed());
    }

    @Test
    public void testRead() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        assertEquals(0, arArchiveInputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        byte[] buffer = new byte[5];
        assertEquals(5, arArchiveInputStream.read(buffer));
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        byte[] buffer = new byte[5];
        assertEquals(5, arArchiveInputStream.read(buffer, 1, 3));
    }

    @Test
    public void testMatches() {
        byte[] signature = {0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, 8));
    }
}