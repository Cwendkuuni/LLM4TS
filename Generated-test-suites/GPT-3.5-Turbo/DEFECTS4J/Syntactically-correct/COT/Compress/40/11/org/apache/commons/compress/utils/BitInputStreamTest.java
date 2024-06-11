
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

public class BitInputStreamTest {
    
    @Test
    public void testConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        
        assertNotNull(bitInputStream);
        assertEquals(inputStream, bitInputStream.in);
        assertEquals(byteOrder, bitInputStream.byteOrder);
    }
    
    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        bitInputStream.close();
        
        // Check if the underlying InputStream is closed
        assertTrue(inputStream.read() < 0);
    }
    
    @Test
    public void testClearBitCache() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(32);
        
        bitInputStream.clearBitCache();
        
        // Try to read more bits after clearing the cache
        long bits = bitInputStream.readBits(8);
        assertEquals(-1, bits);
    }
    
    @Test
    public void testReadBits() throws IOException {
        byte[] bytes = {0x12, 0x34, 0x56, 0x78};
        InputStream inputStream = new ByteArrayInputStream(bytes);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        // Read 4 bits from the first byte (0x12)
        long bits = bitInputStream.readBits(4);
        assertEquals(0x01, bits);
        
        // Read 12 bits (5 bits from the first byte and 7 bits from the second byte)
        bits = bitInputStream.readBits(12);
        assertEquals(0x023, bits);
        
        // Read 12 bits (1 bit from the second byte and 11 bits from the third byte)
        bits = bitInputStream.readBits(12);
        assertEquals(0x045, bits);
        
        // Try to read more bits than available in the input stream
        bits = bitInputStream.readBits(32);
        assertEquals(-1, bits);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        bitInputStream.readBits(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsCountTooLarge() throws IOException {
        byte[] bytes = {0x12, 0x34, 0x56, 0x78};
        InputStream inputStream = new ByteArrayInputStream(bytes);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        // Try to read more than 63 bits
        bitInputStream.readBits(64);
    }
}
