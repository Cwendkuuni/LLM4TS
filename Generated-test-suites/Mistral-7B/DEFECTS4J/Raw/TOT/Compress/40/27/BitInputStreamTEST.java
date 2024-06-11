package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test cases for the BitInputStream class.
 */
public class BitInputStreamTest {

    private BitInputStream bitInputStream;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
        outputStream.close();
    }

    /**
     * Test reading the minimum number of bits (1) successfully.
     */
    @Test
    public void testReadOneBit() throws IOException {
        assertEquals(1, bitInputStream.readBits(1));
    }

    /**
     * Test reading a single bit from empty input stream.
     */
    @Test(expected = IOException.class)
    public void testReadOneBitFromEmptyStream() throws IOException {
        inputStream.close();
        new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(1);
    }

    /**
     * Test reading multiple bits from a single byte.
     */
    @Test
    public void testReadMultipleBitsFromOneByte() throws IOException {
        writeByteToOutputStream(0b11110110);
        assertEquals(14, bitInputStream.readBits(4));
    }

    /**
     * Test reading multiple bits from multiple bytes.
     */
    @Test
    public void testReadMultipleBitsFromMultipleBytes() throws IOException {
        writeByteToOutputStream(0b11110110);
        writeByteToOutputStream(0b11111111);

        assertEquals(222, bitInputStream.readBits(8));
        assertEquals(14, bitInputStream.readBits(4));
        assertEquals(247, bitInputStream.readBits(7));
    }

    /**
     * Test reading more bits than available.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadMoreBitsThanAvailable() throws IOException {
        writeByteToOutputStream(0b11110110);
        bitInputStream.readBits(9);
    }

    /**
     * Test reading bits from a big-endian stream.
     */
    @Test
    public void testReadBitsFromBigEndianStream() throws IOException {
        writeByteToOutputStream((byte) 0x01);
        writeByteToOutputStream((byte) 0x23);

        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        assertEquals(0x01000000 | 0x23, bitInputStream.readBits(16));
    }

    /**
     * Test clearing the cache.
     */
    @Test
    public void testClearBitCache() throws IOException {
        writeByteToOutputStream(0b11110110);
        bitInputStream.readBits(4);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(1));
    }

    /**
     * Test reading zero bits.
     */
    @Test
    public void testReadZeroBits() throws IOException {
        assertEquals(0, bitInputStream.readBits(0));
    }

    /**
     * Test reading from an empty stream.
     */
    @Test(expected = IOException.class)
    public void testReadFromEmptyStream() throws IOException {
        inputStream.close();
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(1);
    }

    /**
     * Test reading negative number of bits.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadNegativeNumberOfBits() throws IOException {
        bitInputStream.readBits(-1);
    }

    private void writeByteToOutputStream(int value) throws IOException {
        outputStream.write(value);
        outputStream.flush();
    }
}