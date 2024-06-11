import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testConstructor() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x4A, 0x63});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        assertNotNull(bitInputStream);
    }

    @Test
    public void testClearBitCache() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x4A, 0x63});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBits() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x4A, 0x63});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        long bits = bitInputStream.readBits(16);
        assertEquals(0b1001011001100011, bits);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x4A});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(-1);
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x4A});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        long bits = bitInputStream.readBits(16);
        assertEquals(-1, bits);
    }
}