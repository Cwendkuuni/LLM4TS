
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.utils.BitInputStream;

public class BitInputStreamTest {
    private BitInputStream bitInputStream;
    
    @Before
    public void setUp() throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[]{0x01, 0x23, 0x45, 0x67, 0x89, 0xab, 0xcd, 0xef});
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);
    }
    
    @After
    public void tearDown() throws Exception {
        bitInputStream.close();
    }
    
    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
        // TODO: Test if the underlying InputStream is closed
    }
    
    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        // TODO: Test if the bit cache is cleared
    }
    
    @Test
    public void testReadBits() throws IOException {
        // Read 5 bits
        long bits = bitInputStream.readBits(5);
        assertEquals(0x00, bits);
        
        // Read 16 bits
        bits = bitInputStream.readBits(16);
        assertEquals(0x0123, bits);
        
        // Read 24 bits
        bits = bitInputStream.readBits(24);
        assertEquals(0x456789, bits);
        
        // Read 32 bits
        bits = bitInputStream.readBits(32);
        assertEquals(0xabcdef01, bits);
        
        // Read 63 bits
        bits = bitInputStream.readBits(63);
        assertEquals(0xefcdab8967452301L, bits);
        
        // Read 1 more bit, should return -1 to indicate end of stream
        bits = bitInputStream.readBits(1);
        assertEquals(-1, bits);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThanMaximum() throws IOException {
        bitInputStream.readBits(64);
    }
}
