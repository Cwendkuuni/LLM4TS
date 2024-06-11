
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {
    
    private BitInputStream bitInputStream;
    private InputStream inputStreamMock;
    
    @Before
    public void setUp() {
        inputStreamMock = mock(InputStream.class);
        bitInputStream = new BitInputStream(inputStreamMock, ByteOrder.BIG_ENDIAN);
    }
    
    @Test
    public void testReadBits() throws IOException {
        byte[] data = { 0x12, 0x34 };
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
        bitInputStream = new BitInputStream(byteInputStream, ByteOrder.LITTLE_ENDIAN);
        
        assertEquals(0x3412, bitInputStream.readBits(16));
        assertEquals(0x2, bitInputStream.readBits(2));
        assertEquals(0x34, bitInputStream.readBits(8));
        assertEquals(0x1, bitInputStream.readBits(4));
        assertEquals(-1, bitInputStream.readBits(8));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsCountGreaterThanMaximumCacheSize() throws IOException {
        bitInputStream.readBits(BitInputStream.MAXIMUM_CACHE_SIZE + 1);
    }
    
    @Test
    public void testReadBitsEndOfStreamBeforeRequestedBits() throws IOException {
        when(inputStreamMock.read()).thenReturn(-1);
        
        assertEquals(-1, bitInputStream.readBits(8));
    }
    
    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }
    
    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
        
        verify(inputStreamMock).close();
    }
}
