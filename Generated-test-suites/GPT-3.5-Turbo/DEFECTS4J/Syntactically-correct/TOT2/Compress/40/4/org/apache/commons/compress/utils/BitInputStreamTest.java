import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testReadBits() throws IOException {
        byte[] data = {0x12, 0x34, 0x56, 0x78};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(bais, ByteOrder.BIG_ENDIAN);

        long result = bitInputStream.readBits(24);
        assertEquals(0x123456L, result);
    }

    @Test
    public void testReadBitsWithDifferentByteOrder() throws IOException {
        byte[] data = {0x78, 0x56, 0x34, 0x12};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(bais, ByteOrder.LITTLE_ENDIAN);

        long result = bitInputStream.readBits(24);
        assertEquals(0x123456L, result);
    }

    @Test
    public void testReadBitsCountGreaterThanMaximumCacheSize() throws IOException {
        byte[] data = {0x12};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(bais, ByteOrder.BIG_ENDIAN);

        try {
            bitInputStream.readBits(64);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        byte[] data = {0x12};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(bais, ByteOrder.BIG_ENDIAN);

        long result = bitInputStream.readBits(32);
        assertEquals(-1L, result);
    }

    @Test
    public void testClearBitCache() throws IOException {
        byte[] data = {0x12, 0x34, 0x56};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(bais, ByteOrder.BIG_ENDIAN);
        long result = bitInputStream.readBits(16);

        bitInputStream.clearBitCache();
        result = bitInputStream.readBits(16);

        assertEquals(0x3456L, result);
    }
}