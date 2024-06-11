import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import static org.junit.Assert.*;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        byte[] bytes = new byte[]{0x12, 0x34, 0x56, 0x78}; // Some sample bytes for testing
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithInvalidCount() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsWithZeroCount() throws IOException {
        long result = bitInputStream.readBits(0);
        assertEquals(0, result);
    }

    @Test
    public void testReadBitsWithValidCount() throws IOException {
        long result = bitInputStream.readBits(16);
        assertEquals(0x1234, result);
    }

    @Test
    public void testReadBitsWithExceedingBits() throws IOException {
        long result = bitInputStream.readBits(32);
        assertEquals(0x12345678, result);
    }

    @Test(expected = IOException.class)
    public void testReadBitsAtEndOfStream() throws IOException {
        bitInputStream.readBits(64);
        bitInputStream.readBits(1);
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.readBits(16);
        bitInputStream.clearBitCache();
        long result = bitInputStream.readBits(8);
        assertEquals(0x56, result);
    }

    @Test
    public void testReadBitsWithLittleEndianOrder() throws IOException {
        bitInputStream = new BitInputStream(bitInputStream.in, ByteOrder.LITTLE_ENDIAN);
        long result = bitInputStream.readBits(16);
        assertEquals(0x3412, result);
    }

    @Test
    public void testReadBitsWithLittleEndianOrderAndExceedingBits() throws IOException {
        bitInputStream = new BitInputStream(bitInputStream.in, ByteOrder.LITTLE_ENDIAN);
        long result = bitInputStream.readBits(32);
        assertEquals(0x78563412, result);
    }

    @Test
    public void testReadBitsWithLittleEndianOrderAndOddBitCount() throws IOException {
        bitInputStream = new BitInputStream(bitInputStream.in, ByteOrder.LITTLE_ENDIAN);
        long result = bitInputStream.readBits(9);
        assertEquals(0x133, result);
    }

}