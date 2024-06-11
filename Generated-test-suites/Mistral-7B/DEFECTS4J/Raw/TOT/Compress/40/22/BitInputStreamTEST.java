package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[]{-67, 31, -128});
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        assertEquals(ByteOrder.LITTLE_ENDIAN, bitInputStream.byteOrder);
    }

    /**
     * Test close method.
     */
    @org.junit.Test
    public void testClose() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{-67, 31, -128});
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.close();
        try {
            bitInputStream.readBits(1);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    /**
     * Test clearBitCache method.
     */
    @org.junit.Test
    public void testClearBitCache() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{-67, 31, -128});
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.clearBitCache();
        try {
            bitInputStream.readBits(1);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    /**
     * Test readBits method with positive count.
     */
    @org.junit.Test
    public void testReadBitsPositiveCount() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{-67, 31, -128});
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        long bits = bitInputStream.readBits(1);
        assertEquals(-1, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(0x01 & 0xFF, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(0x02 & 0xFF, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(0x04 & 0xFF, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(0x08 & 0xFF, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(0x10 & 0xFF, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(0x20 & 0xFF, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(0x40 & 0xFF, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(0x80 & 0xFF, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(-1, bits);

        // Test reading 63 bits
        long expected = 0b11111111111111111111111111111111111111111111111111111111;
        bits = 0L;
        for (int i = 0; i < 63; i++) {
            bits <<= 1;
            bits |= bitInputStream.readBits(1);
        }
        assertEquals(expected, bits);
    }

    /**
     * Test readBits method with negative count.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{-67, 31, -128});
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(-1);
    }

    /**
     * Test readBits method with count greater than maximum cache size.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testReadBitsGreaterThanMaximumCacheSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{-67, 31, -128});
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(64);
    }

    /**
     * Test readBits method with end of stream.
     */
    @org.junit.Test
    public void testReadBitsEndOfStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{-67, 31});
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        long bits = bitInputStream.readBits(1);
        assertEquals(0x01 & 0xFF, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(0x02 & 0xFF, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(-1, bits);
    }
}