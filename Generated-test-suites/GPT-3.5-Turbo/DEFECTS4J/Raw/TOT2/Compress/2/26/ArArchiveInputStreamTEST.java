import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] data = {
            0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a,
            0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38,
            0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38,
            0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a
        };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInput = new ArArchiveInputStream(inputStream);

        assertNotNull(arInput.getNextArEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] data = {
            0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a,
            0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38,
            0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a
        };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInput = new ArArchiveInputStream(inputStream);

        assertNotNull(arInput.getNextEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] data = {
            0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a
        };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInput = new ArArchiveInputStream(inputStream);

        assertEquals(0x21, arInput.read());
    }

    @Test
    public void testReadBytes() throws IOException {
        byte[] data = {
            0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a
        };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInput = new ArArchiveInputStream(inputStream);

        byte[] buffer = new byte[5];
        arInput.read(buffer);
        assertArrayEquals(new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63}, buffer);
    }

    @Test
    public void testMatches() {
        byte[] signature = {
            0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a
        };
        int length = signature.length;
        
        assertTrue(ArArchiveInputStream.matches(signature, length));
    }
}