package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {

    private InputStream inputStream;
    private ByteOrder byteOrder;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        inputStream = mock(InputStream.class);
        byteOrder = ByteOrder.LITTLE_ENDIAN;
        bitInputStream = new BitInputStream(inputStream, byteOrder);
    }

    @Test
    public void testReadBitsWithValidCount() throws IOException {
        when(inputStream.read()).thenReturn((byte) 0x1a);

        long bits = bitInputStream.readBits(5);
        assertEquals(0x20, bits);

        bits = bitInputStream.readBits(3);
        assertEquals(0x0e, bits);

        bits = bitInputStream.readBits(2);
        assertEquals(0x03, bits);

        bits = bitInputStream.readBits(1);
        assertEquals(0x01, bits);

        bits = bitInputStream.readBits(63);
        assertEquals(-1, bits);
    }

    @Test
    public void testReadBitsWithInvalidCount() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-1));
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(65));
    }

    @Test
    public void testReadBitsWithEndOfStream() throws IOException {
        when(inputStream.read()).thenReturn((byte) 0x1a).thenReturn((byte) -1);

        long bits = bitInputStream.readBits(5);
        assertEquals(0x20, bits);

        bits = bitInputStream.readBits(3);
        assertEquals(0x0e, bits);

        bits = bitInputStream.readBits(1);
        assertEquals(0x01, bits);

        bits = bitInputStream.readBits(1);
        assertEquals(-1, bits);
    }

    @Test
    public void testClearBitCache() throws IOException {
        when(inputStream.read()).thenReturn((byte) 0x1a).thenReturn((byte) -1);

        bitInputStream.readBits(5);
        bitInputStream.readBits(3);
        bitInputStream.readBits(1);
        bitInputStream.clearBitCache();

        long bits = bitInputStream.readBits(5);
        assertEquals(-1, bits);
    }

    @Test
    public void testReadBitsWithByteOrderLittleEndian() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1a});

        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        long bits = bitInputStream.readBits(5);
        assertEquals(0x1020, bits);

        bits = bitInputStream.readBits(3);
        assertEquals(0x011, bits);

        bits = bitInputStream.readBits(2);
        assertEquals(0x0011, bits);

        bits = bitInputStream.readBits(1);
        assertEquals(0x00000001, bits);

        bits = bitInputStream.readBits(63);
        assertEquals(-1, bits);
    }

    @Test
    public void testReadBitsWithByteOrderBigEndian() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1a});

        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        long bits = bitInputStream.readBits(5);
        assertEquals(0x2010, bits);

        bits = bitInputStream.readBits(3);
        assertEquals(0x0e01, bits);

        bits = bitInputStream.readBits(2);
        assertEquals(0x0300, bits);

        bits = bitInputStream.readBits(1);
        assertEquals(0x00010000, bits);

        bits = bitInputStream.readBits(63);
        assertEquals(-1, bits);
    }

    @Test
    public void testReadBitsWithEndianSwap() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1a});

        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        long bits = bitInputStream.readBits(5);
        assertEquals(0x1020, bits);

        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        bits = bitInputStream.readBits(5);
        assertEquals(0x2010, bits);
    }

    @Test
    public void testBitInputStreamWithEmptyInput() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[0]);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long bits = bitInputStream.readBits(5);
        assertEquals(-1, bits);
    }
}