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
 * JUnit 4 test file for {@link BitInputStream} class.
 */
public class BitInputStreamTest {

    private InputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        byte[] testData = new byte[]{(byte) 0x01, (byte) 0x2a, (byte) 0x3b, (byte) 0x4c};
        inputStream = new ByteArrayInputStream(testData);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testReadBitsWithValidCount() throws IOException {
        // Test reading 1 bit
        long bit1 = bitInputStream.readBits(1);
        assertEquals(1, bit1);

        // Test reading 7 bits
        long bit7 = bitInputStream.readBits(7);
        assertEquals(0b11111111, bit7);

        // Test reading 8 bits
        long byte1 = bitInputStream.readBits(8);
        assertEquals(0b00010110, byte1);

        // Test reading 16 bits
        long word1 = bitInputStream.readBits(16);
        assertEquals(0b0011111101111111, word1);

        // Test reading 32 bits
        long long1 = bitInputStream.readBits(32);
        assertEquals(0b00111111011111110111111101111111, long1);

        // Test reading 63 bits
        long long63 = bitInputStream.readBits(63);
        assertEquals(0b1111111111111111111111111111111111111111111111111111111, long63);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThanMaximum() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.clearBitCache();
        long bit1 = bitInputStream.readBits(1);
        fail("Expected bit1 to be 0");
    }

    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
        bitInputStream.readBits(1);
        fail("Expected IOException to be thrown");
    }

    private void assertEquals(long expected, long actual) {
        assertEquals(Long.toBinaryString(expected), Long.toBinaryString(actual));
    }
}