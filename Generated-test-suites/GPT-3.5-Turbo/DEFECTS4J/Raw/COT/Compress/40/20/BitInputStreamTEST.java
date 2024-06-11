import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import static org.junit.Assert.*;

public class BitInputStreamTest {

    private static final byte[] TEST_DATA = {0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef};
    private static final int TEST_DATA_SIZE = TEST_DATA.length;
    private static final int BITS_READ = 5;

    @Test
    public void testClearBitCache() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        BitInputStream bis = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        // Add data to cache
        try {
            bis.readBits(BITS_READ);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        
        // Clear cache
        bis.clearBitCache();
        
        // Verify cache is cleared
        assertEquals(0, bis.bitsCached);
        assertEquals(0, bis.bitsCachedSize);
    }
    
    @Test
    public void testReadBits() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        BitInputStream bis = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        try {
            // Read 5 bits
            long bits = bis.readBits(BITS_READ);
            
            // Verify result
            assertEquals(0x01, bits);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        BitInputStream bis = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        bis.readBits(-1); // should throw exception
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThan63() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        BitInputStream bis = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        bis.readBits(64); // should throw exception
    }
    
    @Test
    public void testReadBitsWithEndOfStream() {
        // Create ByteArrayInputStream with no data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bis = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        try {
            // Read 5 bits
            long bits = bis.readBits(BITS_READ);
            
            // Verify result is -1
            assertEquals(-1, bits);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
}