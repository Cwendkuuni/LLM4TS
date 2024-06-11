package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BitInputStreamTest {

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(null, ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThanMaximumCacheSize() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(null, ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(64);
    }
    
    @Test
    public void testReadBitsWithCountEqualToZero() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(null, ByteOrder.BIG_ENDIAN);
        long result = bitInputStream.readBits(0);
        assertEquals(0, result);
    }
    
    @Test
    public void testReadBitsWithCountEqualToMaximumCacheSize() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(null, ByteOrder.BIG_ENDIAN);
        long result = bitInputStream.readBits(63);
        assertEquals(0, result); // TODO: Provide expected value
    }
    
    @Test
    public void testReadBitsWithCountEqualToMaximumCacheSizeAndLittleEndianByteOrder() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(null, ByteOrder.LITTLE_ENDIAN);
        long result = bitInputStream.readBits(63);
        assertEquals(0, result); // TODO: Provide expected value
    }
    
    @Test
    public void testReadBitsWithCountEqualToMaximumCacheSizeAndEndOfStreamReached() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        long result = bitInputStream.readBits(63);
        assertEquals(-1, result); // TODO: Provide expected value
    }
    
    @Test
    public void testReadBitsWithCountEqualToMaximumCacheSizeAndEndOfStreamReachedAndLittleEndianByteOrder() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long result = bitInputStream.readBits(63);
        assertEquals(-1, result); // TODO: Provide expected value
    }
    
    @Test
    public void testReadBitsWithCountEqualToMaximumCacheSizeAndBitsInCache() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[8]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        long result = bitInputStream.readBits(63);
        assertEquals(0, result); // TODO: Provide expected value
    }
    
    @Test
    public void testReadBitsWithCountEqualToMaximumCacheSizeAndBitsInCacheAndLittleEndianByteOrder() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[8]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long result = bitInputStream.readBits(63);
        assertEquals(0, result); // TODO: Provide expected value
    }
    
    @Test
    public void testReadBitsWithCountEqualToOne() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(null, ByteOrder.BIG_ENDIAN);
        long result = bitInputStream.readBits(1);
        assertEquals(0, result); // TODO: Provide expected value
    }
    
    @Test
    public void testReadBitsWithCountEqualToOneAndLittleEndianByteOrder() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(null, ByteOrder.LITTLE_ENDIAN);
        long result = bitInputStream.readBits(1);
        assertEquals(0, result); // TODO: Provide expected value
    }
}