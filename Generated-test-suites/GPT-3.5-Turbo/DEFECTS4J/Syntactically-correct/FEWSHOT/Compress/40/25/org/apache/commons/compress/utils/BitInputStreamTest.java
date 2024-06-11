
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.junit.Test;

public class BitInputStreamTest {
    
    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        bitInputStream.close();
        
        // Assert that the underlying stream is closed
        // ...
    }
    
    @Test
    public void testClearBitCache() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        bitInputStream.clearBitCache();
        
        // Assert that the bit cache has been cleared
        // ...
    }
    
    @Test
    public void testReadBits() throws IOException {
        byte[] bytes = {0b11110000, 0b10101010};
        InputStream inputStream = new ByteArrayInputStream(bytes);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        long result = bitInputStream.readBits(4);
        
        assertEquals(0b1111, result);
    }
    
    @Test
    public void testReadBits_EndOfStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        long result = bitInputStream.readBits(4);
        
        assertEquals(-1, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_InvalidCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        bitInputStream.readBits(64);
    }
    
}
