import org.junit.Test;
import static org.junit.Assert.*;

public class BitInputStreamTest {

    @Test
    public void testReadBits() {
        // Setup
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0b11110000});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Test
        try {
            long result = bitInputStream.readBits(4);

            // Verify
            assertEquals(0b1111, result);

        } catch(IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadBitsNegativeCount() {
        // Setup
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0b11110000});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // Test
        try {
            bitInputStream.readBits(-4);
            
            // Verify
            fail("IllegalArgumentException not thrown");

        } catch(IllegalArgumentException e) {
            // Success
        } catch(IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadBitsOutOfRange() {
        // Setup
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0b11110000});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // Test
        try {
            long result = bitInputStream.readBits(64);
            
            // Verify
            assertEquals(-1, result);

        } catch(IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testClearBitCache() {
        // Setup
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0b11110000});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Test
        bitInputStream.clearBitCache();

        // Verify
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }
}