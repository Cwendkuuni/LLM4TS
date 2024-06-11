package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitInputStreamTest {

    @Test
    public void testBitInputStream() {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        assertEquals(bitInputStream.getClass(), BitInputStream.class);
        assertEquals(input.getClass(), bitInputStream.getInputStream().getClass());
        assertEquals(byteOrder, bitInputStream.getByteOrder());
    }

    @Test
    public void testClearBitCache() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        bitInputStream.clearBitCache();

        assertTrue(bitInputStream.bitsCachedSize() == 0);
        assertTrue(bitInputStream.bitsCached() == 0);
    }

    @Test
    public void testReadBits() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        // read 1 bit
        long bits = bitInputStream.readBits(1);
        assertEquals(bits, 0x01 & 0x01);
        assertEquals(bitInputStream.bitsCachedSize(), 8);
        assertEquals(bitInputStream.bitsCached(), 0x01 << 7);

        // read 63 bits
        for (int i = 0; i < 63; i++) {
            bits = bitInputStream.readBits(1);
            assertTrue(bits == 0 || bits == 1);
        }
        assertEquals(bitInputStream.bitsCachedSize(), 0);
        assertEquals(bitInputStream.bitsCached(), 0);

        // read 64 bits
        assertFalse(bitInputStream.hasNextBit());

        // test end of stream
        InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        BitInputStream emptyBitInputStream = new BitInputStream(emptyInput, byteOrder);
        assertFalse(emptyBitInputStream.hasNextBit());

        // test negative count
        try {
            bitInputStream.readBits(-1);
            fail("Expected IOException for negative count");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test count > 63
        try {
            bitInputStream.readBits(64);
            fail("Expected IOException for count > 63");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testByteOrder() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});

        BitInputStream bitInputStreamLittleEndian = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        BitInputStream bitInputStreamBigEndian = new BitInputStream(input, ByteOrder.BIG_ENDIAN);

        // test little endian
        long bitsLittleEndian = 0x00;
        for (int i = 0; i < 8; i++) {
            bitsLittleEndian |= bitInputStreamLittleEndian.readBits(1) << i;
        }
        assertEquals(bitsLittleEndian, 0x01);

        // test big endian
        long bitsBigEndian = 0x00;
        for (int i = 0; i < 8; i++) {
            bitsBigEndian |= bitInputStreamBigEndian.readBits(1) << (7 - i);
        }
        assertEquals(bitsBigEndian, 0x08);
    }

    @Test
    public void testHasNextBit() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        // test first bit
        assertTrue(bitInputStream.hasNextBit());

        // test read bit
        long bits = bitInputStream.readBits(1);
        assertEquals(bits, 0x01 & 0x01);
        assertEquals(bitInputStream.bitsCachedSize(), 8);
        assertEquals(bitInputStream.bitsCached(), 0x01 << 7);

        // test next bit
        assertTrue(bitInputStream.hasNextBit());

        // test last bit
        assertFalse(bitInputStream.hasNextBit());

        // test end of stream
        InputStream emptyInput = new ByteArrayInputStream(new byte[0][0]);
        BitInputStream emptyBitInputStream = new BitInputStream(emptyInput, byteOrder);
        assertFalse(emptyBitInputStream.hasNextBit());
    }

    @Test
    public void testGetBitsCachedSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        assertEquals(bitInputStream.bitsCachedSize(), 0);

        for (int i = 0; i < 63; i++) {
            bitInputStream.readBits(1);
        }

        assertEquals(bitInputStream.bitsCachedSize(), 63);
        assertEquals(bitInputStream.bitsCached(), Long.parseUnsignedLong("11111111111111111111111111111101", 2));

        assertFalse(bitInputStream.hasNextBit());
    }

    @Test
    public void testGetBitsCached() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        for (int i = 0; i < 63; i++) {
            bitInputStream.readBits(1);
        }

        long bits = bitInputStream.bitsCached();
        assertEquals(bits, Long.parseUnsignedLong("11111111111111111111111111111101", 2));

        assertFalse(bitInputStream.hasNextBit());
    }

    @Test
    public void testGetInputStream() {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        assertEquals(bitInputStream.getInputStream().getClass(), input.getClass());
    }

    @Test
    public void testGetByteOrder() {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});

        BitInputStream bitInputStreamLittleEndian = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        BitInputStream bitInputStreamBigEndian = new BitInputStream(input, ByteOrder.BIG_ENDIAN);

        assertEquals(bitInputStreamLittleEndian.getByteOrder(), ByteOrder.LITTLE_ENDIAN);
        assertEquals(bitInputStreamBigEndian.getByteOrder(), ByteOrder.BIG_ENDIAN);
    }

    @Test
    public void testSameObject() {
        InputStream input1 = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        InputStream input2 = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

        BitInputStream bitInputStream1 = new BitInputStream(input1, byteOrder);
        BitInputStream bitInputStream2 = new BitInputStream(input2, byteOrder);

        assertNotSame(bitInputStream1, bitInputStream2);
    }
}