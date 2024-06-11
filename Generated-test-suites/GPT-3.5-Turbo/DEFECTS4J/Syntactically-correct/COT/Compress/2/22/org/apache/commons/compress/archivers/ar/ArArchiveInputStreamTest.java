import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry_basic() throws IOException {
        byte[] data = "header name lastmodified userid groupid filemode length trailer".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInput = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry = arInput.getNextArEntry();

        assertNotNull(entry);
        assertEquals("name", entry.getName());
        assertEquals(6, entry.getLength());

        arInput.close();
    }

    @Test
    public void testGetNextArEntry_emptyInput() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arInput = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry = arInput.getNextArEntry();

        assertNull(entry);

        arInput.close();
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arInput = new ArArchiveInputStream(inputStream);

        arInput.close();

        // Verify that the input stream is closed
        try {
            arInput.getNextArEntry();
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }
    }

    @Test
    public void testRead_basic() throws IOException {
        byte[] data = "test".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInput = new ArArchiveInputStream(inputStream);

        int result = arInput.read();

        assertEquals('t', result);

        arInput.close();
    }

    @Test
    public void testRead_array() throws IOException {
        byte[] data = "test".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInput = new ArArchiveInputStream(inputStream);
        byte[] buffer = new byte[2];

        int bytesRead = arInput.read(buffer);

        assertArrayEquals(new byte[]{'t', 'e'}, buffer);
        assertEquals(2, bytesRead);

        arInput.close();
    }

    @Test
    public void testRead_offsetLength() throws IOException {
        byte[] data = "test".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInput = new ArArchiveInputStream(inputStream);
        byte[] buffer = new byte[4];

        int bytesRead = arInput.read(buffer, 1, 2);

        assertArrayEquals(new byte[]{0, 't', 'e', 0}, buffer);
        assertEquals(2, bytesRead);

        arInput.close();
    }

    @Test
    public void testMatches() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        boolean result = ArArchiveInputStream.matches(signature, signature.length);

        assertTrue(result);
    }

    @Test
    public void testMatches_invalidSignature() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e};
        boolean result = ArArchiveInputStream.matches(signature, signature.length);

        assertFalse(result);
    }

    @Test
    public void testMatches_invalidLength() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        boolean result = ArArchiveInputStream.matches(signature, 6);

        assertFalse(result);
    }

}