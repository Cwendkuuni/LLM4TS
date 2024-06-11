package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitInputStreamTest {

    @Test
    public void testBitInputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        assertNotNull(bitInputStream);
    }

    @Test
    public void testReadBits_countIsZero() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        long result = bitInputStream.readBits(0);
        assertEquals(0L, result);
    }

    @Test
    public void testReadBits_countIsOne() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        long result = bitInputStream.readBits(1);
        assertEquals(0x01L, result);
    }

    @Test
    public void testReadBits_countIsSeveralBitsInOneByte() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x0F, 0x10});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        long result = bitInputStream.readBits(3);
        assertEquals(0x0F000001L & MASKS[3], result);
    }

    @Test
    public void testReadBits_countIsSeveralBytes() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        long result = bitInputStream.readBits(9);
        long expected = 0x123L;
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_countIsNegative() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_countIsGreaterThanMaximumCacheSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.readBits(64);
    }

    @Test
    public void testClearBitCache() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        long readBits = bitInputStream.readBits(1);
        bitInputStream.clearBitCache();
        long readBitsAfterClearCache = bitInputStream.readBits(1);
        assertEquals(0L, readBitsAfterClearCache);
    }
}