import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;
    
    @Before
    public void setup() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04});
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @Test
    public void testReadBits() throws IOException {
        long result = bitInputStream.readBits(3);
        assertEquals(0x01, result);
        
        result = bitInputStream.readBits(5);
        assertEquals(0x02, result);
        
        result = bitInputStream.readBits(6);
        assertEquals(0x03, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsExceedsMaximumCount() throws IOException {
        bitInputStream.readBits(64);
    }
    
    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.readBits(3);
        bitInputStream.clearBitCache();
        long result = bitInputStream.readBits(3);
        assertEquals(0x01, result);
    }
    
    @Test
    public void testByteOrder() throws IOException {
        long result = bitInputStream.readBits(8);
        assertEquals(0x01, result);
        
        bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04}), ByteOrder.LITTLE_ENDIAN);
        result = bitInputStream.readBits(8);
        assertNotEquals(0x01, result);
    }
    
    @Test
    public void testReadBitsEndOfStream() throws IOException {       
        bitInputStream = new BitInputStream(new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        }, ByteOrder.BIG_ENDIAN);
        
        long result = bitInputStream.readBits(8);
        assertEquals(-1, result);
    }
    
    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        bitInputStream.close();
        bitInputStream.readBits(3);
    }
}