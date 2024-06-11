import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {
    private ByteArrayInputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        byte[] data = {0b01010101, 0b10101010, 0b11110000};
        inputStream = new ByteArrayInputStream(data);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }

    @Test
    public void testReadBits_withPositiveCount_ReturnsBits() throws IOException {
        long result = bitInputStream.readBits(8);
        assertEquals(85, result);
        result = bitInputStream.readBits(12);
        assertEquals(2730, result);
        result = bitInputStream.readBits(4);
        assertEquals(15, result);
    }

    @Test
    public void testReadBits_withNegativeCount_ThrowsIllegalArgumentException() throws IOException {
        try {
            bitInputStream.readBits(-1);
            fail("Expected IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testReadBits_withCountExceedingMaximumCacheSize_ThrowsIllegalArgumentException() throws IOException {
        try {
            bitInputStream.readBits(64);
            fail("Expected IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testReadBits_EndOfStream_ReturnsMinusOne() throws IOException {
        long result = bitInputStream.readBits(64); // Reading more bits than available
        assertEquals(-1, result);
    }

    @Test
    public void testClearBitCache_ResetsCachedBits() throws IOException {
        bitInputStream.readBits(4);
        bitInputStream.clearBitCache();
        long result = bitInputStream.readBits(8);
        assertEquals(85, result);
    }
}