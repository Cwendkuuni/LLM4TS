package org.apache.commons.compress.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

public class BitInputStreamTest {
    
    @Test
    public void testReadBits() throws IOException {
        // Test reading from an empty InputStream
        InputStream emptyStream = new ByteArrayInputStream(new byte[] {});
        BitInputStream bitInputStreamEmpty = new BitInputStream(emptyStream, ByteOrder.BIG_ENDIAN);
        long noBitsRead = bitInputStreamEmpty.readBits(1);
        assertEquals(-1, noBitsRead);
        bitInputStreamEmpty.close();
        
        // Test reading less than 63 bits from a Big Endian InputStream
        InputStream testStreamBigEndian = new ByteArrayInputStream(new byte[] {(byte) 0b10101010, (byte) 0b01010101});
        BitInputStream bitInputStreamBigEndian = new BitInputStream(testStreamBigEndian, ByteOrder.BIG_ENDIAN);
        long bitsReadBigEndian = bitInputStreamBigEndian.readBits(10);
        assertEquals(0b1010101001, bitsReadBigEndian);
        bitInputStreamBigEndian.close();
        
        // Test reading 63 bits from a Little Endian InputStream
        InputStream testStreamLittleEndian = new ByteArrayInputStream(new byte[] {(byte) 0b10101010, (byte) 0b01010101});
        BitInputStream bitInputStreamLittleEndian = new BitInputStream(testStreamLittleEndian, ByteOrder.LITTLE_ENDIAN);
        long bitsReadLittleEndian = bitInputStreamLittleEndian.readBits(63);
        assertEquals(0x5555_5555_5AL, bitsReadLittleEndian);
        bitInputStreamLittleEndian.close();
        
        // Test reading more than 63 bits from a Little Endian InputStream
        InputStream testStreamManyBitsLittleEndian = new ByteArrayInputStream(new byte[] {(byte) 0b10101010, (byte) 0b01010101, (byte) 0b11001100});
        BitInputStream bitInputStreamManyBitsLittleEndian = new BitInputStream(testStreamManyBitsLittleEndian, ByteOrder.LITTLE_ENDIAN);
        long bitsReadManyBitsLittleEndian = bitInputStreamManyBitsLittleEndian.readBits(75);
        assertEquals(0b11001100_10101010_01L, bitsReadManyBitsLittleEndian);
        bitInputStreamManyBitsLittleEndian.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws IOException {
        InputStream testStream = new ByteArrayInputStream(new byte[] {});
        BitInputStream bitInputStream = new BitInputStream(testStream, ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(64);
        bitInputStream.close();
    }
    
    @Test
    public void testClearBitCache() throws IOException {
        InputStream testStream = new ByteArrayInputStream(new byte[] {(byte) 0b10101010, (byte) 0b01010101});
        BitInputStream bitInputStream = new BitInputStream(testStream, ByteOrder.BIG_ENDIAN);
        
        // Read and cache some bits
        bitInputStream.readBits(5);
        bitInputStream.readBits(3);
        bitInputStream.readBits(4);
        
        // Clear the cache
        bitInputStream.clearBitCache();
        
        // Verify that the cache is cleared
        long noBitsRead = bitInputStream.readBits(1);
        assertEquals(-1, noBitsRead);
        
        bitInputStream.close();
    }
}