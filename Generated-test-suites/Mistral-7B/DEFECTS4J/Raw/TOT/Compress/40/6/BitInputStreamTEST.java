package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;

/**
 * Test class for the BitInputStream class.
 */
public class BitInputStreamTest {

    /**
     * Test constructor with valid arguments.
     */
    @org.junit.Test
    public void testConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x0f, 0x1a, 0x2b, 0x3c });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertNotNull(bitInputStream);
    }

    /**
     * Test reading bits from the input stream.
     */
    @org.junit.Test
    public void testReadBits() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x0f, 0x1a, 0x2b, 0x3c });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // Read 1 bit
        assertEquals(1L, bitInputStream.readBits(1));

        // Read 3 bits
        assertEquals(5L, bitInputStream.readBits(3));

        // Read 6 bits
        assertEquals(45L, bitInputStream.readBits(6));

        // Read 7 bits
        assertEquals(-1L, bitInputStream.readBits(7));

        // Read all available bits
        assertEquals(0xffffffff0fL, bitInputStream.readBits(24));

        // Clear bit cache
        bitInputStream.clearBitCache();

        // Read 1 bit
        assertEquals(0L, bitInputStream.readBits(1));

        // End of stream
        assertThrows(IOException.class, () -> bitInputStream.readBits(1));
    }

    /**
     * Test reading bits with big-endian byte order.
     */
    @org.junit.Test
    public void testReadBitsBigEndian() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x0f, 0x1a, 0x2b, 0x3c });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Read 1 bit
        assertEquals(1L << 24, bitInputStream.readBits(1));

        // Read 3 bits
        assertEquals((5L << 21) | (1L << 16), bitInputStream.readBits(3));

        // Read 6 bits
        assertEquals((45L << 12) | (1L << 6), bitInputStream.readBits(6));

        // Read 7 bits
        assertEquals(-1L, bitInputStream.readBits(7));

        // Read all available bits
        assertEquals(0xffffffff0fL, bitInputStream.readBits(24));

        // Clear bit cache
        bitInputStream.clearBitCache();

        // Read 1 bit
        assertEquals(0L, bitInputStream.readBits(1));

        // End of stream
        assertThrows(IOException.class, () -> bitInputStream.readBits(1));
    }

    /**
     * Test the clearBitCache method.
     */
    @org.junit.Test
    public void testClearBitCache() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x0f, 0x1a, 0x2b, 0x3c });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        bitInputStream.readBits(8);
        bitInputStream.clearBitCache();

        assertEquals(0L, bitInputStream.readBits(1));
    }
}