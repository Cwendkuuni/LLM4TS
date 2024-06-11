package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {
    
    private InputStream inputStream;
    private BitInputStream bitInputStream;
    
    @Before
    public void setUp() {
        inputStream = mock(InputStream.class);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
        verify(inputStream).close();
    }
    
    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws IOException {
        bitInputStream.readBits(64);
    }
    
    @Test
    public void testReadBitsLittleEndian() throws IOException {
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        
        when(inputStream.read()).thenReturn(1); // 0b00000001
        
        long result = bitInputStream.readBits(1);
        assertEquals(1, result);
        
        result = bitInputStream.readBits(4);
        assertEquals(8, result);
    }
    
    @Test
    public void testReadBitsBigEndian() throws IOException {
        when(inputStream.read()).thenReturn(1); // 0b00000001
        
        long result = bitInputStream.readBits(1);
        assertEquals(1, result);
        
        result = bitInputStream.readBits(4);
        assertEquals(16, result);
    }
    
    @Test
    public void testReadBitsEndOfFile() throws IOException {
        when(inputStream.read()).thenReturn(-1); // End of file
        
        long result = bitInputStream.readBits(8);
        assertEquals(-1, result);
    }
}