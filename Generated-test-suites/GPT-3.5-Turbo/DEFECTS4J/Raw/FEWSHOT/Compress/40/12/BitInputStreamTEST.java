package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {

    private static final byte[] TEST_BYTES = {0x12, 0x34, 0x56, 0x78};
    private BitInputStream bitInputStream;

    @Before
    public void setup() {
        bitInputStream = new BitInputStream(new ByteArrayInputStream(TEST_BYTES), ByteOrder.BIG_ENDIAN);
    }

    @Test
    public void testReadBitsWithPositiveCount() throws IOException {
        assertEquals(0x1, bitInputStream.readBits(1));
        assertEquals(0x12, bitInputStream.readBits(8));
        assertEquals(0x2, bitInputStream.readBits(2));
        assertEquals(0x34, bitInputStream.readBits(8));
        assertEquals(0x456, bitInputStream.readBits(12));
        assertEquals(0x7, bitInputStream.readBits(3));
        assertEquals(0x8, bitInputStream.readBits(4));
        assertEquals(0, bitInputStream.readBits(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThan63() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsWithEndOfStream() throws IOException {
        BitInputStream emptyStream = new BitInputStream(new ByteArrayInputStream(new byte[0]), ByteOrder.LITTLE_ENDIAN);
        assertEquals(-1, emptyStream.readBits(1));
        assertNull(emptyStream.readBits(8));
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.readBits(16);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(8));
        assertEquals(0, bitInputStream.readBits(8));
    }

    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
        try {
            bitInputStream.readBits(1);
            assertTrue(false);
        } catch (IOException e) {
            assertTrue(true);
        }
    }
}