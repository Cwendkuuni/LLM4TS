package org.apache.commons.compress.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testReadBitsWithPositiveCount() throws IOException {
        byte[] inputBytes = {0x01, 0x02, 0x03};
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);
        BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.LITTLE_ENDIAN);
        
        long result = bitInputStream.readBits(8);
        
        assertEquals(1, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        byte[] inputBytes = {0x01};
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);
        BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
        
        bitInputStream.readBits(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithGreaterThanMaximumCount() throws IOException {
        byte[] inputBytes = {0x01};
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);
        BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.LITTLE_ENDIAN);
        
        bitInputStream.readBits(64);
    }
    
    @Test
    public void testClearBitCache() throws IOException {
        byte[] inputBytes = {0x01, 0x02, 0x03};
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);
        BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
        
        bitInputStream.readBits(16);
        bitInputStream.clearBitCache();
        
        long result = bitInputStream.readBits(8);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testReadBitsWithBigEndianByteOrder() throws IOException {
        byte[] inputBytes = {0x01, 0x02, 0x03};
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);
        BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
        
        long result = bitInputStream.readBits(16);
        
        assertEquals(258, result);
    }
    
    @Test
    public void testReadBitsWithLittleEndianByteOrder() throws IOException {
        byte[] inputBytes = {0x01, 0x02, 0x03};
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);
        BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.LITTLE_ENDIAN);
        
        long result = bitInputStream.readBits(16);
        
        assertEquals(513, result);
    }
}