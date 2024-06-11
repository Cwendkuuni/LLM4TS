package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class BitInputStreamTest {
    
    @Mock
    private InputStream inputStream;
    
    private BitInputStream bitInputStream;
    
    @Before
    public void setUp() {
        inputStream = mock(InputStream.class);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }
    
    @After
    public void tearDown() {
        try {
            bitInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadBits() throws IOException {
        when(inputStream.read()).thenReturn((int) 'a');
        when(inputStream.read()).thenReturn((int) 'b');
        when(inputStream.read()).thenReturn((int) 'c');
        when(inputStream.read()).thenReturn((int) 'd');
        
        long result = bitInputStream.readBits(20);
        
        assertEquals(result, 0x61626364L);
    }
    
    @Test
    public void testReadBitsWithNegativeCount() throws IOException {
        long result = bitInputStream.readBits(-1);
        
        assertEquals(result, -1);
    }
    
    @Test
    public void testReadBitsWithZeroCount() throws IOException {
        long result = bitInputStream.readBits(0);
        
        assertEquals(result, 0);
    }
    
    @Test
    public void testReadBitsWithCountGreaterThanMaximumCacheSize() throws IOException {
        long result = bitInputStream.readBits(64);
        
        assertEquals(result, -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThanMaximumCacheSize_expectIllegalArgumentException() throws IOException {
        bitInputStream.readBits(65);
    }
    
    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        
        assertEquals(bitInputStream.bitsCached, 0);
        assertEquals(bitInputStream.bitsCachedSize, 0);
    }
}