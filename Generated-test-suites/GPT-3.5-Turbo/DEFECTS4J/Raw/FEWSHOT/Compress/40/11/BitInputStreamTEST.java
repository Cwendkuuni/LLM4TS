
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {
    
    private InputStream mockInputStream;
    private BitInputStream bitInputStreamLE;
    private BitInputStream bitInputStreamBE;
    
    @Before
    public void setUp() {
        mockInputStream = mock(InputStream.class);
        bitInputStreamLE = new BitInputStream(mockInputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStreamBE = new BitInputStream(mockInputStream, ByteOrder.BIG_ENDIAN);
    }
    
    @After
    public void tearDown() throws IOException {
        bitInputStreamLE.close();
        bitInputStreamBE.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        bitInputStreamLE.readBits(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThanMaximumCacheSize() throws IOException {
        bitInputStreamLE.readBits(64);
    }
    
    @Test
    public void testReadBitsWithCountLessThanMaximumCacheSize() throws IOException {
        when(mockInputStream.read()).thenReturn(1).thenReturn(2); // simulate 1 byte input
        
        long resultLE = bitInputStreamLE.readBits(16);
        assertEquals(258, resultLE);
        
        long resultBE = bitInputStreamBE.readBits(16);
        assertEquals(513, resultBE);
    }
    
    @Test
    public void testReadBitsWithCountEqualToMaximumCacheSize() throws IOException {
        when(mockInputStream.read()).thenReturn(1).thenReturn(2); // simulate 1 byte input
        
        long resultLE = bitInputStreamLE.readBits(63);
        assertEquals(131073, resultLE);
        
        long resultBE = bitInputStreamBE.readBits(63);
        assertEquals(262146, resultBE);
    }
    
    @Test
    public void testReadBitsWithEndOfStreamReached() throws IOException {
        when(mockInputStream.read()).thenReturn(-1).thenReturn(2);
        
        long resultLE = bitInputStreamLE.readBits(16);
        assertEquals(-1, resultLE);
    }
    
    @Test
    public void testClearBitCache() {
        bitInputStreamLE.clearBitCache();
        bitInputStreamBE.clearBitCache();
        
        // Check if the bitsCached and bitsCachedSize are reset to 0
        assertEquals(0, bitInputStreamLE.bitsCached);
        assertEquals(0, bitInputStreamLE.bitsCachedSize);
        assertEquals(0, bitInputStreamBE.bitsCached);
        assertEquals(0, bitInputStreamBE.bitsCachedSize);
    }
    
    @Test
    public void testClose() throws IOException {
        bitInputStreamLE.close();
        
        // Verify if the close() method of InputStream is called
        verify(mockInputStream, times(1)).close();
    }
}
