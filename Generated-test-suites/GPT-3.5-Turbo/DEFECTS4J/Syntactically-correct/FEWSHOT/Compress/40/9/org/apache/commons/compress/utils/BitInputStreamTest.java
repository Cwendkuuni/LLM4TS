
package org.apache.commons.compress.utils;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.*;

public class BitInputStreamTest {
    
    @Test
    public void testReadBits() throws IOException {
        // Initialize InputStream and BitInputStream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x01 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Test reading 1 bit
        long result = bitInputStream.readBits(1);
        assertEquals(0, result);

        // Clear the bit cache
        bitInputStream.clearBitCache();
        
        // Test reading 2 bits
        long result2 = bitInputStream.readBits(2);
        assertEquals(1, result2);
        
        // Clear the bit cache
        bitInputStream.clearBitCache();
        
        // Test reading 8 bits
        long result3 = bitInputStream.readBits(8);
        assertEquals(1, result3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCount() throws IOException {
        // Initialize InputStream and BitInputStream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x01 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        // Test reading more than the maximum count of bits
        bitInputStream.readBits(64);
    }
    
    @Test
    public void testReadBitsEndOfStream() throws IOException {
        // Initialize empty InputStream and BitInputStream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        // Test reading bits when end of stream is reached
        long result = bitInputStream.readBits(8);
        assertEquals(-1, result);
    }
}
