
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import static org.junit.Assert.assertEquals;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;
    private InputStream inputStream;

    private final byte[] data = {0b00110011, 0b01010101, 0b11111111, 0b00000000};
    private final int dataSize = 4;

    @Before
    public void setup() {
        inputStream = new ByteArrayInputStream(data);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void teardown() throws IOException {
        bitInputStream.close();
    }

    @Test
    public void testReadBits_PositiveCount() throws IOException {
        long bits = bitInputStream.readBits(16);
        assertEquals(0b0011001101010101L, bits);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_NegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_CountGreaterThanMaximumCacheSize() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBits_CountGreaterThanAvailableBits() throws IOException {
        bitInputStream.clearBitCache();
        long bits = bitInputStream.readBits(65);
        assertEquals(-1, bits);
    }

    @Test
    public void testReadBits_WithLittleEndianByteOrder() throws IOException {
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long bits = bitInputStream.readBits(16);
        assertEquals(0b0101010100110011L, bits);
    }

    @Test
    public void testReadBits_ClearedCache() throws IOException {
        bitInputStream.clearBitCache();
        long bits = bitInputStream.readBits(8);
        assertEquals(0b00110011L, bits);
    }

}
