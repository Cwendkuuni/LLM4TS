package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class BitInputStreamTest {
    @Test
    public void testReadBitsWithValidParameters() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        long bits = bitInputStream.readBits(1);
        assertEquals(0x01 & 1L, bits);

        bits = bitInputStream.readBits(2);
        assertEquals(0x0102 & ((1L << 2) - 1), bits);

        bits = bitInputStream.readBits(3);
        assertEquals(0x010203 & ((1L << 3) - 1), bits);

        bits = bitInputStream.readBits(4);
        assertEquals(0x01020304 & ((1L << 4) - 1), bits);

        bits = bitInputStream.readBits(5);
        assertEquals(0x0102030404 & ((1L << 5) - 1), bits);

        bits = bitInputStream.readBits(6);
        assertEquals(0x010203040404 & ((1L << 6) - 1), bits);

        bits = bitInputStream.readBits(7);
        assertEquals(0x01020304040404 & ((1L << 7) - 1), bits);

        bits = bitInputStream.readBits(8);
        assertEquals(0x01020304, bits);

        bits = bitInputStream.readBits(9);
        assertEquals((0x0102030404 << 1), bits);

        bits = bitInputStream.readBits(63);
        assertEquals((0x01020304040404040404040404040404040404L & ((1L << 63) - 1)), bits);

        bitInputStream.clearBitCache();

        bits = bitInputStream.readBits(1);
        assertEquals(0x02 & 1L, bits);

        bits = bitInputStream.readBits(2);
        assertEquals(0x0203 & ((1L << 2) - 1), bits);

        bits = bitInputStream.readBits(3);
        assertEquals(0x020304 & ((1L << 3) - 1), bits);

        bits = bitInputStream.readBits(4);
        assertEquals(0x02030404 & ((1L << 4) - 1), bits);

        bits = bitInputStream.readBits(5);
        assertEquals(0x0203040404 & ((1L << 5) - 1), bits);

        bits = bitInputStream.readBits(6);
        assertEquals(0x020304040404 & ((1L << 6) - 1), bits);

        bits = bitInputStream.readBits(7);
        assertEquals(0x02030404040404 & ((1L << 7) - 1), bits);

        bits = bitInputStream.readBits(8);
        assertEquals(0x02, bits);

        bits = bitInputStream.readBits(9);
        assertEquals((0x02 << 1), bits);

        bits = bitInputStream.readBits(63);
        assertEquals((0x02030404040404040404040404040404040404 << 1), bits);

        bitInputStream.close();
    }

    @Test
    public void testReadBitsWithInvalidParameters() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-1));
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));
    }

    @Test
    public void testReadBitsWithEndOfStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x01 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        long bits = bitInputStream.readBits(1);
        assertEquals(0x01 & 1L, bits);

        bits = bitInputStream.readBits(2);
        assertEquals(-1, bits);

        bitInputStream.close();
    }
}