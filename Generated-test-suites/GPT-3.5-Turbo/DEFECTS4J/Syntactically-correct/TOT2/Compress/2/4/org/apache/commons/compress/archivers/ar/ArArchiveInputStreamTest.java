import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry_NullInput() throws IOException {
        ArArchiveInputStream arInput = new ArArchiveInputStream(null);
        assertNull(arInput.getNextArEntry());
    }

    @Test
    public void testRead_EmptyInput() throws IOException {
        byte[] input = new byte[0];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        ArArchiveInputStream arInput = new ArArchiveInputStream(byteArrayInputStream);
        assertEquals(-1, arInput.read());
    }

    @Test
    public void testGetNextEntry_NoEntry() throws IOException {
        byte[] input = new byte[0];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        ArArchiveInputStream arInput = new ArArchiveInputStream(byteArrayInputStream);
        assertNull(arInput.getNextEntry());
    }

    @Test
    public void testMatches_ValidSignature() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, 8));
    }

    @Test
    public void testMatches_InvalidLength() {
        byte[] signature = {0x21, 0x3c, 0x61};
        assertFalse(ArArchiveInputStream.matches(signature, 3));
    }

    @Test
    public void testMatches_InvalidSignature() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0b};
        assertFalse(ArArchiveInputStream.matches(signature, 8));
    }

}