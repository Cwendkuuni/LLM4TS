import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead_SingleByte() throws IOException {
        byte[] input = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(in);

        int result = stream.read();

        assertEquals(1, result);
    }

    @Test
    public void testRead_Buffer() throws IOException {
        byte[] input = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(in);

        byte[] buffer = new byte[3];
        int result = stream.read(buffer, 0, 3);

        assertEquals(3, result);
        assertArrayEquals(new byte[]{1, 2, 3}, buffer);
    }

    @Test
    public void testRead_EndOfFile() throws IOException {
        byte[] input = new byte[]{1, 2, 3};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(in);

        stream.read();
        stream.read();
        stream.read();

        int result = stream.read(); // Should reach end of file

        assertEquals(-1, result);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] input = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(in);

        long bytesReadBeforeSkip = stream.getStreamPosition();
        long bytesSkipped = stream.skip(2);
        long bytesReadAfterSkip = stream.getStreamPosition();

        assertEquals(2, bytesSkipped);
        assertEquals(bytesReadBeforeSkip + bytesSkipped, bytesReadAfterSkip);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] input = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(in);

        int available = stream.available();

        assertEquals(5, available);
    }

    @Test
    public void testClose() throws IOException {
        byte[] input = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(in);

        stream.close();

        // Assert that no exception is thrown when trying to read after closing
        int result = stream.read();

        assertEquals(-1, result);
    }

    @Test
    public void testMatchingSignature() {
        byte[] signature = new byte[]{120, -100, 11, -47};
        int length = 4;
        boolean result = DeflateCompressorInputStream.checkSignature(signature, length);

        assertTrue(result);
    }

    @Test
    public void testNonMatchingSignature() {
        byte[] signature = new byte[]{1, 2, 3, 4};
        int length = 4;
        boolean result = DeflateCompressorInputStream.checkSignature(signature, length);

        assertFalse(result);
    }
}