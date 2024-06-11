package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test cases for the BitInputStream class.
 */
public class BitInputStreamTest {

    @Test
    public void testReadBitsWithLittleEndian() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1A, (byte) 0x2B, (byte) 0x3C, (byte) 0x4D});
        try (BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder)) {
            long expected = 0b11111111_11111111_11111111_11111111_11110000_11110000_11110000_11110010; // 0x4D 2B 1A 1C
            for (int i = 0; i < 64; i += 8) {
                long readBits = bitInputStream.readBits(8);
                Assert.assertEquals(expected >> i & 0xFFFF_FFFF_FFFF_FFFFL, readBits);
            }
        }
    }

    @Test
    public void testReadBitsWithBigEndian() throws IOException {
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1A, (byte) 0x2B, (byte) 0x3C, (byte) 0x4D});
        try (BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder)) {
            long expected = 0b11111111_11111111_11111111_11111101_11110010_11110000_11110000_11110000L; // 4D 2B 1A 1C
            for (int i = 0; i < 64; i += 8) {
                long readBits = bitInputStream.readBits(8);
                Assert.assertEquals(expected >> i & 0xFFFF_FFFF_FFFF_FFFFL, readBits);
            }
        }
    }

    @Test
    public void testEndOfStream() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try (BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder)) {
            Assert.assertEquals(-1, bitInputStream.readBits(8));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x1A});
        try (BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder)) {
            bitInputStream.readBits(-1);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCountBiggerThanMaximum() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x1A});
        try (BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder)) {
            bitInputStream.readBits(64);
        }
    }

    @Test
    public void testClearBitCache() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x1A});
        try (BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder)) {
            bitInputStream.clearBitCache();
            long readBits = bitInputStream.readBits(4);
            Assert.assertEquals(0, readBits);
        }
    }
}