import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class BitInputStreamTest {
    
    @Test
    public void testReadBits() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0b10101010, 0b01010101});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        // Test reading 8 bits
        long result = bitInputStream.readBits(8);
        assertEquals(170, result);
        
        // Test reading 6 bits
        result = bitInputStream.readBits(6);
        assertEquals(42, result);
        
        // Test reading 10 bits
        result = bitInputStream.readBits(10);
        assertEquals(341, result);
        
        // Test reading 7 bits
        result = bitInputStream.readBits(7);
        assertEquals(85, result);
        
        // Test reading 4 bits
        result = bitInputStream.readBits(4);
        assertEquals(5, result);
        
        // Test reading 5 bits
        result = bitInputStream.readBits(5);
        assertEquals(10, result);
        
        // Test reading 3 bits
        result = bitInputStream.readBits(3);
        assertEquals(5, result);
        
        // Test reaching end of stream
        result = bitInputStream.readBits(1);
        assertEquals(-1, result);
        
        bitInputStream.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        
        bitInputStream.readBits(-1);
        
        bitInputStream.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCountGreaterThanMax() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        
        bitInputStream.readBits(64);
        
        bitInputStream.close();
    }
    
    @Test
    public void testClearBitCache() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0b10101010});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        // Fill the cache
        bitInputStream.readBits(4);
        
        // Clear the cache
        bitInputStream.clearBitCache();
        
        long result = bitInputStream.readBits(8);
        assertEquals(170, result);
        
        bitInputStream.close();
    }
}