package org.apache.commons.compress.utils;

import static org.junit.Assert.fail;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    private InputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        byte[] inputData = new byte[]{(byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
        inputStream = new ByteArrayInputStream(inputData);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testConstructor() {
        // Test valid constructor
        BitInputStream testBitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidByteOrder() {
        // Test constructor with invalid byte order
        BitInputStream testBitInputStream = new BitInputStream(inputStream, ByteOrder.CUSTOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() {
        // Test reading bits with a negative count
        bitInputStream.readBits(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThanMaximum() {
        // Test reading bits with a count greater than maximum
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsWithValidCount() throws IOException {
        // Test reading valid number of bits
        long expected = 0b11111111_11111111_11110111_11110111; // 0xABCD in LITTLE_ENDIAN byte order
        long actual = bitInputStream.readBits(24);
        assertEquals(expected, actual);
    }

    @Test
    public void testReadBitsWithRemainingBits() throws IOException {
        // Test reading remaining bits after reading a valid number of bits
        long expectedRemainingBits = 0b0111_0111_0111_1111; // bits left in the input byte after reading 24 bits
        long actualRemainingBits = bitInputStream.readBits(8);
        assertEquals(expectedRemainingBits, actualRemainingBits);
    }

    @Test
    public void testClearBitCache() throws IOException {
        // Test clearing the bit cache
        bitInputStream.readBits(24);
        bitInputStream.clearBitCache();
        long actual = bitInputStream.readBits(8);
        assertEquals(-1, actual); // Test that reading bits after calling clearBitCache() returns -1
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        bitInputStream.close();
        bitInputStream.readBits(8);
    }
}