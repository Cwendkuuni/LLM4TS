import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {
    
    private static final byte LITTLE_ENDIAN_BYTE_1 = 0b11011010;
    private static final byte LITTLE_ENDIAN_BYTE_2 = 0b11100101;
    private static final byte BIG_ENDIAN_BYTE_1 = 0b01011100;
    private static final byte BIG_ENDIAN_BYTE_2 = 0b01100011;

    private BitInputStream bitInputStream;
    private InputStream inputStream;
    
    @Before
    public void setup() throws IOException {
        byte[] data = {LITTLE_ENDIAN_BYTE_1, LITTLE_ENDIAN_BYTE_2};
        inputStream = new ByteArrayInputStream(data);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }
    
    @After
    public void teardown() throws IOException {
        bitInputStream.close();
        inputStream.close();
    }

    @Test
    public void testReadBits_LittleEndian_PositiveCount() throws IOException {
        long result = bitInputStream.readBits(5);
        
        assertEquals(26, result);
    }
    
    @Test
    public void testReadBits_BigEndian_PositiveCount() throws IOException {
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        long result = bitInputStream.readBits(7);
        
        assertEquals(58, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_LittleEndian_NegativeCount() throws IOException {
        bitInputStream.readBits(-5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_LittleEndian_CountGreaterThanMaxCacheSize() throws IOException {
        bitInputStream.readBits(64);
    }
    
    @Test
    public void testReadBits_LittleEndian_EndOfStream() throws IOException {
        bitInputStream.readBits(16);
        long result = bitInputStream.readBits(10);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        
        assertEquals(0, bitInputStream.bitsCachedSize);
    }
}