
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.junit.Test;

public class BitInputStreamTest {
    
    @Test
    public void testReadBits() throws IOException {
        // Test reading 0 bits
        InputStream emptyStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream1 = new BitInputStream(emptyStream, ByteOrder.BIG_ENDIAN);
        assertEquals(-1, bitInputStream1.readBits(0));
        bitInputStream1.close();
        
        // Test reading 1 bit
        byte[] singleByte = {(byte) 0xFF};
        InputStream singleByteStream = new ByteArrayInputStream(singleByte);
        BitInputStream bitInputStream2 = new BitInputStream(singleByteStream, ByteOrder.BIG_ENDIAN);
        assertEquals(1, bitInputStream2.readBits(1));
        bitInputStream2.clearBitCache();
        
        // Test reading 8 bits
        byte[] eightBytes = {(byte) 0xFF, (byte) 0x00};
        InputStream eightBytesStream = new ByteArrayInputStream(eightBytes);
        BitInputStream bitInputStream3 = new BitInputStream(eightBytesStream, ByteOrder.BIG_ENDIAN);
        assertEquals(255, bitInputStream3.readBits(8));
        bitInputStream3.clearBitCache();
        
        // Test reading 64 bits
        byte[] eightBytes2 = {(byte) 0xFF, (byte) 0x00, (byte) 0xFF, (byte) 0x00, (byte) 0xFF, (byte) 0x00, (byte) 0xFF, (byte) 0x00};
        InputStream eightBytes2Stream = new ByteArrayInputStream(eightBytes2);
        BitInputStream bitInputStream4 = new BitInputStream(eightBytes2Stream, ByteOrder.BIG_ENDIAN);
        assertEquals(18374686479671623680L, bitInputStream4.readBits(64));
        bitInputStream4.clearBitCache();
        
        // Test reading more bits than available
        byte[] singleByte2 = {(byte) 0xFF};
        InputStream singleByte2Stream = new ByteArrayInputStream(singleByte2);
        BitInputStream bitInputStream5 = new BitInputStream(singleByte2Stream, ByteOrder.BIG_ENDIAN);
        assertEquals(-1, bitInputStream5.readBits(9));
        bitInputStream5.clearBitCache();
    }
}
