import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testClearBitCache() {
        byte[] data = {1, 2, 3 ,4};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        bitInputStream.readBits(8); // Read 1 byte
        bitInputStream.clearBitCache();

        assertEquals(0, bitInputStream.bitsCachedSize);
        assertEquals(0, bitInputStream.bitsCached);
    }

    @Test
    public void testReadBits() throws IOException {
        byte[] data = {1, 2, 3, 4};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        long bits = bitInputStream.readBits(16);

        assertEquals(513, bits);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws IOException {
        byte[] data = {1, 2, 3, 4};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        byte[] data = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        long bits1 = bitInputStream.readBits(24);
        long bits2 = bitInputStream.readBits(8);

        assertEquals(66051, bits1);
        assertEquals(-1, bits2);
    }
}