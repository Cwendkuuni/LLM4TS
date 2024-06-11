package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;

public class BitInputStreamTest {

    private static final int TEST_DATA_SIZE = 10;
    private static final byte TEST_DATA[] = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A};
    private BitInputStream bitInputStream;

    @Before
    public void setup() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testReadBitsWithValidCount() throws IOException {
        // Test reading 1 bit
        assertEquals(0x01 & 0x01L, bitInputStream.readBits(1));

        // Test reading multiple bits
        assertEquals((0x01 << 1) | (0x02 & 0x01L), bitInputStream.readBits(2));
        assertEquals((0x01 << 2) | ((0x02 & 0x01L) << 1) | ((0x03 & 0x01L) << 2), bitInputStream.readBits(3));

        // Test reading 63 bits
        long expected = 0x01L;
        for (int i = 0; i < 62; i++) {
            expected <<= 1;
            expected |= (TEST_DATA[i / 8] >> (i % 8));
            assertEquals(expected, bitInputStream.readBits(1));
            expected <<= 1;
        }
        assertEquals(-1L, bitInputStream.readBits(1)); // End of stream reached
    }

    @Test
    public void testReadBitsWithInvalidCount() throws IOException {
        // Test reading a negative count
        try {
            bitInputStream.readBits(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test reading a count greater than MAXIMUM_CACHE_SIZE
        try {
            bitInputStream.readBits(64);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testClearBitCache() throws IOException {
        // Test clearing the bit cache after reading some bits
        bitInputStream.readBits(1);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCachedSize);
        assertEquals(0, bitInputStream.bitsCached);

        // Test clearing the bit cache before reading any bits
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCachedSize);
        assertEquals(0, bitInputStream.bitsCached);
    }

    @Test
    public void testByteOrderLittleEndian() throws IOException {
        BitInputStream bitInputStreamLittleEndian = new BitInputStream(new ByteArrayInputStream(TEST_DATA), ByteOrder.LITTLE_ENDIAN);

        // Test reading the same bits with different orders
        BitInputStream bitInputStreamBigEndian = new BitInputStream(new ByteArrayInputStream(TEST_DATA), ByteOrder.BIG_ENDIAN);

        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            long bitLittleEndian = bitInputStreamLittleEndian.readBits(8);
            long bitBigEndian = bitInputStreamBigEndian.readBits(8);
            assertTrue("Byte order should be the same for the same data: " + TEST_DATA[i] + ".", (bitLittleEndian & 0xFF) == bitBigEndian);
        }

        assertEquals(-1L, bitInputStreamLittleEndian.readBits(1));
        assertEquals(-1L, bitInputStreamBigEndian.readBits(1));
    }

    @Test
    public void testByteOrderBigEndian() throws IOException {
        BitInputStream bitInputStreamBigEndian = new BitInputStream(new ByteArrayInputStream(TEST_DATA), ByteOrder.BIG_ENDIAN);

        // Test reading the same bits with different orders
        BitInputStream bitInputStreamLittleEndian = new BitInputStream(new ByteArrayInputStream(TEST_DATA), ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            long bitLittleEndian = bitInputStreamLittleEndian.readBits(8);
            long bitBigEndian = bitInputStreamBigEndian.readBits(8);
            assertNotEquals((bitLittleEndian >> 24) & 0xFF, bitBigEndian);
            assertNotEquals((bitLittleEndian >> 16) & 0xFF, bitBigEndian >> 8);
            assertNotEquals((bitLittleEndian >> 8) & 0xFF, bitBigEndian);
            assertEquals(bitLittleEndian, (bitBigEndian << 24) | (bitBigEndian << 16) | (bitBigEndian << 8) | bitBigEndian);
        }

        assertEquals(-1L, bitInputStreamBigEndian.readBits(1));
        assertEquals(-1L, bitInputStreamLittleEndian.readBits(1));
    }
}