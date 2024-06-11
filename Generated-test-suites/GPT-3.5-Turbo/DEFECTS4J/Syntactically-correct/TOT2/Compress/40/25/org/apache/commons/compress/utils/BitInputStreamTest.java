import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteOrder;


public class BitInputStreamTest {
    
    private BitInputStream bitInputStream;
    
    @Before
    public void setUp() {
        byte[] data = {0b01101000, 0b01101111}; // 'h' and 'o'
        InputStream inputStream = new ByteArrayInputStream(data);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }
    
    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.readBits(5); // Read 5 bits to cache
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(1));
    }
    
    @Test
    public void testReadBits() throws IOException {
        assertEquals(0b0110L, bitInputStream.readBits(4));
        assertEquals(0b1000L, bitInputStream.readBits(4));
        assertEquals(0b011L, bitInputStream.readBits(3));
        assertEquals(0b0110L, bitInputStream.readBits(4)); // should start a new byte
        assertEquals(0b1111L, bitInputStream.readBits(4)); // end of stream for last byte
        assertEquals(-1, bitInputStream.readBits(1)); // end of stream
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        bitInputStream.readBits(-1); // negative count should throw exception
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsOverflowCount() throws IOException {
        bitInputStream.readBits(64); // count exceeding maximum should throw exception
    }
    
    // Add more test cases for edge cases and exceptions
    
}