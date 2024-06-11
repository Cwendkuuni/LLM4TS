import org.junit.Test;
import static org.junit.Assert.*;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveInputStream stream = new ArArchiveInputStream(new ByteArrayInputStream(new byte[] {}));
        assertNull(stream.getNextArEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArArchiveInputStream stream = new ArArchiveInputStream(new ByteArrayInputStream(new byte[] {}));
        assertNull(stream.getNextEntry());
    }

    @Test
    public void testClose() throws IOException {
        ArArchiveInputStream stream = new ArArchiveInputStream(new ByteArrayInputStream(new byte[] {}));
        stream.close();
        // no assertion, just checking if an exception is thrown
    }

    @Test
    public void testRead() throws IOException {
        ArArchiveInputStream stream = new ArArchiveInputStream(new ByteArrayInputStream(new byte[] {}));
        assertEquals(-1, stream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        ArArchiveInputStream stream = new ArArchiveInputStream(new ByteArrayInputStream(new byte[] {}));
        byte[] b = new byte[10];
        assertEquals(-1, stream.read(b));
    }

    @Test
    public void testReadByteArrayIntInt() throws IOException {
        ArArchiveInputStream stream = new ArArchiveInputStream(new ByteArrayInputStream(new byte[] {}));
        byte[] b = new byte[10];
        assertEquals(-1, stream.read(b, 0, 10));
    }

    @Test
    public void testMatches() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, 8));
    }
}