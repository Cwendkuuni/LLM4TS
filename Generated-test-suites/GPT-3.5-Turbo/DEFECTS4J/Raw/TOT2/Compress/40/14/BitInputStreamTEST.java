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
        BitInputStream bitInputStream = new BitInputStream(null, ByteOrder.BIG_ENDIAN);
        
        try {
            bitInputStream.readBits(-1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            long result = bitInputStream.readBits(64);
            assertEquals(-1, result);
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

}