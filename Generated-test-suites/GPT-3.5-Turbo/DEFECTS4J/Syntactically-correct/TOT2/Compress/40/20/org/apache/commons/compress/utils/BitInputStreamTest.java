import static org.junit.Assert.*;
import org.junit.Test;

public class BitInputStreamTest {
    
    @Test
    public void testClearBitCache() {
        BitInputStream bitInputStream = new BitInputStream(null, ByteOrder.BIG_ENDIAN);
        bitInputStream.clearBitCache();
        
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }
    
    @Test
    public void testReadBits() {
        BitInputStream bitInputStream = new BitInputStream(null, ByteOrder.LITTLE_ENDIAN);
        
        // Testing typical use case
        long result = bitInputStream.readBits(8);
        assertEquals(0, result); // If in is null this should return 0
        
        // Testing edge cases
        try {
            bitInputStream.readBits(-1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
           // Expected exception
        }
        
        try {
            bitInputStream.readBits(64);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Testing error scenario
        try {
            bitInputStream.readBits(8);
            fail("IOException expected");
        } catch (IOException e) {
            // Expected exception
        }
    }
}