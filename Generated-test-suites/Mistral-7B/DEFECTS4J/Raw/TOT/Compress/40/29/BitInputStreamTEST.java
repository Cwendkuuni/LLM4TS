package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    private InputStream inputStream;

    @Before
    public void setUp() {
        inputStream = new ByteArrayInputStream(new byte[]{(byte) 0b11111111, (byte) 0b00111111, (byte) 0b11000000, (byte) 0b00001111});
        new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        inputStream.close();
    }

    /**
     * Test readBits with correct count.
     */
    @Test
    public void testReadBitsCorrectCount() throws IOException {
        // Test reading exact number of bits
        long result = bitInputStream.readBits(8);
        assertEquals(0b11111111, result);
        result = bitInputStream.readBits(4);
        assertEquals(0b0111, result);
        result = bitInputStream.readBits(3);
        assertEquals(0b111, result);
    }

    /**
     * Test readBits with incorrect count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsIncorrectCount() throws IOException {
        // Test reading a negative or larger number of bits
        bitInputStream.readBits(-1);
        bitInputStream.readBits(64);
    }

    /**
     * Test clearBitCache.
     */
    @Test
    public void testClearBitCache() throws IOException {
        // Test clearing the bit cache
        long result = bitInputStream.readBits(8);
        assertEquals(0b11111111, result);
        bitInputStream.clearBitCache();
        result = bitInputStream.readBits(8);
        assertEquals(0, result);
    }

    /**
     * Test end of the underlying stream.
     */
    @Test(expected = IOException.class)
    public void testEndOfUnderlyingStream() throws IOException {
        // Test reading bits from an empty input stream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        new BitInputStream(emptyInputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(1);
    }

    /**
     * Test reading bits in big-endian order.
     */
    @Test
    public void testReadBitsBigEndian() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[]{(byte) 0b00111111, (byte) 0b11111111, (byte) 0b11000000, (byte) 0b00001111});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        long result = bitInputStream.readBits(8);
        assertEquals(0b11111111, result);
        result = bitInputStream.readBits(4);
        assertEquals(0b1111, result);
        result = bitInputStream.readBits(3);
        assertEquals(0b111, result);
    }
}