package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;

public class BitInputStreamTest {

    private InputStream inputStream;
    private ByteOrder byteOrder;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() throws IOException {
        byte[] testData = new byte[]{(byte) 0x01, (byte) 0x2A, (byte) 0x3F, (byte) 0x55};
        inputStream = new ByteArrayInputStream(testData);
        byteOrder = ByteOrder.LITTLE_ENDIAN;
        bitInputStream = new BitInputStream(inputStream, byteOrder);
    }

    @Test
    public void testReadBitsWithValidCount() throws IOException {
        long expectedBits = 0x012A3F55;
        for (int i = 0; i <= 63; i += 8) {
            long readBits = bitInputStream.readBits(i);
            assertEquals(expectedBits & MASKS[i], readBits);
            expectedBits >>= i;
        }
    }

    @Test
    public void testReadBitsWithValidCountButLessThan8Bits() throws IOException {
        for (int i = 0; i < 64; i++) {
            long readBits = bitInputStream.readBits(i);
            if (i < 8) {
                assertEquals(testData[0] >>> (7 - i) & 0x01, readBits & MASKS[i]);
            } else {
                assertEquals(bitInputStream.readBits(8), readBits >> (i - 8) & MASKS[i]);
            }
        }
    }

    @Test
    public void testReadBitsWithInvalidCountNegative() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-1));
    }

    @Test
    public void testReadBitsWithInvalidCountGreaterThan63() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));
    }

    @Test
    public void testClearBitCache() throws IOException {
        long readBits = bitInputStream.readBits(8);
        bitInputStream.clearBitCache();
        long readBitsAfterClear = bitInputStream.readBits(8);
        assertEquals(readBits & MASKS[8], readBitsAfterClear);
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
        assertThrows(IOException.class, () -> bitInputStream.readBits(8));
    }

    @Test
    public void testReadBitsWithDifferentByteOrder() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteOrder bigEndianByteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bigEndianBitInputStream = new BitInputStream(new ByteArrayInputStream(testData), bigEndianByteOrder);
        for (int i = 0; i <= 63; i += 8) {
            long readBits = bigEndianBitInputStream.readBits(i);
            if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                assertNotEquals(readBits, bitInputStream.readBits(i));
            } else {
                assertNotEquals(readBits, bigEndianBitInputStream.readBits(i ^ 32));
            }
        }
    }
}