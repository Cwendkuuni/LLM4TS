package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    /**
     * Test method for {@link BitInputStream#BitInputStream(InputStream, ByteOrder)}.
     */
    @org.junit.Test
    public void testBitInputStreamConstruction() {
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        assertEquals(ByteOrder.LITTLE_ENDIAN, bitInputStream.getByteOrder());
        assertEquals(input, bitInputStream.getInputStream());
    }

    /**
     * Test method for {@link BitInputStream#BitInputStream(InputStream, ByteOrder)}.
     */
    @org.junit.Test
    public void testBitInputStreamConstructionWithBigEndian() {
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);
        assertEquals(ByteOrder.BIG_ENDIAN, bitInputStream.getByteOrder());
        assertEquals(input, bitInputStream.getInputStream());
    }

    /**
     * Test method for {@link BitInputStream#clearBitCache()}.
     */
    @org.junit.Test
    public void testClearBitCache() {
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        assertEquals(ByteOrder.LITTLE_ENDIAN, bitInputStream.getByteOrder());
        assertEquals(input, bitInputStream.getInputStream());

        bitInputStream.readBits(5);
        bitInputStream.clearBitCache();

        assertEquals(0, bitInputStream.readBits(32));
    }

    /**
     * Test method for {@link BitInputStream#readBits(int)}.
     */
    @org.junit.Test
    public void testReadBits() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);

        long bits = 0;

        // read 1 bit
        bits |= bitInputStream.readBits(1);
        assertEquals(bits & 1, 1);

        // read 3 bits
        bits = (bits << 3) | bitInputStream.readBits(3);
        assertEquals(bits & 0x07, 0x05);

        // read 6 bits
        bits = (bits << 6) | bitInputStream.readBits(6);
        assertEquals(bits & 0x3f, 0x1e);

        // read 8 bits
        bits = (bits << 8) | bitInputStream.readBits(8);
        assertEquals(bits, 0x04030201L);

        // read 10 bits
        bits = (bits << 10) | bitInputStream.readBits(10);
        assertEquals(bits & 0x3ffc00L, 0x03fc00L);

        // read 64 bits
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result = (result << 8) | bitInputStream.readBits(8);
        }
        assertEquals(result, 0x0102030401020304L);

        // read more bits than available
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));
    }

    /**
     * Test method for {@link BitInputStream#readBits(int)}.
     */
    @org.junit.Test
    public void testReadBitsWithBigEndian() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);

        long bits = 0;

        // read 1 bit
        bits |= bitInputStream.readBits(1);
        assertEquals(bits & 1, 1);

        // read 3 bits
        bits = (bits << 3) | bitInputStream.readBits(3);
        assertEquals(bits & 0x07, 0x02);

        // read 6 bits
        bits = (bits << 6) | bitInputStream.readBits(6);
        assertEquals(bits & 0x3f, 0x04);

        // read 8 bits
        bits = (bits << 8) | bitInputStream.readBits(8);
        assertEquals(bits, 0x04030201L);

        // read 10 bits
        bits = (bits << 10) | bitInputStream.readBits(10);
        assertEquals(bits & 0x3ffc00L, 0x010203fcL);

        // read 64 bits
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result = (result << 8) | bitInputStream.readBits(8);
        }
        assertEquals(result, 0x0102030401020304L);

        // read more bits than available
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));
    }

    /**
     * Test method for {@link BitInputStream#close()}.
     */
    @org.junit.Test
    public void testClose() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);

        bitInputStream.close();
        assertThrows(IOException.class, () -> bitInputStream.readBits(5));
    }
}