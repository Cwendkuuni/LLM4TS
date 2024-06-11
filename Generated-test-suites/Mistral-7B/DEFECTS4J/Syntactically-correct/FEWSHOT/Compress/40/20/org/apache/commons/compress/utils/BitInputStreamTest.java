package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BitInputStreamTest {
    @Test
    public void testBitInputStream() {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1a, (byte) 0x2b, (byte) 0x3c, (byte) 0x4d});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        assertEquals(bitInputStream.getClass(), BitInputStream.class);
        assertEquals(bitInputStream.in.getClass(), InputStream.class);
        assertEquals(bitInputStream.byteOrder, byteOrder);
    }

    @Test
    public void testClearBitCache() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1a, (byte) 0x2b, (byte) 0x3c, (byte) 0x4d});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        bitInputStream.clearBitCache();

        assertTrue(bitInputStream.bitsCachedSize == 0);
        assertNull(bitInputStream.bitsCached);
    }

    @Test
    public void testReadBits_singleBit() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1a});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        long result = bitInputStream.readBits(1);

        assertTrue(bitInputStream.bitsCachedSize == 8);
        assertEquals(result, 1L << 0);
        assertEquals(bitInputStream.bitsCached, 0x1a);
    }

    @Test
    public void testReadBits_multipleBits() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1a, (byte) 0x2b});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        long result = bitInputStream.readBits(2);

        assertTrue(bitInputStream.bitsCachedSize == 16);
        assertEquals(result, ((1L << 0) | (1L << 1)));
        assertEquals(bitInputStream.bitsCached, 0x1a);

        result = bitInputStream.readBits(4);

        assertTrue(bitInputStream.bitsCachedSize == 24);
        assertEquals(result, ((1L << 0) | (1L << 1) | (1L << 2) | (1L << 3)));
        assertEquals(bitInputStream.bitsCached, (byte) 0x2b);

        result = bitInputStream.readBits(5); // Reads 3 more bits this time.

        assertTrue(bitInputStream.bitsCachedSize == 27);
        assertEquals(result, ((1L << 1) | (1L << 2) | (1L << 3) | (1L << 4) | (1L << 5) | (1L << 6) | (1L << 7)));
        assertEquals(bitInputStream.bitsCached, (byte) 0x0);
    }

    @Test
    public void testReadBits_atEndOfStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        long result = bitInputStream.readBits(1);

        assertFalse(bitInputStream.bitsCachedSize == 0);
        assertEquals(result, -1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_negativeCount() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1a});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_countBiggerThanMaximum() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1a});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        bitInputStream.readBits(64);
    }
}