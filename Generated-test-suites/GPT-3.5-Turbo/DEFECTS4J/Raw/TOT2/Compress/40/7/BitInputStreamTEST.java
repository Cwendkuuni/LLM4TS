import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.utils.BitInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[]{0x64, 0x65, 0x66});
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);
        
        assertNotNull(bitInputStream);
        assertEquals(bitInputStream.byteOrder, ByteOrder.BIG_ENDIAN);
    }
    
    @Test
    public void testClose() {
        InputStream input = new ByteArrayInputStream(new byte[]{0x64});
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);
        
        try {
            bitInputStream.close();
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testClearBitCache() {
        InputStream input = new ByteArrayInputStream(new byte[]{0x64});
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);
        
        bitInputStream.clearBitCache();
        
        assertEquals(bitInputStream.bitsCached, 0);
        assertEquals(bitInputStream.bitsCachedSize, 0);
    }
    
    @Test
    public void testReadBits() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);
        
        long result = bitInputStream.readBits(20);
        
        assertEquals(result, 66051);
    }
    
    @Test
    public void testReadBitsInvalidCount() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);
        
        try {
            long result = bitInputStream.readBits(64);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}