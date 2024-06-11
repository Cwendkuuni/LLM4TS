import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setup() {
        byte[] data = {0x12, 0x34, 0x56};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testReadBits() throws IOException {
        // Test with count < 8
        assertEquals(0x12, bitInputStream.readBits(4));
        assertEquals(0x34, bitInputStream.readBits(8));
        
        // Test with count > 8
        assertEquals(0x563, bitInputStream.readBits(12));
        assertEquals(0, bitInputStream.readBits(20));
        
        // Test with count = 0
        assertEquals(-1, bitInputStream.readBits(0));
        
        // Test with count > MAXIMUM_CACHE_SIZE
        try {
            bitInputStream.readBits(64);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        }
    }

    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(8));
    }
    
}