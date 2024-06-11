package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    private BitInputStream bitInputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayInputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1a, (byte) 0x2b, (byte) 0x3c});
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws Exception {
        if (bitInputStream != null) {
            bitInputStream.close();
        }
        if (byteArrayInputStream != null) {
            byteArrayInputStream.close();
        }
    }

    // Test constructor
    @Test
    public void testBitInputStreamConstructor() {
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[0]), ByteOrder.LITTLE_ENDIAN);
        assertNotNull(bitInputStream);
    }

    // Test close() method
    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
        assertFalse(bitInputStream.isOpen());
    }

    // Test clearBitCache() method
    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.readBits(16);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCached);
    }

    // Test readBits() method - normal case
    @Test
    public void testReadBitsNormalCase() throws IOException {
        long expected = 0b11111111; // 7 bits from first byte
        long actual = bitInputStream.readBits(7);
        assertEquals(expected, actual);

        expected = 0b1111000000111111; // 12 bits from first and second bytes
        actual = bitInputStream.readBits(12);
        assertEquals(expected, actual);
    }

    // Test readBits() method - count greater than available bits
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsCountGreaterThanAvailableBits() throws IOException {
        bitInputStream.readBits(64);
    }

    // Test readBits() method - end of stream
    @Test
    public void testReadBitsEndOfStream() throws IOException {
        long actual = bitInputStream.readBits(8);
        assertEquals(-1, actual);
    }

    // Test readBits() method - byte order
    @Test
    public void testReadBitsByteOrder() throws IOException {
        ByteOrder bigEndian = ByteOrder.BIG_ENDIAN;
        BitInputStream bigEndianBitInputStream = new BitInputStream(byteArrayInputStream, bigEndian);
        long expected = 0b00001111; // 4 bits from first byte (big-endian)
        long actual = bigEndianBitInputStream.readBits(4);
        assertEquals(expected, actual);
    }
}