package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;

public class BitInputStreamTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testReadBits_ValidCount() throws IOException {
        final byte[] testData = new byte[]{(byte) 0b11111111, (byte) 0b01111111};
        final InputStream inputStream = new ByteArrayInputStream(testData);
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        assertEquals(1L, bitInputStream.readBits(1));
        assertEquals(3L, bitInputStream.readBits(3));
        assertEquals(0b111101101111L, bitInputStream.readBits(11));
        assertEquals(0xFFFF_FF80L, bitInputStream.readBits(16));

        bitInputStream.close();
    }

    @Test
    public void testReadBits_InvalidCount() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0xFF});
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("count must not be negative or greater than 63");

        bitInputStream.readBits(-1);
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBits_EndOfStream() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{});
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        assertEquals(-1L, bitInputStream.readBits(1));
        bitInputStream.close();
    }

    @Test
    public void testClearBitCache() throws IOException {
        final byte[] testData = new byte[]{(byte) 0b11111111};
        final InputStream inputStream = new ByteArrayInputStream(testData);
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        bitInputStream.readBits(8);
        bitInputStream.clearBitCache();

        assertEquals(0L, bitInputStream.readBits(8));

        bitInputStream.close();
    }
}