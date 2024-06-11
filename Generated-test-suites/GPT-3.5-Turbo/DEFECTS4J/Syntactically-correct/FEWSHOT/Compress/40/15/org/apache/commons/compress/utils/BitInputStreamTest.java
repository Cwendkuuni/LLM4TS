package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BitInputStreamTest {
    
    @Mock
    private InputStream inputStream;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testConstructor() {
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        assertEquals(inputStream, bitInputStream.in);
        assertEquals(byteOrder, bitInputStream.byteOrder);
    }
    
    @Test
    public void testClose() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        bitInputStream.close();
        verify(inputStream).close();
    }
    
    @Test
    public void testClearBitCache() {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        bitInputStream.bitsCached = 10;
        bitInputStream.bitsCachedSize = 5;
        
        bitInputStream.clearBitCache();
        
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsExceedsMaximumCacheSize() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(64);
    }
    
    @Test
    public void testReadBitsLittleEndian() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        
        bitInputStream.bitsCached = 10101101;
        bitInputStream.bitsCachedSize = 8;
        
        long bits = bitInputStream.readBits(4);
        
        assertEquals(13, bits);
        assertEquals(1101, bitInputStream.bitsCached);
        assertEquals(4, bitInputStream.bitsCachedSize);
    }
    
    @Test
    public void testReadBitsBigEndian() throws IOException {
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        
        bitInputStream.bitsCached = 10101101;
        bitInputStream.bitsCachedSize = 8;
        
        long bits = bitInputStream.readBits(4);
        
        assertEquals(10, bits);
        assertEquals(101011, bitInputStream.bitsCached);
        assertEquals(4, bitInputStream.bitsCachedSize);
    }
    
    @Test
    public void testReadBitsLittleEndianWithEndOfStream() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        
        bitInputStream.bitsCached = -1;
        bitInputStream.bitsCachedSize = 63;
        
        long bits = bitInputStream.readBits(4);
        
        assertEquals(-1, bits);
        assertEquals(-1, bitInputStream.bitsCached);
        assertEquals(63, bitInputStream.bitsCachedSize);
    }
    
    @Test
    public void testReadBitsBigEndianWithEndOfStream() throws IOException {
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        
        bitInputStream.bitsCached = -1;
        bitInputStream.bitsCachedSize = 63;
        
        long bits = bitInputStream.readBits(4);
        
        assertEquals(-1, bits);
        assertEquals(-1, bitInputStream.bitsCached);
        assertEquals(63, bitInputStream.bitsCachedSize);
    }
}