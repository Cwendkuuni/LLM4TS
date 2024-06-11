package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;

public class BitInputStreamTest {

    @Test
    public void testBitInputStreamInstance() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        assertNotNull(bitInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBitInputStreamNegativeCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBitInputStreamGreaterThanMaximumCacheSizeCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.readBits(64);
    }

    @Test
    public void testClearBitCache() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBits() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        // test read 1 bit
        long bit1 = bitInputStream.readBits(1);
        assertEquals(0x01 & 0x01, bit1);

        // test read 1 bit again to make sure the bit is cleared from the cache
        long bit1_again = bitInputStream.readBits(1);
        assertEquals(0x00, bit1_again);

        // test read 8 bits (one byte)
        long byte1 = bitInputStream.readBits(8);
        assertEquals(0x01, byte1);

        // test read 9 bits (two bytes, first byte is read completely)
        long byte1_and_bit1_of_byte2 = bitInputStream.readBits(9);
        byte byte2 = inputStream.read();
        long bit2 = (byte2 & 0xFF) << 1;
        long expected = (0x01 << 7) | bit2;
        assertEquals(expected, byte1_and_bit1_of_byte2);

        // test read 7 bits from the second byte (leaving 1 bit unread)
        long bit3 = bitInputStream.readBits(7);
        assertEquals(byte2 & 0x7F, bit3 << 1);

        // test read the remaining bit from the second byte
        long bit4 = bitInputStream.readBits(1);
        assertEquals((byte2 & 0x80) >> 7, bit4);

        // test read 3 bits from the third byte (leaving 5 bits unread)
        long bit5_and_bit6_of_byte3 = bitInputStream.readBits(3);
        byte byte3 = inputStream.read();
        long expected3 = (byte3 & 0xE0) >> 5;
        assertEquals(expected3, bit5_and_bit6_of_byte3);

        // test read 3 bits from the fourth byte (leaving 5 bits unread)
        long bit7_and_bit8_of_byte4 = bitInputStream.readBits(3);
        byte byte4 = inputStream.read();
        long expected4 = (byte4 & 0xE0) >> 5;
        assertEquals(expected4, bit7_and_bit8_of_byte4);

        // test read 6 bits from the fourth byte (reading the entire byte)
        long byte4_full = bitInputStream.readBits(6);
        byte expectedByte4 = (byte) ((byte4 & 0x3F) << 2);
        assertEquals(expectedByte4, byte4_full >>> 2);

        // test read 1 bit from the fifth byte
        long bit9 = bitInputStream.readBits(1);
        byte byte5 = inputStream.read();
        assertEquals((byte5 & 0x01) << 7, bit9);

        // test end of input stream
        long endOfStream = bitInputStream.readBits(1);
        assertEquals(-1, endOfStream);

        inputStream.close();
    }

    @Test
    public void testReadBitsByteOrderLittleEndian() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        long bits = bitInputStream.readBits(16);
        long expected = ((inputStream.read() & 0xFF) << 8) | (inputStream.read() & 0xFF);
        assertEquals(expected, bits);

        bitInputStream.clearBitCache();

        bits = bitInputStream.readBits(16);
        byte firstByte = inputStream.read();
        byte secondByte = inputStream.read();
        assertEquals(((firstByte & 0xFF) << 8) | (secondByte & 0xFF), bits);

        inputStream.close();
    }

    @Test
    public void testReadBitsByteOrderBigEndian() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        long bits = bitInputStream.readBits(16);
        byte firstByte = inputStream.read();
        byte secondByte = inputStream.read();
        long expected = ((secondByte & 0xFF) << 8) | (firstByte & 0xFF);
        assertEquals(expected, bits);

        bitInputStream.clearBitCache();

        bits = bitInputStream.readBits(16);
        assertEquals(((inputStream.read() & 0xFF) << 8) | (inputStream.read() & 0xFF), bits);

        inputStream.close();
    }
}