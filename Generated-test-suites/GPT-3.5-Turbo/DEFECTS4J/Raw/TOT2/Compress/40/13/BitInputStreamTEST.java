import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import org.junit.Test;

public class BitInputStreamTest {

    // Test for clearBitCache method
    @Test
    public void testClearBitCache() {
        BitInputStream bis = new BitInputStream(new ByteArrayInputStream(new byte[] {}), ByteOrder.BIG_ENDIAN);
        bis.readBits(8); // Read 8 bits to populate the cache
        bis.clearBitCache();
        assertEquals(0, bis.readBits(1)); // After clearing, cache should be empty
    }

    // Test for readBits method (typical use case)
    @Test
    public void testReadBitsTypical() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(0xAA); // 10101010
        BitInputStream bis = new BitInputStream(new ByteArrayInputStream(baos.toByteArray()), ByteOrder.BIG_ENDIAN);
        assertEquals(0b101010, bis.readBits(6)); // Read first 6 bits
        assertEquals(0b10, bis.readBits(2)); // Read remaining 2 bits
    }

    // Test for readBits method (edge case - count equals 0)
    @Test
    public void testReadBitsEdgeZero() throws IOException {
        BitInputStream bis = new BitInputStream(new ByteArrayInputStream(new byte[] {}), ByteOrder.BIG_ENDIAN);
        assertEquals(0, bis.readBits(0)); // 0 bits requested should return 0
    }

    // Test for readBits method (edge case - count equals MAXIMUM_CACHE_SIZE)
    @Test
    public void testReadBitsEdgeMaxCacheSize() throws IOException {
        byte[] data = {0x3F}; // Bits 00111111
        BitInputStream bis = new BitInputStream(new ByteArrayInputStream(data), ByteOrder.BIG_ENDIAN);
        assertEquals(0x3F, bis.readBits(6)); // All 6 bits should be read
    }

    // Test for readBits method (exception handling - negative count)
    @Test
    public void testReadBitsExceptionNegativeCount() {
        BitInputStream bis = new BitInputStream(new ByteArrayInputStream(new byte[] {}), ByteOrder.BIG_ENDIAN);
        try {
            bis.readBits(-1); // Negative count should throw an exception
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }

    // Test for readBits method (exception handling - count greater than MAXIMUM_CACHE_SIZE)
    @Test
    public void testReadBitsExceptionMaxCount() {
        BitInputStream bis = new BitInputStream(new ByteArrayInputStream(new byte[] {}), ByteOrder.BIG_ENDIAN);
        try {
            bis.readBits(64); // Count greater than maximum cache size should throw an exception
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }

    // Test for readBits method (error scenario - end of stream reached before count bits read)
    @Test
    public void testReadBitsErrorEndOfStream() throws IOException {
        BitInputStream bis = new BitInputStream(new ByteArrayInputStream(new byte[] {0b11110000}), ByteOrder.BIG_ENDIAN);
        assertEquals(-1, bis.readBits(8)); // Requesting 8 bits when only 4 are available should return -1
    }

}