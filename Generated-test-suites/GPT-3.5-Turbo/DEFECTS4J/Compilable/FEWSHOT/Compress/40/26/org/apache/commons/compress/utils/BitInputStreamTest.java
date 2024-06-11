package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.junit.Test;

public class BitInputStreamTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[] {}), ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsGreaterThanMaximumCacheSize() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[] {}), ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(64);
    }
    
    @Test
    public void testReadBitsLittleEndian() throws IOException {
        byte[] bytes = { 1, 2, 0, 3, 4, 5, 6, 7, 8, 9, 10 };
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(bytes), ByteOrder.LITTLE_ENDIAN);
        
        assertEquals(1, bitInputStream.readBits(1));
        assertEquals(513, bitInputStream.readBits(10));
        assertEquals(1029, bitInputStream.readBits(11));
        assertEquals(131172, bitInputStream.readBits(18));
        assertEquals(2207, bitInputStream.readBits(12));
        assertEquals(552972, bitInputStream.readBits(19));
        assertEquals(-1, bitInputStream.readBits(63));
    }
    
    @Test
    public void testReadBitsBigEndian() throws IOException {
        byte[] bytes = { 1, 2, 0, 3, 4, 5, 6, 7, 8, 9, 10 };
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(bytes), ByteOrder.BIG_ENDIAN);
        
        assertEquals(2, bitInputStream.readBits(1));
        assertEquals(260, bitInputStream.readBits(10));
        assertEquals(1034, bitInputStream.readBits(11));
        assertEquals(67620, bitInputStream.readBits(18));
        assertEquals(1431, bitInputStream.readBits(12));
        assertEquals(591503, bitInputStream.readBits(19));
        assertEquals(-1, bitInputStream.readBits(63));
    }
    
    @Test
    public void testClearBitCache() throws IOException {
        byte[] bytes = { 1, 2, 0, 3, 4, 5, 6, 7, 8, 9, 10 };
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(bytes), ByteOrder.LITTLE_ENDIAN);
        
        assertEquals(1, bitInputStream.readBits(1));
        assertEquals(513, bitInputStream.readBits(10));
        
        bitInputStream.clearBitCache();
        
        assertEquals(1, bitInputStream.readBits(1));
        assertEquals(513, bitInputStream.readBits(10));
    }
    
    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        bitInputStream.close();
        
        assertNull(bitInputStream.readBits(1));
    }

}