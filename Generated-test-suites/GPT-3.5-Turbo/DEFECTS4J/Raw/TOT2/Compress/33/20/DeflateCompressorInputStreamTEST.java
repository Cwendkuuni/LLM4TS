import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        assertEquals(1, deflateInputStream.read());
        assertEquals(2, deflateInputStream.read());
    }

    @Test
    public void testReadWithByteArray() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[3];
        int bytesRead = deflateInputStream.read(buffer, 0, 3);

        assertArrayEquals(new byte[]{1, 2, 3}, buffer);
        assertEquals(3, bytesRead);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        long bytesSkipped = deflateInputStream.skip(2);

        assertEquals(2, bytesSkipped);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        int availableBytes = deflateInputStream.available();

        assertEquals(5, availableBytes);
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        deflateInputStream.close();

        assertEquals(-1, deflateInputStream.read());
    }

    @Test
    public void testCheckSignature() {
        byte[] signature = {0x1f, (byte) 0x8b};
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(null);
        
        assertTrue(deflateInputStream.checkSignature(signature, 2));
    }
}