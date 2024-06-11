package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Test;

public class BitInputStreamTest {

    @Test
    public void testBitInputStreamConstruction() {
        InputStream inputStreamMock = mock(InputStream.class);
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(inputStreamMock, byteOrder);

        assertNotNull(bitInputStream);
        assertEquals(inputStreamMock, bitInputStream.in);
        assertEquals(byteOrder, bitInputStream.byteOrder);
    }

    @Test
    public void testClearBitCache() throws IOException {
        InputStream inputStreamMock = mock(InputStream.class);
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStreamMock, byteOrder);

        bitInputStream.clearBitCache();

        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBits() throws IOException {
        InputStream inputStreamMock = mock(InputStream.class);
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStreamMock, byteOrder);

        when(inputStreamMock.read()).thenReturn('a', 'b', -1);

        // Test reading less bits than the available ones
        assertEquals(0b11111111L, bitInputStream.readBits(7));

        // Test reading all available bits
        assertEquals(0xFFL, bitInputStream.readBits(8));

        // Test reading more bits than the available ones
        assertEquals(-1L, bitInputStream.readBits(9));

        // Test reading all bits and checking the end of the stream
        assertNull(bitInputStream.readBits(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        InputStream inputStreamMock = mock(InputStream.class);
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStreamMock, byteOrder);

        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsCountGreaterThanMaximum() throws IOException {
        InputStream inputStreamMock = mock(InputStream.class);
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStreamMock, byteOrder);

        bitInputStream.readBits(64);
    }

    @Test
    public void testByteOrderLittleEndian() throws IOException {
        InputStream inputStreamMock = mock(InputStream.class);
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStreamMock, byteOrder);

        when(inputStreamMock.read()).thenReturn((byte) 0x1A, (byte) 0x2B);

        assertEquals(0b1010101010111111L, bitInputStream.readBits(12));
    }

    @Test
    public void testByteOrderBigEndian() throws IOException {
        InputStream inputStreamMock = mock(InputStream.class);
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStreamMock, byteOrder);

        when(inputStreamMock.read()).thenReturn((byte) 0x1A, (byte) 0x2B);

        assertEquals(0b1111111101010101L, bitInputStream.readBits(12));
    }

    @Test
    public void testClose() throws IOException {
        InputStream inputStreamMock = mock(InputStream.class);
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStreamMock, byteOrder);

        bitInputStream.close();

        verify(inputStreamMock).close();
    }
}