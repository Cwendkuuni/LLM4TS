import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.compress.utils.BitInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import static org.junit.Assert.*;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        // Set up the input stream for testing
        byte[] inputBytes = {0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes);
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }

    @Test
    public void testReadBits_PositiveCount_ExpectBitsReturned() throws IOException {
        // Test reading 32 bits
        long result = bitInputStream.readBits(32);
        assertEquals(0x0A0B0C0DL, result);

        // Test reading 12 bits
        result = bitInputStream.readBits(12);
        assertEquals(0x0EL, result);

        // Test reading 8 bits
        result = bitInputStream.readBits(8);
        assertEquals(0x0F, result);
    }

    @Test
    public void testReadBits_NegativeCount_ExpectIllegalArgumentException() {
        // Test reading -1 bit
        try {
            bitInputStream.readBits(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        }
    }

    @Test
    public void testReadBits_CountExceedsMaximumCacheSize_ExpectIllegalArgumentException() {
        // Test reading 64 bits
        try {
            bitInputStream.readBits(64);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        }
    }

    @Test
    public void testReadBits_EndOfStreamReached_ExpectMinusOne() throws IOException {
        // Consume the bits in the stream
        bitInputStream.readBits(32);
        bitInputStream.readBits(12);
        bitInputStream.readBits(8);

        // Attempt to read more bits
        long result = bitInputStream.readBits(1);
        assertEquals(-1L, result);
    }

    @Test
    public void testClearBitCache_ExpectCacheCleared() throws IOException {
        // Read some bits to fill the cache
        bitInputStream.readBits(24);

        // Clear the cache
        bitInputStream.clearBitCache();

        // Try to read bits from the cache
        long result = bitInputStream.readBits(8);
        assertEquals(0L, result);
    }
}