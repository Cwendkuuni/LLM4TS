import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        byte[] data = {0, 1, 2, 3, 4};
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        assertEquals(0, stream.read());
        assertEquals(1, stream.read());
        stream.close();
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = {0, 1, 2, 3, 4};
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        byte[] buffer = new byte[3];
        stream.read(buffer, 0, 3);
        assertArrayEquals(new byte[]{0, 1, 2}, buffer);
        stream.close();
    }

    @Test
    public void testReadWithEmptyStream() throws IOException {
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{}));
        assertEquals(-1, stream.read());
        stream.close();
    }

    @Test
    public void testSkipBytes() throws IOException {
        byte[] data = {0, 1, 2, 3, 4};
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        stream.skip(2);
        assertEquals(2, stream.read());
        stream.close();
    }

    @Test
    public void testAvailableBytes() throws IOException {
        byte[] data = {0, 1, 2, 3, 4};
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        assertEquals(5, stream.available());
        stream.skip(2);
        assertEquals(3, stream.available());
        stream.close();
    }

    @Test
    public void testCloseStream() throws IOException {
        byte[] data = {0, 1, 2, 3, 4};
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        stream.close();
        assertFalse(stream.available() > 0);
    }
}