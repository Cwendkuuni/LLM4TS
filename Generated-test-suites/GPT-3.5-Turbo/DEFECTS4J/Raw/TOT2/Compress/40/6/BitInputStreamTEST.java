import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.utils.BitInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testClearBitCache() {
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[] {}), ByteOrder.BIG_ENDIAN);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(1));
    }

    @Test
    public void testReadBits() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[] {0x12, 0x34}), ByteOrder.BIG_ENDIAN);
        assertEquals(0x12, bitInputStream.readBits(8));
        assertEquals(0x34, bitInputStream.readBits(8));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[] {}), ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[] {0x12}), ByteOrder.BIG_ENDIAN);
        assertEquals(0x12, bitInputStream.readBits(8));
        assertEquals(-1, bitInputStream.readBits(8));
    }

    @Test
    public void testReadBitsLittleEndian() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[] {0x12}), ByteOrder.LITTLE_ENDIAN);
        assertEquals(0x12, bitInputStream.readBits(8));
    }

    @Test
    public void testClose() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[] {}), ByteOrder.BIG_ENDIAN);
        bitInputStream.close();
    }
}