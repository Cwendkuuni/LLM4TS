package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

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
        byte[] testData = new byte[]{(byte) 0x81, (byte) 0x7E};
        inputStream = new ByteArrayInputStream(testData);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
    }

    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.clearBitCache();
        assertEquals(-1, bitInputStream.readBits(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        bitInputStream.readBits(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsGreaterThanMaximumCacheSize() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsSmallCount() throws IOException {
        long expected = 0x01;
        long actual = bitInputStream.readBits(1);
        assertEquals(expected, actual);
    }

    @Test
    public void testReadBitsMediumCount() throws IOException {
        long expected = 0x0100;
        long actual = bitInputStream.readBits(7);
        assertEquals(expected, actual);
    }

    @Test
    public void testReadBitsLargeCount() throws IOException {
        long expected = 0x01010101_0101_0101L;
        long actual = bitInputStream.readBits(13 + 16 + 13);
        assertEquals(expected, actual);
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        long expected = -1;
        long actual = bitInputStream.readBits(1);
        assertEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testReadBitsWithIOException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.read();
        assertThrows(IOException.class, () -> bitInputStream.readBits(1));
    }
}