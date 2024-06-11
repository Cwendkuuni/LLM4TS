package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import org.junit.Test;

public class BitInputStreamTest {

    @Test
    public void testReadBits_valid_arguments_and_valid_input() throws IOException {
        final InputStream inputStream = mock(InputStream.class);
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        when(inputStream.read()).thenReturn(0x01).thenReturn(0x02).thenReturn(0x03).thenReturn(0xFF);

        final BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        long bits = bitInputStream.readBits(1);
        assertEquals(1, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(0, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(1, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(1, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(0, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(-1, bits);

        // Test reading multiple bits at once
        bits = bitInputStream.readBits(3);
        assertEquals(0x03 & 0x07, bits); // 3 bits can be represented in a single byte

        // Test reading the maximum number of bits (63)
        bits = bitInputStream.readBits(63);
        assertEquals(0xFFFFFFFFFFFFFFFFL, bits);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_negative_count() throws IOException {
        final InputStream inputStream = mock(InputStream.class);
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_count_greater_than_maximum() throws IOException {
        final InputStream inputStream = mock(InputStream.class);
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        bitInputStream.readBits(64);
    }

    @Test
    public void testClearBitCache() throws IOException {
        final InputStream inputStream = mock(InputStream.class);
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        bitInputStream.readBits(1);
        bitInputStream.clearBitCache();

        long bits = bitInputStream.readBits(1);
        assertEquals(-1, bits);
    }

    @Test
    public void testReadBits_end_of_input() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        long bits = bitInputStream.readBits(1);
        assertEquals(1, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(0, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(1, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(1, bits);
        bits = bitInputStream.readBits(1);
        assertEquals(-1, bits);

        bitInputStream.clearBitCache();

        bits = bitInputStream.readBits(1);
        assertEquals(-1, bits);
    }
}