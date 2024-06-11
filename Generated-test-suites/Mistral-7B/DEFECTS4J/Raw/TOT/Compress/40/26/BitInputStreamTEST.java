package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Test;

/**
 * Test class for BitInputStream
 */
public class BitInputStreamTest {

    /**
     * Test constructor, input stream and byte order
     */
    @Test
    public void testConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        assertEquals(inputStream, bitInputStream.in);
        assertEquals(ByteOrder.LITTLE_ENDIAN, bitInputStream.byteOrder);
    }

    /**
     * Test close method
     */
    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.close();
        assertThrows(IOException.class, () -> bitInputStream.readBits(1));
    }

    /**
     * Test clearBitCache method
     */
    @Test
    public void testClearBitCache() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCachedSize);
        assertEquals(0, bitInputStream.bitsCached);
    }

    /**
     * Test readBits method with valid count
     */
    @Test
    public void testReadBitsValidCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        long bits = bitInputStream.readBits(3);
        assertEquals(0b00110110, bits); // read first two bits from byte 0 and the first bit from byte 1
        assertEquals(3, bitInputStream.bitsCachedSize);

        bits = bitInputStream.readBits(2);
        assertEquals(0b11, bits); // read next two bits from byte 1
        assertEquals(5, bitInputStream.bitsCachedSize);

        bits = bitInputStream.readBits(1);
        assertEquals(0, bits); // read next bit from byte 2
        assertEquals(6, bitInputStream.bitsCachedSize);

        bitInputStream.close();
    }

    /**
     * Test readBits method with invalid count
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.readBits(64);
    }

    /**
     * Test readBits method with end of stream
     */
    @Test
    public void testReadBitsEndOfStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02 });
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        long bits = bitInputStream.readBits(3);
        assertEquals(0b011, bits); // read first three bits from byte 0
        assertEquals(-1, bitInputStream.readBits(1)); // read next bit from byte 1, end of stream reached
        assertEquals(-1, bitInputStream.readBits(1)); // read next bit from byte 1, end of stream reached
        bitInputStream.close();
    }
}