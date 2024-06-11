import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BitInputStreamTest {
    
    @Test
    public void testReadBits() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {(byte)0b01101001, (byte)0b01010011});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        assertEquals(0b0110100101010011, bitInputStream.readBits(16));
        assertEquals(0b01101001, bitInputStream.readBits(8));
        assertEquals(0b0101, bitInputStream.readBits(4));
        assertEquals(0b0011, bitInputStream.readBits(4));
        assertEquals(-1, bitInputStream.readBits(1));
        
        bitInputStream.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {(byte)0b01101001, (byte)0b01010011});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        bitInputStream.readBits(-1);
        
        bitInputStream.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithTooLargeCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {(byte)0b01101001, (byte)0b01010011});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        bitInputStream.readBits(64);
        
        bitInputStream.close();
    }
    
    @Test(expected = IOException.class)
    public void testReadBitsWithIOException() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read()).thenThrow(new IOException());
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        bitInputStream.readBits(8);
        
        bitInputStream.close();
    }
    
    @Test
    public void testClearBitCache() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {(byte)0b01101001, (byte)0b01010011});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        bitInputStream.readBits(8);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(8));
        
        bitInputStream.close();
    }
}