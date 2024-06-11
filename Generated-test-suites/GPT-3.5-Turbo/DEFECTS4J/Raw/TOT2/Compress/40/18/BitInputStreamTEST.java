import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        // Set up a ByteOrder for testing
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;

        // Create a sample byte array to use as input stream
        byte[] inputBytes = {0b10101010, 0b01010101};

        // Create a ByteArrayInputStream from the sample byte array
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);

        // Create a BitInputStream object
        bitInputStream = new BitInputStream(inputStream, byteOrder);
    }

    @Test
    public void testReadBits() throws IOException {
        // Test reading 8 bits
        assertEquals(0b10101010, bitInputStream.readBits(8));

        // Test reading 4 bits
        assertEquals(0b1010, bitInputStream.readBits(4));

        // Test reading more than the maximum cache size (63)
        try {
            bitInputStream.readBits(64);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception thrown
        }

        // Test reading bits when the end of the stream has been reached
        assertEquals(-1, bitInputStream.readBits(8));
    }

    @Test
    public void testClearBitCache() throws IOException {
        // Read some bits to cache
        bitInputStream.readBits(4);

        // Clear the bit cache
        bitInputStream.clearBitCache();

        // Ensure the cache has been cleared
        assertEquals(0, bitInputStream.readBits(4));
    }

    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
        assertTrue(bitInputStream.in.read() == -1);
    }
}