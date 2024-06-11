import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {
    
    private ArArchiveInputStream arInputStream;

    @Before
    public void setup() {
        InputStream input = new ByteArrayInputStream(new byte[]{}); // Empty input stream for testing
        arInputStream = new ArArchiveInputStream(input);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        assertNull(arInputStream.getNextArEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        assertNull(arInputStream.getNextEntry());
    }

    @Test
    public void testClose() throws IOException {
        arInputStream.close();
        assertTrue(arInputStream.closed);
        assertNull(arInputStream.input);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(-1, arInputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] b = new byte[5];
        assertEquals(0, arInputStream.read(b));
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0}, b);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] b = new byte[5];
        assertEquals(0, arInputStream.read(b, 1, 3));
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0}, b);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

}