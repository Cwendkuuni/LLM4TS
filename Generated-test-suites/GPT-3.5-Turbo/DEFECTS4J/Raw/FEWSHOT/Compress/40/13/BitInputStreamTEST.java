package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {
    @Test
    public void testConstructor() {
        InputStream in = mock(InputStream.class);
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(in, byteOrder);

        assertEquals(in, bitInputStream.in);
        assertEquals(byteOrder, bitInputStream.byteOrder);
    }

    @Test
    public void testClose() throws IOException {
        InputStream in = mock(InputStream.class);
        BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);

        bitInputStream.close();

        verify(in).close();
    }

    @Test
    public void testClearBitCache() {
        BitInputStream bitInputStream = new BitInputStream(null, null);
        bitInputStream.bitsCached = 123;
        bitInputStream.bitsCachedSize = 5;

        bitInputStream.clearBitCache();

        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(null, null);

        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsCountGreaterThanMaximumCacheSize() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(null, null);

        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsBigEndianOrder() throws IOException {
        InputStream in = mock(InputStream.class);
        when(in.read()).thenReturn(1);
        BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
        bitInputStream.bitsCached = 0;
        bitInputStream.bitsCachedSize = 0;

        long result = bitInputStream.readBits(8);

        assertEquals(1, result);
        assertEquals(1, bitInputStream.bitsCached);
        assertEquals(8, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsLittleEndianOrder() throws IOException {
        InputStream in = mock(InputStream.class);
        when(in.read()).thenReturn(1);
        BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.bitsCached = 0;
        bitInputStream.bitsCachedSize = 0;

        long result = bitInputStream.readBits(8);

        assertEquals(1, result);
        assertEquals(1, bitInputStream.bitsCached);
        assertEquals(8, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        InputStream in = mock(InputStream.class);
        when(in.read()).thenReturn(-1);
        BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
        bitInputStream.bitsCached = 0;
        bitInputStream.bitsCachedSize = 0;

        long result = bitInputStream.readBits(8);

        assertEquals(-1, result);
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }
}