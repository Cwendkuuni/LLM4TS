
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {
    private BitInputStream bitInputStream;
    private InputStream mockInputStream;
    
    @Before
    public void setUp() {
        mockInputStream = mock(InputStream.class);
        bitInputStream = new BitInputStream(mockInputStream, ByteOrder.BIG_ENDIAN);
    }
    
    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }
    
    @Test
    public void testReadBits() throws IOException {
        when(mockInputStream.read()).thenReturn(0x01).thenReturn(0x02).thenReturn(0x03);
        
        long result = bitInputStream.readBits(8);
        assertEquals(1, result);
        result = bitInputStream.readBits(16);
        assertEquals(258, result);
        
        verify(mockInputStream).read();
        verify(mockInputStream).read();
        verify(mockInputStream).read();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeBitsCount() throws IOException {
        bitInputStream.readBits(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithExceedingBitsCount() throws IOException {
        bitInputStream.readBits(64);
    }
    
    @Test
    public void testReadBitsWhenEndOfStream() throws IOException {
        when(mockInputStream.read()).thenReturn(-1);
        
        long result = bitInputStream.readBits(8);
        assertEquals(-1, result);
        
        verify(mockInputStream).read();
    }
    
    @Test
    public void testClearBitCache() throws IOException {
        when(mockInputStream.read()).thenReturn(0x01);
        
        bitInputStream.readBits(8);
        bitInputStream.clearBitCache();
        
        long result = bitInputStream.readBits(8);
        assertEquals(1, result);
        
        verify(mockInputStream, 2).read();
    }
}
