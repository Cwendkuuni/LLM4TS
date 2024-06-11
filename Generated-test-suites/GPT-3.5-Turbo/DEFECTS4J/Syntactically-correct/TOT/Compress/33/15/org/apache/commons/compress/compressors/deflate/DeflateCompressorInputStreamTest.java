import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.*;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {120, -100, 45, -45, 96, 99, -50};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(input);
        
        assertEquals(120, deflateInputStream.read());
        assertEquals(-100, deflateInputStream.read());
        assertEquals(45, deflateInputStream.read());
        assertEquals(-45, deflateInputStream.read());
        assertEquals(96, deflateInputStream.read());
        assertEquals(99, deflateInputStream.read());
        assertEquals(-50, deflateInputStream.read());
        assertEquals(-1, deflateInputStream.read());
        
        deflateInputStream.close();
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] data = {120, -100, 45, -45, 96, 99, -50};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(input);

        byte[] buffer = new byte[4];
        int bytesRead = deflateInputStream.read(buffer, 1, 3);
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0, 120, -100, 45}, buffer);

        bytesRead = deflateInputStream.read(buffer, 0, 3);
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{-45, 96, 99, 0}, buffer);

        deflateInputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {120, -100, 45, -45, 96, 99, -50};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(input);

        long skipCount = deflateInputStream.skip(2);
        assertEquals(2, skipCount);
        assertEquals(45, deflateInputStream.read());

        deflateInputStream.close();
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = {120, -100, 45, -45, 96, 99, -50};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(input);

        int availableBytes = deflateInputStream.available();
        assertEquals(data.length, availableBytes);

        deflateInputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = {120, -100, 45, -45, 96, 99, -50};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(input);

        deflateInputStream.close();

        // Attempting to read after closing should throw an exception
        try {
            deflateInputStream.read();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testCheckSignature() {
        byte[] invalidSignature = {103, 101, 116, 83, 105, 103, 110, 97, 116, 117, 114, 101};
        byte[] validSignature = {-120, 1};
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(null);

        assertFalse(deflateInputStream.checkSignature(invalidSignature, invalidSignature.length));
        assertTrue(deflateInputStream.checkSignature(validSignature, validSignature.length));
    }
}