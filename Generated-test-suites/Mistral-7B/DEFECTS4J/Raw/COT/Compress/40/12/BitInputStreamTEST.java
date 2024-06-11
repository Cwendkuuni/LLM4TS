package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test cases for BitInputStream class
 */
public class BitInputStreamTest {

    @Test
    public void testBitInputStreamConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1A, (byte) 0x2B, (byte) 0x3C});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        assertEquals(ByteOrder.LITTLE_ENDIAN, bitInputStream.byteOrder);
        assertEquals(inputStream, bitInputStream.in);
    }

    @Test
    public void testReadBitsNormalCase() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1A, (byte) 0x2B, (byte) 0x3C});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        assertEquals(0, bitInputStream.readBits(0));
        assertEquals(0x1A >>> 7 & 0x01, bitInputStream.readBits(1)); // read 1 bit from the first byte
        assertEquals(0x1A & 0x7F, bitInputStream.readBits(7)); // read 7 bits from the first byte
        assertEquals(0x2B, bitInputStream.readBits(8)); // read 1 byte (8 bits)
        assertEquals(0x2B >>> 4 & 0x0F, bitInputStream.readBits(4)); // read 4 bits from the second byte
        assertEquals(-1, bitInputStream.readBits(9)); // try to read 1 more bit than available

        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(0));
        assertEquals(0x3C, bitInputStream.readBits(8)); // read 1 byte (8 bits)
    }

    @Test
    public void testReadBitsBigEndian() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1A, (byte) 0x2B, (byte) 0x3C});
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        assertEquals(0, bitInputStream.readBits(0));
        assertEquals(0x3C >>> 24 & 0x01, bitInputStream.readBits(1)); // read 1 bit from the last byte of the first read byte
        assertEquals(0x3C, bitInputStream.readBits(8)); // read 1 byte (8 bits)
        assertEquals(0x2B, bitInputStream.readBits(8)); // read 1 byte (8 bits)
        assertEquals(0x1A, bitInputStream.readBits(8)); // read 1 byte (8 bits)
        assertEquals(-1, bitInputStream.readBits(9)); // try to read 1 more bit than available

        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(0));
        assertEquals(0x1A, bitInputStream.readBits(8)); // read 1 byte (8 bits)
        assertEquals(0x2B, bitInputStream.readBits(8)); // read 1 byte (8 bits)
        assertEquals(0x3C, bitInputStream.readBits(8)); // read 1 byte (8 bits)
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1A, (byte) 0x2B, (byte) 0x3C});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.readBits(-1);
    }

    @Test
    public void testClearBitCache() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1A, (byte) 0x2B, (byte) 0x3C});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        assertEquals(0, bitInputStream.readBits(0));
        assertEquals(0x1A, bitInputStream.readBits(8));
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(0));
    }

    @Test
    public void testCloseInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1A, (byte) 0x2B, (byte) 0x3C});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.close();
        assertThrows(IOException.class, () -> bitInputStream.readBits(0));
    }
}