import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.utils.BitInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        byte[] data = {0x12, 0x34, 0x56, 0x78};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsLittleEndian() throws IOException {
        assertEquals(0x12345678, bitInputStream.readBits(32));
    }

    @Test
    public void testReadBitsBigEndian() throws IOException {
        bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[] {0x78, 0x56, 0x34, 0x12}), ByteOrder.LITTLE_ENDIAN);
        assertEquals(0x12345678, bitInputStream.readBits(32));
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        assertEquals(-1, bitInputStream.readBits(64));
    }

    @Test
    public void testReadBitsEdgeCase() throws IOException {
        assertEquals(0x12, bitInputStream.readBits(8));
        assertEquals(0x34, bitInputStream.readBits(8));
        assertEquals(0x56, bitInputStream.readBits(8));
        assertEquals(0x78, bitInputStream.readBits(8));
    }

}