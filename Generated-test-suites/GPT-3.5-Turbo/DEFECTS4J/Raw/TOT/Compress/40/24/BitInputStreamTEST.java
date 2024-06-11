import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import static org.junit.Assert.*;

public class BitInputStreamTest {
    private BitInputStream bitInputStream;
    
    @Before
    public void setUp() {
        byte[] data = {0x12, 0x34, 0x56, 0x78};
        InputStream inputStream = new ByteArrayInputStream(data);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }
    
    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }
    
    @Test
    public void testReadBits_WithValidCount_ShouldReturnBits() throws IOException {
        long result = bitInputStream.readBits(16);
        assertEquals(0x1234, result);
    }
    
    @Test
    public void testReadBits_WithCountZero_ShouldReturnZero() throws IOException {
        long result = bitInputStream.readBits(0);
        assertEquals(0, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_WithInvalidNegativeCount_ShouldThrowIllegalArgumentException() throws IOException {
        bitInputStream.readBits(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_WithInvalidCountGreaterThanMaximumCacheSize_ShouldThrowIllegalArgumentException() throws IOException {
        bitInputStream.readBits(64);
    }
    
    @Test
    public void testReadBits_WithCountGreaterThanAvailableBits_ShouldReturnMinusOne() throws IOException {
        bitInputStream.clearBitCache();
        long result = bitInputStream.readBits(65);
        assertEquals(-1, result);
    }
    
    @Test
    public void testReadBits_WithLittleEndianByteOrder_ShouldReturnBitsInLittleEndianOrder() throws IOException {
        bitInputStream = new BitInputStream(bitInputStream.in, ByteOrder.LITTLE_ENDIAN);
        long result = bitInputStream.readBits(16);
        assertEquals(0x3412, result);
    }
    
    @Test
    public void testClearBitCache_ShouldClearCachedBits() throws IOException {
        bitInputStream.readBits(16);
        bitInputStream.clearBitCache();
        long result = bitInputStream.readBits(16);
        assertEquals(0x3456, result);
    }
}