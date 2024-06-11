package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {
    private InputStream inputStream;

    @Before
    public void setup() {
        inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x12, (byte) 0x34, (byte) 0x56});
    }

    @Test
    public void testReadBits() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        assertEquals(0x12, bitInputStream.readBits(8));
        assertEquals(0x34, bitInputStream.readBits(8));
        assertEquals(0x56, bitInputStream.readBits(8));
        assertEquals(-1, bitInputStream.readBits(8));
        bitInputStream.close();
    }

    @Test
    public void testClearBitCache() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long bit1 = bitInputStream.readBits(8);
        long bit2 = bitInputStream.readBits(4);
        bitInputStream.clearBitCache();
        assertEquals(0x12, bitInputStream.readBits(8));
        bitInputStream.close();
    }
}