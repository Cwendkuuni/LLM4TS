package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitInputStreamTest {

    @Test
    public void testBitInputStreamWithValidArgs() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(in, byteOrder);
        long expected = 0b0000000111111111;
        long actual;

        actual = bitInputStream.readBits(6);
        assertEquals(expected, actual);
        assertEquals(0, bitInputStream.bitsCachedSize);

        expected = 0b1111000000111111;
        actual = bitInputStream.readBits(10);
        assertEquals(expected, actual);
        assertEquals(8, bitInputStream.bitsCachedSize);

        expected = 0b11111111;
        actual = bitInputStream.readBits(32);
        assertEquals(expected, actual);
        assertEquals(32, bitInputStream.bitsCachedSize);

        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testBitInputStreamWithInvalidArgs() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{0x01});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(in, byteOrder);

        try {
            bitInputStream.readBits(-1);
            assertFalse("Expected IOException for negative count");
        } catch (IllegalArgumentException ex) {
            // expected
        }

        try {
            bitInputStream.readBits(64);
            assertFalse("Expected IOException for count greater than MAXIMUM_CACHE_SIZE");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    @Test
    public void testBitInputStreamWithEndOfStream() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(in, byteOrder);

        long actual = bitInputStream.readBits(12);
        assertEquals(-1, actual);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testBitInputStreamWithByteOrderSwap() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04});
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(in, byteOrder);

        long expected = 0b1111111100000000;
        long actual = bitInputStream.readBits(16);
        assertEquals(expected, actual);
        assertEquals(8, bitInputStream.bitsCachedSize);
    }
}