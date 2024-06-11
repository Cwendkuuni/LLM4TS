package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Test cases for BitInputStream class
 */
public class BitInputStreamTest {

    private BitInputStream bitInputStream;
    private final byte[] data = new byte[]{(byte) 0x01, (byte) 0x2a, (byte) 0x3b, (byte) 0x4c};
    private final ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

    @Before
    public void setUp() {
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testConstructor() {
        assertNotNull(bitInputStream);
    }

    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
        assertFalse(inputStream.isOpen());
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsBiggerThanMaxCount() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsSmallCount() throws IOException {
        assertEquals(1, bitInputStream.readBits(1));
        assertEquals(0x2a & 0x01, bitInputStream.readBits(1)); // first bit of the second byte
        assertEquals(0x3b & 0x02, bitInputStream.readBits(1)); // second bit of the second byte
        assertEquals(0x4c & 0x04, bitInputStream.readBits(2)); // first two bits of the third byte
    }

    @Test
    public void testReadBitsBigCount() throws IOException {
        assertEquals(0x012a3b4cL, bitInputStream.readBits(32)); // read a whole byte
    }

    @Test
    public void testReadBitsAcrossByteBoundariesLittleEndian() throws IOException {
        assertEquals((long) 0x01 << 4 | (long) 0x2a & 0xF0, bitInputStream.readBits(4)); // read 4 bits from the first byte
        assertEquals((long) 0x3b << 2, bitInputStream.readBits(3)); // read 3 bits from the second byte and 1 bit from the third byte
    }

    @Test
    public void testReadBitsAcrossByteBoundariesBigEndian() throws IOException {
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        assertEquals((long) 0x01 << 4 | (long) 0x2a & 0xF0, bitInputStream.readBits(4)); // read 4 bits from the third byte
        assertEquals((long) 0x3b << 2, bitInputStream.readBits(3)); // read 3 bits from the second byte and 1 bit from the first byte
    }
}