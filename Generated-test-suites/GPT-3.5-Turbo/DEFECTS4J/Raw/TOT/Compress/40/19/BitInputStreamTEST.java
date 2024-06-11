import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import static org.junit.Assert.assertEquals;

public class BitInputStreamTest {
    private BitInputStream bitInputStream;

    @Before
    public void setup() {
        byte[] byteArray = new byte[]{0b10101010, 0b01010101}; // This is a sample byte array
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @Test
    public void testReadBits_WithPositiveCount_ShouldReturnBitsUsingByteOrder() throws IOException {
        long expectedBits = 0b1010101001010101000000000000000000000000000000000000000000000000L;
        int count = 48; // Choosing a count that is less than 63

        long actualBits = bitInputStream.readBits(count);

        assertEquals(expectedBits, actualBits);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_WithNegativeCount_ShouldThrowException() throws IOException {
        int count = -1;

        bitInputStream.readBits(count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_WithCountGreaterThanMaximumCacheSize_ShouldThrowException() throws IOException {
        int count = 64; // Choosing a count that is greater than 63

        bitInputStream.readBits(count);
    }

    @Test
    public void testReadBits_WithEndOfStreamBeforeRequestedCount_ShouldReturnMinusOne() throws IOException {
        long expectedBits = -1;
        int count = 64; // Choosing a count that is greater than the number of bits in the stream

        long actualBits = bitInputStream.readBits(count);

        assertEquals(expectedBits, actualBits);
    }

    @Test
    public void testClearBitCache_ShouldClearCachedBits() throws IOException {
        int count = 32;
        bitInputStream.readBits(count);

        bitInputStream.clearBitCache();

        long actualBits = bitInputStream.readBits(count);
        assertEquals(0, actualBits);
    }

}