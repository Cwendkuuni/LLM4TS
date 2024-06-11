package org.apache.commons.compress.utils;

import static org.junit.Assert.fail;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for BitInputStream
 */
public class BitInputStreamTest {

    private InputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() throws IOException {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04};
        inputStream = new ByteArrayInputStream(data);
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

    // Test method for constructor
    @Test
    public void testBitInputStream() {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assert null != bitInputStream;
    }

    // Test method for close
    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
        assert inputStream.available() == 0;
    }

    // Test method for clearBitCache
    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.clearBitCache();
        assert bitsCached(bitInputStream) == 0;
    }

    // Test method for readBits
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegative() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsGreaterThanMaximum() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBits1Bit() throws IOException {
        long bits = bitInputStream.readBits(1);
        assertEquals(0x01, bits);
    }

    @Test
    public void testReadBitsMultipleBitsFromSameByte() throws IOException {
        long bits1 = bitInputStream.readBits(4);
        long bits2 = bitInputStream.readBits(4);
        long bitsExpected = 0x03020100;
        assertEquals(bitsExpected, (bits1 << 4) | bits2);
    }

    @Test
    public void testReadBitsMultipleBytes() throws IOException {
        long bits1 = bitInputStream.readBits(5);
        long bits2 = bitInputStream.readBits(3);
        long bitsExpected = 0x00030201;
        assertEquals(bitsExpected, bits1 << 3 | bits2);
    }

    @Test(expected = IOException.class)
    public void testReadBitsEndOfStream() throws IOException {
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        inputStream.read(); // read last byte
        bitInputStream.readBits(1); // should throw an IOException
    }

    @Test
    public void testReadBitsByteOrderLittleEndian() throws IOException {
        BitInputStream bitInputStreamLE = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long bits = bitInputStreamLE.readBits(1);
        assertEquals(0x01, bits);

        bits = bitInputStreamLE.readBits(1);
        assertEquals(0x02, bits << 1);

        bits = bitInputStreamLE.readBits(1);
        assertEquals(0x04, bits << 2);

        bits = bitInputStreamLE.readBits(2);
        assertEquals(0x03, bits);

        bits = bitInputStreamLE.readBits(7);
        assertEquals(0x7F & 0x03020100, bits);

        bits = bitInputStreamLE.readBits(8);
        assertEquals(0x01020304, bits);
    }

    @Test
    public void testReadBitsByteOrderBigEndian() throws IOException {
        BitInputStream bitInputStreamBE = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        long bits = bitInputStreamBE.readBits(1);
        assertEquals(0x01, bits);

        bits = bitInputStreamBE.readBits(1);
        assertEquals(0x04, bits << 1);

        bits = bitInputStreamBE.readBits(1);
        assertEquals(0x02, bits << 2);

        bits = bitInputStreamBE.readBits(2);
        assertEquals(0x03, bits);

        bits = bitInputStreamBE.readBits(7);
        assertEquals(0x7F & 0x00010203, bits);

        bits = bitInputStreamBE.readBits(8);
        assertEquals(0x04030201, bits);
    }

    private long bitsCached(BitInputStream bitInputStream) {
        return bitInputStream.bitsCached;
    }

    private void assertEquals(long expected, long actual) {
        if (expected != actual) {
            fail("Expected: " + Long.toBinaryString(expected) + ", but was: " + Long.toBinaryString(actual));
        }
    }

    private void assertEquals(int expected, int actual) {
        if (expected != actual) {
            fail("Expected: " + Integer.toBinaryString(expected) + ", but was: " + Integer.toBinaryString(actual));
        }
    }
}