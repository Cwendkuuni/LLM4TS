package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;

import org.junit.Test;

/**
 * Test cases for {@link BitInputStream} class.
 */
public class BitInputStreamTest {

    /**
     * Test constructor.
     * @throws IOException on error
     */
    @Test
    public void testConstructor() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0xAA, (byte) 0x55});
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertNotNull(bitInputStream);
    }

    /**
     * Test close method.
     * @throws IOException on error
     */
    @Test
    public void testClose() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0xAA, (byte) 0x55});
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.close();
        assertFalse(inputStream.isOpen());
    }

    /**
     * Test clearBitCache method.
     * @throws IOException on error
     */
    @Test
    public void testClearBitCache() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0xAA, (byte) 0x55});
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    /**
     * Test readBits method with positive count and no bits in cache.
     * @throws IOException on error
     */
    @Test
    public void testReadBitsNoBitsInCache() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0xAA, (byte) 0x55});
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        final long expected = 0b10101111_11110111L; // AA in hex
        final long actual = bitInputStream.readBits(16);
        assertEquals(expected, actual);
    }

    /**
     * Test readBits method with positive count and bits in cache.
     * @throws IOException on error
     */
    @Test
    public void testReadBitsWithBitsInCache() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0xAA, (byte) 0x55});
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(8); // read 1 byte
        final long expected = 0b10111111_01110111L; // AA in hex, read LSB first
        final long actual = bitInputStream.readBits(8);
        assertEquals(expected, actual);
    }

    /**
     * Test readBits method with negative count.
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0xAA, (byte) 0x55});
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(-1);
    }

    /**
     * Test readBits method with count greater than MAXIMUM_CACHE_SIZE.
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsGreaterThanMaximumCacheSize() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0xAA, (byte) 0x55});
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(64);
    }

    /**
     * Test readBits method when reaching the end of the underlying stream.
     * @throws IOException on error
     */
    @Test
    public void testReadBitsEndOfStream() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        final long expected = -1L;
        final long actual = bitInputStream.readBits(1);
        assertEquals(expected, actual);
    }

    /**
     * Test readBits method with count equal to the number of bits in the last byte read.
     * @throws IOException on error
     */
    @Test
    public void testReadBitsLastByte() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0xAA});
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        final long expected = 0b11111111L; // AA in hex
        final long actual = bitInputStream.readBits(8);
        assertEquals(expected, actual);
    }

    /**
     * Test readBits method with count bigger than the number of remaining bits in the last byte read.
     * @throws IOException on error
     */
    @Test
    public void testReadBitsLastByteExcessive() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0xAA});
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        final long expected = 0b11111111L; // AA in hex
        final long actual = bitInputStream.readBits(9);
        assertEquals(expected, actual);
    }
}