package org.apache.commons.compress.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Test class for BitInputStream
 */
public class BitInputStreamTest {

    private BitInputStream bitInputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() {
        byteArrayInputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1a, (byte) 0x2b, (byte) 0x3c});
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Test method for {@link BitInputStream#BitInputStream(InputStream, ByteOrder)}
     */
    @Test
    public void testBitInputStream() {
        BitInputStream bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN);
        assertEquals(byteArrayInputStream, bitInputStream.in);
        assertEquals(ByteOrder.LITTLE_ENDIAN, bitInputStream.byteOrder);
    }

    /**
     * Test method for {@link BitInputStream#close()}
     */
    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
        assertThrows(IOException.class, () -> bitInputStream.readBits(1));
    }

    /**
     * Test method for {@link BitInputStream#clearBitCache()}
     */
    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    /**
     * Test method for {@link BitInputStream#readBits(int)} with valid count
     */
    @Test
    public void testReadBitsValidCount() throws IOException {
        // Test reading 1 bit
        long bits1 = bitInputStream.readBits(1);
        assertEquals(0x1a >>> 7 & 1, bits1); // Get the 7th bit of the first byte

        // Test reading multiple bits
        long bits8 = bitInputStream.readBits(8);
        long expected = 0x1a;
        assertEquals(expected, bits8);

        long bits16 = bitInputStream.readBits(16);
        expected = 0x1a << 8 | (0x2b >>> 4);
        assertEquals(expected, bits16);

        long bits24 = bitInputStream.readBits(24);
        expected = 0x1a << 8 | (0x2b << 4);
        assertEquals(expected, bits24);

        long bits32 = bitInputStream.readBits(32);
        expected = 0x1a << 8 | (0x2b << 4) | (0x3c >>> 4);
        assertEquals(expected, bits32);
    }

    /**
     * Test method for {@link BitInputStream#readBits(int)} with invalid count
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    /**
     * Test method for {@link BitInputStream#readBits(int)} with end of stream
     */
    @Test
    public void testReadBitsEndOfStream() throws IOException {
        bitInputStream.readBits(32); // Read all available bits

        long bits = bitInputStream.readBits(1); // Read 1 bit after EOS
        assertEquals(-1, bits);
    }
}