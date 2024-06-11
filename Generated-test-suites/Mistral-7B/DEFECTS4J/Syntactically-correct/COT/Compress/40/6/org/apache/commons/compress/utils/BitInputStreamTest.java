package org.apache.commons.compress.utils;

import static org.junit.Assert.fail;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Test cases for BitInputStream class
 */
public class BitInputStreamTest {

    private InputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        byte[] inputData = new byte[]{(byte) 0b11110001, (byte) 0b11111111};
        inputStream = new ByteArrayInputStream(inputData);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        if (bitInputStream != null) {
            bitInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    // Test constructor
    @Test
    public void testBitInputStreamConstruction() {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assert null != bitInputStream;
    }

    // Test close method
    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
        assert inputStream.available() == 0;
    }

    // Test clearBitCache method
    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.clearBitCache();
        assert bitsCached(0) == 0;
    }

    // Test readBits method with valid count
    @Test
    public void testReadBitsValidCount() throws IOException {
        long expected = 0b0111_1100_1111_1111L;
        long actual = bitInputStream.readBits(15);
        assert expected == actual;
    }

    // Test readBits method with zero count
    @Test
    public void testReadBitsZeroCount() throws IOException {
        long actual = bitInputStream.readBits(0);
        assertLongEquals(0, actual);
    }

    // Test readBits method with negative count
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
        fail("Expected IllegalArgumentException");
    }

    // Test readBits method with count greater than maximum cache size
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsGreaterThanMaximumCacheSize() throws IOException {
        bitInputStream.readBits(64);
        fail("Expected IllegalArgumentException");
    }

    // Test readBits method at end of stream
    @Test
    public void testReadBitsEndOfStream() throws IOException {
        long actual = bitInputStream.readBits(15);
        long expected = -1;
        assert expected == actual;
    }

    // Test readBits method with byte order as BIG_ENDIAN
    @Test
    public void testReadBitsByteOrderBIG_ENDIAN() throws IOException {
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        long expected = 0b1111_0001_1111_1111L;
        long actual = bitInputStream.readBits(15);
        assert expected == actual;
    }

    private long bitsCached() throws IOException {
        return bitInputStream.bitsCached;
    }

    private void assertLongEquals(long expected, long actual) {
        if (expected != actual) {
            fail("Expected: " + Long.toBinaryString(expected) + ", but was: " + Long.toBinaryString(actual));
        }
    }
}