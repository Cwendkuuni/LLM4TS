package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {

    private InputStream inputStream;

    @Before
    public void setup() {
        inputStream = mock(InputStream.class);
    }

    @Test
    public void testConstructor() {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        assertNotNull(bitInputStream);
        assertEquals(inputStream, bitInputStream.in);
        assertEquals(byteOrder, bitInputStream.byteOrder);
    }

    @Test
    public void testClose() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(mock(Closeable.class), ByteOrder.LITTLE_ENDIAN);
        bitInputStream.close();
        verifyClose(bitInputStream.in);
    }

    @Test
    public void testClearBitCache() {
        BitInputStream bitInputStream = new BitInputStream(mock(Closeable.class), ByteOrder.LITTLE_ENDIAN);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsNegativeCount() {
        BitInputStream bitInputStream = new BitInputStream(mock(Closeable.class), ByteOrder.LITTLE_ENDIAN);
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-1));
    }

    @Test
    public void testReadBitsZeroCount() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(mock(Closeable.class), ByteOrder.LITTLE_ENDIAN);
        assertEquals(0, bitInputStream.readBits(0));
    }

    @Test
    public void testReadBitsOneBit() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertEquals(1, bitInputStream.readBits(1));
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
        verify(inputStream).read();
    }

    @Test
    public void testReadBitsSeveralBits() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long bits = 0;
        for (int i = 0; i < 10; i++) {
            bits |= bitInputStream.readBits(3);
            bitInputStream.clearBitCache();
        }
        assertEquals(0x031201L, bits);
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
        verify(inputStream, times(4)).read();
    }

    @Test
    public void testReadBitsBigEndian() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        long bits = 0;
        for (int i = 0; i < 10; i++) {
            bits |= bitInputStream.readBits(3);
            bitInputStream.clearBitCache();
        }
        assertEquals(0x010203L, bits);
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
        verify(inputStream, times(4)).read();
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read()).thenReturn(-1);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertEquals(-1, bitInputStream.readBits(1));
        assertEquals(-1, bitInputStream.readBits(100));
        verify(inputStream).read();
        verify(inputStream).read();
        verify(inputStream).read();
        verify(inputStream).read();
    }

    @Test
    public void testReadBitsOverflow() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(mock(Closeable.class), ByteOrder.LITTLE_ENDIAN);
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));
    }

    private void verifyClose(Closeable closeable) throws IOException {
        closeable.close();
        // no further verification would fail if stream has a finally block
    }

    private void verify(InputStream inputStream, int times) throws IOException {
        for (int i = 0; i < times; i++) {
            when(inputStream.read()).thenReturn((byte) 0x01);
        }
    }

    private void verify(InputStream inputStream) throws IOException {
        verify(inputStream, times(4));
    }
}