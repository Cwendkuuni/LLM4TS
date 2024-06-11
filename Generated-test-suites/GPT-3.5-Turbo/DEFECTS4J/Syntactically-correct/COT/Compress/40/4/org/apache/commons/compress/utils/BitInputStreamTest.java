
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() {
        byteArrayInputStream = new ByteArrayInputStream(new byte[] { (byte) 0xAA, (byte) 0xBB, (byte) 0xCC });
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
        byteArrayInputStream.close();
    }
    
    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        // Check if the bit cache is cleared
        assertEquals(0, bitInputStream.readBits(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws IOException {
        // Try to read more than the maximum allowed bits
        bitInputStream.readBits(64);
    }
    
    @Test
    public void testReadBitsLittleEndian() throws IOException {
        BitInputStream littleEndianBitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN);
        
        // Read 4 bits
        long bits1 = littleEndianBitInputStream.readBits(4);
        assertEquals(10, bits1);
        
        // Read 8 bits
        long bits2 = littleEndianBitInputStream.readBits(8);
        assertEquals(0B10101110, bits2);
    }

    @Test
    public void testReadBitsBigEndian() throws IOException {
        // Read 4 bits
        long bits1 = bitInputStream.readBits(4);
        assertEquals(0B1010, bits1);
        
        // Read 8 bits
        long bits2 = bitInputStream.readBits(8);
        assertEquals(0xBB, bits2);
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        bitInputStream.close();
        // Try to read bits after closing the stream
        long bits = bitInputStream.readBits(5);
        assertEquals(-1, bits);
    }

}

