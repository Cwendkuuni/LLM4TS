import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BitInputStreamTest {

    // Test for constructor
    @Test
    public void testConstructor() {
        InputStream mockInputStream = mock(InputStream.class);
        ByteOrder mockByteOrder = ByteOrder.BIG_ENDIAN;
        
        BitInputStream bitInputStream = new BitInputStream(mockInputStream, mockByteOrder);
        
        assertNotNull(bitInputStream);
        assertEquals(mockInputStream, bitInputStream.in);
        assertEquals(mockByteOrder, bitInputStream.byteOrder);
    }

    // Test for close method
    @Test
    public void testClose() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);
        
        BitInputStream bitInputStream = new BitInputStream(mockInputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.close();
        
        verify(mockInputStream).close();
    }

    // Test for clearBitCache method
    @Test
    public void testClearBitCache() {
        InputStream mockInputStream = mock(InputStream.class);
        
        BitInputStream bitInputStream = new BitInputStream(mockInputStream, ByteOrder.BIG_ENDIAN);
        bitInputStream.clearBitCache();
        
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    // Test for readBits method
    @Test
    public void testReadBits() throws IOException {
        InputStream mockInputStream = new ByteArrayInputStream(new byte[]{0b1101_0101});
        
        BitInputStream bitInputStream = new BitInputStream(mockInputStream, ByteOrder.LITTLE_ENDIAN);
        
        assertEquals(5, bitInputStream.readBits(3));
        assertEquals(0b100, bitInputStream.readBits(3));
        
        assertEquals(-1, bitInputStream.readBits(5));
    }

    // Test for exception handling in readBits method
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);
        
        BitInputStream bitInputStream = new BitInputStream(mockInputStream, ByteOrder.BIG_ENDIAN);
        
        bitInputStream.readBits(64); // count greater than maximum CACHE_SIZE, should throw IllegalArgumentException
    }

    // Test for exception handling in readBits method when end of stream reached
    @Test
    public void testReadBitsEndOfStream() throws IOException {
        InputStream mockInputStream = new ByteArrayInputStream(new byte[]{0b1101_0101});
        
        BitInputStream bitInputStream = new BitInputStream(mockInputStream, ByteOrder.LITTLE_ENDIAN);
        
        assertEquals(0b101_0101, bitInputStream.readBits(8));
        assertEquals(-1, bitInputStream.readBits(8)); // end of stream reached, should return -1
    }

    // Test for exception handling in readBits method when end of stream reached before reading requested bits
    @Test
    public void testReadBitsEndOfStreamBeforeRequestedBits() throws IOException {
        InputStream mockInputStream = new ByteArrayInputStream(new byte[]{0b1000_0000});
        
        BitInputStream bitInputStream = new BitInputStream(mockInputStream, ByteOrder.BIG_ENDIAN);
        
        assertEquals(0b1000_0000, bitInputStream.readBits(9)); // end of stream reached before requested bits, should return -1
    }
}