
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

    @Before
    public void setUp() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0b01100001, 0b00110001});
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }

    @Test
    public void testReadBits_PositiveCount_ReturnsCorrectBits() throws IOException {
        long bits = bitInputStream.readBits(4); // Read 4 bits
        assertEquals(0b0001, bits);
    }

    @Test
    public void testReadBits_NegativeCount_ThrowsIllegalArgumentException() throws IOException {
        try {
            bitInputStream.readBits(-1);
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        }
    }

    @Test
    public void testReadBits_CountGreaterThanMaximumCacheSize_ThrowsIllegalArgumentException() throws IOException {
        try {
            bitInputStream.readBits(64);
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        }
    }

    @Test
    public void testReadBits_EndOfStreamReached_ReturnsNegativeOne() throws IOException {
        long bits = bitInputStream.readBits(64); // Attempt to read more bits than available in the stream
        assertEquals(-1, bits);
    }

    @Test
    public void testClearBitCache_BitCacheCleared() {
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(4));
    }

}
