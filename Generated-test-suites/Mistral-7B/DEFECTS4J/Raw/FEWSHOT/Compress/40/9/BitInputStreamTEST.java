package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitInputStreamTest {
    private InputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        inputStream = mock(InputStream.class);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testConstructor() {
        assertNull(bitInputStream.in);
        assertEquals(ByteOrder.LITTLE_ENDIAN, bitInputStream.byteOrder);
        assertFalse(bitInputStream.hasBitsCached());
    }

    @Test
    public void testReadBitsWithValidCount() throws IOException {
        when(inputStream.read()).thenReturn((byte) 0x42);

        long bits = bitInputStream.readBits(5);
        assertEquals(0b101010, bits);
        assertEquals(3, bitInputStream.bitsCachedSize);

        bits = bitInputStream.readBits(13);
        assertEquals(0b11111111111010, bits);
        assertEquals(0, bitInputStream.bitsCachedSize);

        verify(inputStream).read();
        verify(inputStream).read();
    }

    @Test
    public void testReadBitsWithInvalidCount() {
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-1));
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));
    }

    @Test
    public void testReadBitsAtEndOfStream() throws IOException {
        when(inputStream.read()).thenReturn((byte) -1);

        long bits = bitInputStream.readBits(5);
        assertEquals(-1, bits);
        assertEquals(0, bitInputStream.bitsCachedSize);

        verify(inputStream, Mockito.times(1)).read();
    }

    @Test
    public void testClearBitCache() throws IOException {
        when(inputStream.read()).thenReturn((byte) 0x42);
        bitInputStream.readBits(5);

        bitInputStream.clearBitCache();

        assertTrue(bitInputStream.hasBitsCached());
        assertEquals(0, bitInputStream.bitsCachedSize);

        verify(inputStream).read();
        verify(inputStream).read();
    }

    @Test
    public void testReadBitsWithDifferentByteOrders() throws IOException {
        InputStream inputStreamBigEndian = mock(InputStream.class);
        BitInputStream bitInputStreamBigEndian = new BitInputStream(inputStreamBigEndian, ByteOrder.BIG_ENDIAN);
        when(inputStream.read()).thenReturn((byte) 0x42);

        long bits = bitInputStream.readBits(5);
        assertEquals(0b011010, bits);
        assertEquals(3, bitInputStream.bitsCachedSize);

        verify(inputStream).read();
        verify(inputStream).read();

        long bitsBigEndian = bitInputStreamBigEndian.readBits(5);
        assertEquals(0b101010, bitsBigEndian);
        assertEquals(3, bitInputStreamBigEndian.bitsCachedSize);
    }

    @Test
    public void testReadBitsWithCustomInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[]{(byte) 0x42});
        BitInputStream bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN);

        long bits = bitInputStream.readBits(5);
        assertEquals(0b101010, bits);
        assertEquals(3, bitInputStream.bitsCachedSize);

        verify(byteArrayInputStream, Mockito.times(1)).read();
        verifyNoMoreInteractions(byteArrayInputStream);
    }

    @Test
    public void testClearBitCacheAfterReadBits() throws IOException {
        when(inputStream.read()).thenReturn((byte) 0x42);
        bitInputStream.readBits(5);

        bitInputStream.clearBitCache();

        assertFalse(bitInputStream.hasBitsCached());
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testHasBitsCached() {
        assertFalse(bitInputStream.hasBitsCached());
        bitInputStream.readBits(5);
        assertTrue(bitInputStream.hasBitsCached());
        bitInputStream.clearBitCache();
        assertFalse(bitInputStream.hasBitsCached());
    }

    @Test
    public void testReadBitsWithDifferentInputStreams() throws IOException {
        InputStream inputStream1 = mock(InputStream.class);
        InputStream inputStream2 = mock(InputStream.class);
        BitInputStream bitInputStream1 = new BitInputStream(inputStream1, ByteOrder.LITTLE_ENDIAN);
        BitInputStream bitInputStream2 = new BitInputStream(inputStream2, ByteOrder.LITTLE_ENDIAN);

        when(inputStream1.read()).thenReturn((byte) 0x42);
        when(inputStream2.read()).thenReturn((byte) 0x43);

        long bits1 = bitInputStream1.readBits(5);
        assertEquals(0b101010, bits1);
        assertEquals(3, bitInputStream1.bitsCachedSize);

        long bits2 = bitInputStream2.readBits(5);
        assertEquals(0b111011, bits2);
        assertEquals(3, bitInputStream2.bitsCachedSize);

        verify(inputStream1).read();
        verify(inputStream1).read();

        verify(inputStream2).read();
        verify(inputStream2).read();
    }
}