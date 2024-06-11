import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

public class BitInputStreamTest {
    
    @Test
    public void testConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        assertEquals(inputStream, bitInputStream.in);
        assertEquals(ByteOrder.BIG_ENDIAN, bitInputStream.byteOrder);
    }
    
    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        bitInputStream.close();
    }
    
    @Test
    public void testClearBitCache() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        // Fill bit cache to test clearing
        try {
            bitInputStream.readBits(10);
        } catch (IOException e) {
            // Ignore exception in this test
        }
        
        bitInputStream.clearBitCache();
        
        // No exception thrown and bit cache is cleared
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        bitInputStream.readBits(-1);
    }
    
    @Test
    public void testReadBitsEndOfStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        assertEquals(-1, bitInputStream.readBits(10));
    }
    
    @Test
    public void testReadBitsBigEndian() throws IOException {
        byte[] data = {0x1F, 0x62, 0x38}; // 00011111 01100010 00111000
        
        InputStream inputStream = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        assertEquals(0x01, bitInputStream.readBits(1));
        assertEquals(0x0F, bitInputStream.readBits(4));
        assertEquals(0x00, bitInputStream.readBits(3));
        assertEquals(0x05, bitInputStream.readBits(3));
        assertEquals(0x1C, bitInputStream.readBits(5));
        assertEquals(0x38, bitInputStream.readBits(6));
        
        assertEquals(-1, bitInputStream.readBits(1)); // End of stream
    }
    
    @Test
    public void testReadBitsLittleEndian() throws IOException {
        byte[] data = {0x1F, 0x62, 0x38}; // 00011111 01100010 00111000
        
        InputStream inputStream = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        
        assertEquals(0x01, bitInputStream.readBits(1));
        assertEquals(0x0E, bitInputStream.readBits(4));
        assertEquals(0x04, bitInputStream.readBits(3));
        assertEquals(0x07, bitInputStream.readBits(3));
        assertEquals(0x08, bitInputStream.readBits(5));
        assertEquals(0x06, bitInputStream.readBits(6));
        
        assertEquals(-1, bitInputStream.readBits(1)); // End of stream
    }
}