package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {
    private InputStream in;
    private ByteOrder byteOrder;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        byteOrder = ByteOrder.LITTLE_ENDIAN;
        in = new ByteArrayInputStream(new byte[]{(byte) 0x1a, (byte) 0x2b, (byte) 0x3c, (byte) 0x4d});
        bitInputStream = new BitInputStream(in, byteOrder);
    }

    @Test
    public void testReadBitsPositiveCountLessThanOrEqualToMaximumCacheSize() throws IOException {
        long expectedBits = 0;

        // read 1 bit
        assertEquals(expectedBits | 0b00000001, bitInputStream.readBits(1));
        assertTrue(bitInputStream.bitsCachedSize() > 0);

        // read 2 bits
        expectedBits = 0b0011;
        assertEquals(expectedBits, bitInputStream.readBits(2));
        assertTrue(bitInputStream.bitsCachedSize() > 2);

        // read 60 bits
        expectedBits = 0b11111111111111111111111111111111;
        assertEquals(expectedBits, bitInputStream.readBits(60));
        assertTrue(bitInputStream.bitsCachedSize() > 60);

        // read remaining bits
        assertEquals(0, bitInputStream.readBits(bitInputStream.bitsCachedSize()));
        assertFalse(bitInputStream.hasAvailableBits());
    }

    @Test
    public void testReadBitsPositiveCountGreaterThanMaximumCacheSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            bitInputStream.readBits(64);
        });
    }

    @Test
    public void testReadBitsNegativeCount() {
        assertThrows(IllegalArgumentException.class, () -> {
            bitInputStream.readBits(-1);
        });
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        long expectedBits = -1;

        assertEquals(expectedBits, bitInputStream.readBits(1));
        assertEquals(expectedBits, bitInputStream.readBits(1));
        assertEquals(expectedBits, bitInputStream.readBits(1));
        assertEquals(expectedBits, bitInputStream.readBits(1));

        in.close();
        assertNull(bitInputStream.getInputStream());
        assertEquals(expectedBits, bitInputStream.readBits(1));
    }

    @Test
    public void testClearBitCache() throws IOException {
        long expectedBits = 0;

        bitInputStream.readBits(1);
        bitInputStream.clearBitCache();

        assertEquals(expectedBits, bitInputStream.readBits(1));
    }

    @Test
    public void testClose() throws IOException {
        byte[] expectedBytes = new byte[]{0x1a, 0x2b, 0x3c, 0x4d};

        in.close();
        assertNull(bitInputStream.getInputStream());

        bitInputStream = new BitInputStream(new ByteArrayInputStream(expectedBytes), byteOrder);
        bitInputStream.close();
        assertNull(bitInputStream.getInputStream());
    }

    @Test
    public void testByteOrderLittleEndian() throws IOException {
        byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStreamLittleEndian = new BitInputStream(in, byteOrder);

        long expectedBits = 0b0011011011011011;
        assertEquals(expectedBits, bitInputStreamLittleEndian.readBits(8));
        assertEquals(expectedBits >>> 3, bitInputStreamLittleEndian.readBits(5));
    }

    @Test
    public void testByteOrderBigEndian() throws IOException {
        byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStreamBigEndian = new BitInputStream(in, byteOrder);

        long expectedBits = 0b1111111101101101;
        assertEquals(expectedBits, bitInputStreamBigEndian.readBits(8));
        assertEquals(expectedBits << 3, bitInputStreamBigEndian.readBits(5));
    }
}