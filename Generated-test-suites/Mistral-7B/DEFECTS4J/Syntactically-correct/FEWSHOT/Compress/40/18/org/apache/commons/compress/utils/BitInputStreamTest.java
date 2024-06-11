package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BitInputStreamTest {

    @Test
    public void testReadBitsWithValidCount() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // read first bit
        long bits = bitInputStream.readBits(1);
        assertEquals(1, bits);

        // read next 5 bits
        bits = bitInputStream.readBits(6);
        assertEquals(0b111110, bits);

        // read next bit
        bits = bitInputStream.readBits(1);
        assertEquals(0, bits);

        // read next 7 bits
        bits = bitInputStream.readBits(7);
        assertEquals(0b01111111, bits);

        // read next bit
        bits = bitInputStream.readBits(1);
        assertEquals(1, bits);

        // read last bit
        bits = bitInputStream.readBits(1);
        assertEquals(0, bits);

        // read beyond end of stream
        assertThrows(IOException.class, () -> bitInputStream.readBits(1));
    }

    @Test
    public void testReadBitsWithInvalidCount() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // read with negative count
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-1));

        // read with count > MAXIMUM_CACHE_SIZE
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));
    }

    @Test
    public void testClearBitCache() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        long bits = 0;
        int count = 0;

        do {
            bits = bitInputStream.readBits(1);
            count++;
        } while (bits != -1);

        bitInputStream.clearBitCache();

        // read next bit
        bits = bitInputStream.readBits(1);
        assertEquals(0, bits);
    }
}