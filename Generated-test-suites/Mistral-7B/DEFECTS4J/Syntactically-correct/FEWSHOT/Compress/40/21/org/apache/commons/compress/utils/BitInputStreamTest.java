package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;

import org.junit.Test;

public class BitInputStreamTest {

    @Test
    public void testReadBits_ValidData() throws IOException {
        final InputStream inputStream = mock(InputStream.class);
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        when(inputStream.read()).thenReturn((byte) 0xAB);
        when(inputStream.read()).thenReturn((byte) 0xCD);
        when(inputStream.read()).thenReturn((byte) 0xEF);

        final int bitsToRead = 6;
        final long expectedBits = 0b01101101_00111100; // binary representation of "AB CD"

        assertEquals(expectedBits, bitInputStream.readBits(bitsToRead));
    }

    @Test
    public void testReadBits_EndOfStream() throws IOException {
        final InputStream inputStream = mock(InputStream.class);
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        when(inputStream.read()).thenReturn((byte) 0xAB);
        when(inputStream.read()).thenReturn((byte) 0xCD);
        when(inputStream.read()).thenReturn((byte) -1);

        final int bitsToRead = 6;
        final long expectedBits = 0b01101101_00111100; // binary representation of "AB CD"

        assertEquals(expectedBits, bitInputStream.readBits(bitsToRead));

        // Check that the underlying stream has been closed
        bitInputStream.close();
        when(inputStream.read()).thenThrow(IOException.class);
        assertThrows(IOException.class, () -> bitInputStream.readBits(bitsToRead));
    }

    @Test
    public void testReadBits_InvalidCount() throws IOException {
        final InputStream inputStream = mock(InputStream.class);
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        when(inputStream.read()).thenReturn((byte) 0xAB);

        final int bitsToRead = -1;
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(bitsToRead));

        final int maxCacheSize = BitInputStream.MAXIMUM_CACHE_SIZE;
        final int bitsToRead2 = maxCacheSize + 1;
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(bitsToRead2));
    }

    @Test
    public void testClearBitCache() throws IOException {
        final InputStream inputStream = mock(InputStream.class);
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        when(inputStream.read()).thenReturn((byte) 0xAB);
        when(inputStream.read()).thenReturn((byte) 0xCD);
        when(inputStream.read()).thenReturn((byte) 0xEF);

        bitInputStream.readBits(2);
        bitInputStream.clearBitCache();

        final int bitsToRead = 6;
        assertEquals(0, bitInputStream.readBits(bitsToRead));
    }

    @Test
    public void testReadBits_MultipleCalls() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{0xAB, 0xCD, 0xEF});
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        final int bitsToRead = 3;
        final long expectedBits1 = 0b110;
        final long expectedBits2 = 0b011;

        assertEquals(expectedBits1, bitInputStream.readBits(bitsToRead));
        assertEquals(expectedBits2, bitInputStream.readBits(bitsToRead));
    }

    @Test
    public void testReadBits_BigEndian() throws IOException {
        final InputStream inputStream = mock(InputStream.class);
        final ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        when(inputStream.read()).thenReturn((byte) 0xCD);
        when(inputStream.read()).thenReturn((byte) 0xAB);

        final int bitsToRead = 6;
        final long expectedBits = 0b11011010_11001100; // binary representation of "CD AB"

        assertEquals(expectedBits, bitInputStream.readBits(bitsToRead));
    }
}