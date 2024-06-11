package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Test cases for BitInputStream class.
 */
public class BitInputStreamTest {

    private ByteArrayInputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x23, 0x45, 0x67, 0x89});
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testConstructor() {
        BitInputStream bitInputStream = new BitInputStream(null, ByteOrder.LITTLE_ENDIAN);
        assertNull(bitInputStream);

        bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[]{0x01}), ByteOrder.LITTLE_ENDIAN);
        assertNotNull(bitInputStream);
    }

    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
        assertTrue(inputStream.available() == 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsGreaterThanMaximumCacheSize() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsBasicFunctionality() throws IOException {
        long expected = 0b0111011101110111;
        long actual = bitInputStream.readBits(8);
        assertEquals(expected, actual);

        expected = 0b1110111011101110;
        actual = bitInputStream.readBits(12);
        assertEquals(expected, actual);

        expected = 0b1111111111111111;
        actual = bitInputStream.readBits(16);
        assertEquals(expected, actual);
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        long actual = bitInputStream.readBits(1);
        assertEquals(-1, actual);
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.clearBitCache();
        long actual = bitInputStream.readBits(8);
        assertEquals(0, actual);

        bitInputStream.readBits(8);
        bitInputStream.readBits(1);

        bitInputStream.clearBitCache();
        actual = bitInputStream.readBits(1);
        assertEquals(0, actual);
    }
}