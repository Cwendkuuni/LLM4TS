package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEqual;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class BitInputStreamTest {

    @Test
    public void testReadBits_SmallCount() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        when(inputStream.read()).thenReturn(0x2A);

        long bits = bitInputStream.readBits(1);
        assertEquals(0x01 & 0x2A, bits);

        bits = bitInputStream.readBits(3);
        assertEquals(0x0E & 0x2A, bits);

        bits = bitInputStream.readBits(4);
        assertEquals(0x1C & 0x2A, bits);

        bits = bitInputStream.readBits(5);
        assertEquals(0x30 & 0x2A, bits);

        bits = bitInputStream.readBits(6);
        assertEquals(0x40 & 0x2A, bits);

        bits = bitInputStream.readBits(7);
        assertEquals(0x80 & 0x2A, bits);
    }

    @Test
    public void testReadBits_BigCount() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        when(inputStream.read()).thenReturn(0x2A).thenReturn(0x0B).thenReturn(0xFF);

        long bits = bitInputStream.readBits(8);
        assertEquals(0x2A, bits);

        bits = bitInputStream.readBits(9);
        assertEquals(0x2A0B, bits);

        bits = bitInputStream.readBits(10);
        assertEquals(0x2A0BFF, bits);

        bits = bitInputStream.readBits(11);
        assertEquals(0x2A0BFF00 | 0x000002A0, bits);

        bits = bitInputStream.readBits(12);
        assertEquals(0x2A0BFF00 | 0x000012A0, bits);

        bits = bitInputStream.readBits(13);
        assertEquals(0x2A0BFF00 | 0x000012A0 | 0x00008000, bits);

        bits = bitInputStream.readBits(14);
        assertEquals(0x2A0BFF00 | 0x000012A0 | 0x00010000, bits);

        bits = bitInputStream.readBits(15);
        assertEquals(0x2A0BFF00 | 0x00010000 | 0x00018000, bits);

        bitInputStream.readBits(16); // should not throw an exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_NegativeCount() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        when(inputStream.read()).thenReturn(0x2A);

        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_BiggerThanMaximumCacheSize() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        when(inputStream.read()).thenReturn(0x2A);

        bitInputStream.readBits(64);
    }

    @Test
    public void testClearBitCache() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        when(inputStream.read()).thenReturn(0x2A);

        bitInputStream.readBits(1);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(1));
    }
}