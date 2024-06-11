import static org.junit.Assert.*;
import org.junit.Test;

public class BitInputStreamTest {

    @Test
    public void testClearBitCache() {
        // Setup
        // Initialize BitInputStream object
        BitInputStream bitInputStream = new BitInputStream(null, ByteOrder.BIG_ENDIAN);

        // Execute
        bitInputStream.clearBitCache();

        // Verify
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBits() {
        // Setup
        // Initialize BitInputStream object
        BitInputStream bitInputStream = new BitInputStream(null, ByteOrder.BIG_ENDIAN);

        // Execute & Verify
        // Test reading 0 bits
        assertEquals(0, bitInputStream.readBits(0));

        // Test reading 8 bits
        assertEquals(255, bitInputStream.readBits(8));

        // Test reading 63 bits
        assertEquals(9223372036854775807L, bitInputStream.readBits(63));

        // Test reading more than 63 bits - Exception should be thrown
        try {
            bitInputStream.readBits(64);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Success
        }

    }

    @Test
    public void testClose() {
        // Setup
        // Initialize BitInputStream object
        BitInputStream bitInputStream = new BitInputStream(null, ByteOrder.BIG_ENDIAN);

        // Execute
        // No exception should be thrown
        try {
            bitInputStream.close();
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }

}