import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testClearBitCache() {
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[]{0x01}), ByteOrder.BIG_ENDIAN);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(5));
    }

    @Test
    public void testReadBits() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[]{0x0F}), ByteOrder.LITTLE_ENDIAN);
        assertEquals(15, bitInputStream.readBits(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[]{0x01}), ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[]{}), ByteOrder.LITTLE_ENDIAN);
        assertEquals(-1, bitInputStream.readBits(1));
    }

}