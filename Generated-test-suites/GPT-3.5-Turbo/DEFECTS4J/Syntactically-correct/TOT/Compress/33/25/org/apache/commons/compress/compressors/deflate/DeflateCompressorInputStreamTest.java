import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {0x78, 0x9c, 0x00, 0x00, 0x00, 0x01, 0x05, 0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        int result = deflateInputStream.read();
        
        assertEquals(120, result); // Asserting that the read value is correct
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] data = {0x78, 0x9c, 0x2b, 0x4a, 0xcd, 0x49, 0x55, 0xc8, 0xcf, 0x51, 0xe4, 0xe0};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[10];

        int result = deflateInputStream.read(buffer, 0, buffer.length);

        assertEquals(10, result); // Asserting that the correct number of bytes is read
        assertArrayEquals(new byte[]{43, 74, -51, 73, 85, -56, -49, 81, -28, -32}, buffer); // Asserting that the correct bytes are read into the buffer
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {0x78, 0x9c, 0x2b, 0x4a, 0xcd, 0x49, 0x55, 0xc8, 0xcf, 0x51, 0xe4, 0xe0};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        long result = deflateInputStream.skip(5);

        assertEquals(5L, result); // Asserting that the correct number of bytes is skipped
        assertEquals(7, deflateInputStream.available()); // Asserting that the correct number of bytes are available after skip
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = {0x78, 0x9c, 0x2b, 0x4a, 0xcd, 0x49, 0x55, 0xc8, 0xcf, 0x51, 0xe4, 0xe0};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        int result = deflateInputStream.available();

        assertEquals(12, result); // Asserting that the correct number of bytes are available
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = {0x78, 0x9c, 0x2b, 0x4a, 0xcd, 0x49, 0x55, 0xc8, 0xcf, 0x51, 0xe4, 0xe0};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        deflateInputStream.close();

        try {
            deflateInputStream.read();
            fail("Expected IOException"); // Asserting that an exception is thrown after closing the stream
        } catch (IOException e) {
            // Exception is expected
        }
    }

    @Test
    public void testCheckSignature() {
        byte[] signature = {0x78, 0x9c};
        int length = 2;

        boolean result = DeflateCompressorInputStream.checkSignature(signature, length);

        assertTrue(result); // Asserting that the signature is correctly recognized
    }

    @Test
    public void testCheckSignatureWithWrongSignature() {
        byte[] signature = {0x12, 0x34};
        int length = 2;

        boolean result = DeflateCompressorInputStream.checkSignature(signature, length);

        assertFalse(result); // Asserting that the wrong signature is not recognized
    }
}