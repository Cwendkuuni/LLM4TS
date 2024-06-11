package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    private static final int TEST_DATA_SIZE = 10;
    private static final byte[] TEST_DATA = new byte[] { 0x01, 0x2A, 0x3B, 0x4C, 0x5D };
    private BitInputStream inputStream;

    @Before
    public void setUp() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_DATA);
        inputStream = new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testConstructor() {
        BitInputStream bitInputStream = new BitInputStream(null, ByteOrder.LITTLE_ENDIAN);
        assertNull(bitInputStream);
    }

    @Test
    public void testClose() throws IOException {
        inputStream.close();
        assertFalse(inputStream.isOpen());
    }

    @Test
    public void testClearBitCache() throws IOException {
        inputStream.clearBitCache();
        assertEquals(0, inputStream.bitsCached);
        assertEquals(0, inputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsWithValidCountAndValidInputStream() throws IOException {
        long expectedData = 0x012A3B;
        long bitsRead = 0;

        for (int i = 0; i < TEST_DATA_SIZE * 8; i += 8) {
            bitsRead |= inputStream.readBits(8);
        }

        assertEquals(expectedData, bitsRead);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithInvalidCount() throws IOException {
        inputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountBiggerThanMaximum() throws IOException {
        inputStream.readBits(64);
    }

    @Test
    public void testReadBitsWithEndOfStreamReached() throws IOException {
        long expectedData = 0x012A3B;
        long bitsRead = 0;

        for (int i = 0; i < TEST_DATA_SIZE * 8 - 1; i += 8) {
            bitsRead |= inputStream.readBits(8);
        }
        assertEquals(-1, inputStream.readBits(8));
        assertEquals(expectedData, bitsRead << 8);
    }
}