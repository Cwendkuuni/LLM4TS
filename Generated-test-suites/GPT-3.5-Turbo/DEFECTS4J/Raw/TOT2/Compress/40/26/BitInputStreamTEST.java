
package org.apache.commons.compress.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import static org.junit.Assert.*;
import org.junit.Test;

public class BitInputStreamTest {

    @Test
    public void testReadBitsValidCount() throws IOException {
        byte[] data = {0x00, 0x01, 0x02, 0x03};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        assertEquals(0, bitInputStream.readBits(0));
        assertEquals(1, bitInputStream.readBits(8));
        assertEquals(513, bitInputStream.readBits(16));
    }
    
    @Test
    public void testReadBitsNegativeCount() throws IOException {
        byte[] data = {0x00, 0x01, 0x02, 0x03};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        try {
            bitInputStream.readBits(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    @Test
    public void testReadBitsTooLargeCount() throws IOException {
        byte[] data = {0x00, 0x01, 0x02, 0x03};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        try {
            bitInputStream.readBits(64);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    @Test
    public void testReadBitsEndOfStream() throws IOException {
        byte[] data = {0x00, 0x01, 0x02, 0x03};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        assertEquals(0x00010203, bitInputStream.readBits(32));
        assertEquals(-1, bitInputStream.readBits(8));
    }
    
    @Test
    public void testClearBitCache() throws IOException {
        byte[] data = {0x00, 0x01, 0x02, 0x03};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        bitInputStream.readBits(16);
        bitInputStream.clearBitCache();
        
        assertEquals(0, bitInputStream.readBits(8));
    }
}

