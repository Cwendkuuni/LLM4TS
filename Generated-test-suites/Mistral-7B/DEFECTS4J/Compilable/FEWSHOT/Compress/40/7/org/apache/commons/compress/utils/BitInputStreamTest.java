package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteOrder;

import org.junit.Test;

public class BitInputStreamTest {

    @Test
    public void testBitInputStreamConstructor() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertNotNull(bitInputStream);
    }

    @Test
    public void testBitInputStreamClose() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.close();
        assertThrows(IOException.class, () -> bitInputStream.readBits(1));
    }

    @Test
    public void testClearBitCache() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long bits = bitInputStream.readBits(4);
        bitInputStream.clearBitCache();
        assertEquals(-1, bitInputStream.readBits(4));
    }

    @Test
    public void testNegativeCount() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-1));
    }

    @Test
    public void testReadBitsLessThanSixteen() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0xF0 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long bits = bitInputStream.readBits(4);
        assertEquals(0x0F, bits);
    }

    @Test
    public void testReadBitsSixteen() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0xF0 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long bits = bitInputStream.readBits(16);
        assertEquals(0xFFFF, bits);
    }

    @Test
    public void testReadBitsSixteenWithByteOrderBigEndian() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x0F, (byte) 0xF0 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        long bits = bitInputStream.readBits(16);
        assertEquals(0xFFFF, bits);
    }

    @Test
    public void testReadBitsSixteenAfterReadingSixteenBits() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0xF0, (byte) 0x0F });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long bits1 = bitInputStream.readBits(16);
        long bits2 = bitInputStream.readBits(16);
        assertEquals(0xFFFF, bits1);
        assertEquals(0xFFFF, bits2);
    }

    @Test
    public void testReadBitsMoreThanSixteenButLessThanSixtyFour() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long bits = bitInputStream.readBits(24);
        assertEquals(0x01_02_03, bits);
    }

    @Test
    public void testReadBitsMoreThanSixtyFour() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));
    }

    @Test
    public void testReadBitsWithEndOfStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, (byte) 0xFF });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long bits1 = bitInputStream.readBits(16);
        long bits2 = bitInputStream.readBits(16);
        assertEquals(0xFFFF, bits1);
        assertEquals(-1, bits2);
    }

    @Test
    public void testReadBitsFromDifferentBytes() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long bits = bitInputStream.readBits(1) | (bitInputStream.readBits(1) << 1) | (bitInputStream.readBits(1) << 2) | (bitInputStream.readBits(1) << 3);
        assertEquals(0x08, bits);
    }

    @Test
    public void testReadBitsFromDifferentBytesWithByteOrderBigEndian() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0x05, 0x04, 0x03, 0x02, 0x01 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        long bits = bitInputStream.readBits(1) | (bitInputStream.readBits(1) << 1) | (bitInputStream.readBits(1) << 2) | (bitInputStream.readBits(1) << 3);
        assertEquals(0x08, bits);
    }
}