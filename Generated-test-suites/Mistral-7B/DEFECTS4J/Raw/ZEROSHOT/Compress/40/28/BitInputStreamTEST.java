package org.apache.commons.compress.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for BitInputStream class.
 */
public class BitInputStreamTest {

    private InputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() throws IOException {
        byte[] data = new byte[]{0x01, 0x23, 0x45, 0x67};
        inputStream = new ByteArrayInputStream(data);
        byteOrder = ByteOrder.LITTLE_ENDIAN;
        bitInputStream = new BitInputStream(inputStream, byteOrder);
    }

    @After
    public void tearDown() throws Exception {
        if (bitInputStream != null) {
            bitInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    /**
     * Test constructor with valid InputStream and byte order.
     */
    @Test
    public void testBitInputStreamConstructor() {
        assertNotNull(bitInputStream);
    }

    /**
     * Test close method.
     */
    @Test
    public void testClose() throws Exception {
        bitInputStream.close();
        assertNull(bitInputStream);
    }

    /**
     * Test clearBitCache method.
     */
    @Test
    public void testClearBitCache() throws Exception {
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(1));
    }

    /**
     * Test readBits method with different count values.
     */
    @Test
    public void testReadBits() throws Exception {
        // Test reading 1 bit
        assertEquals(0x01 & 0x01L, bitInputStream.readBits(1));
        assertEquals(0x00 & 0x01L, bitInputStream.readBits(1));
        assertEquals(0x02 & 0x01L, bitInputStream.readBits(1));

        // Test reading multiple bits from one byte
        assertEquals(0x23 & MASKS[5], bitInputStream.readBits(6));
        assertEquals(0x1C & MASKS[5], bitInputStream.readBits(6));

        // Test reading multiple bits from different bytes
        assertEquals(0x45 << 3 | (0x67 >> 5), bitInputStream.readBits(8));
        assertEquals(0x48 & 0x7F, bitInputStream.readBits(7));

        // Test reading bits from the end of the input stream
        assertEquals(-1, bitInputStream.readBits(1));
    }

    /**
     * Test readBits method with negative or greater count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws Exception {
        bitInputStream.readBits(-1);
        bitInputStream.readBits(64);
    }

    /**
     * Test clearing the cache after reading bits.
     */
    @Test
    public void testReadBitsClearsCache() throws Exception {
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        assertEquals(0x00 & 0x01L, bitInputStream.readBits(1));
    }

    /**
     * Test reading bits from a Big-Endian stream.
     */
    @Test
    public void testReadBitsBigEndian() throws Exception {
        byteOrder = ByteOrder.BIG_ENDIAN;
        bitInputStream = new BitInputStream(inputStream, byteOrder);

        assertEquals(0x01 & 0x80_0000_0001L, bitInputStream.readBits(1));
        assertEquals((0x23 >> 1) & 0x7F, bitInputStream.readBits(7));
        assertEquals((0x45 >> 3) | (0x67 << 3), bitInputStream.readBits(8));
    }

    /**
     * Test reading from an empty input stream.
     */
    @Test
    public void testReadBitsEmptyInputStream() throws Exception {
        inputStream = new ByteArrayInputStream(new byte[0]);
        bitInputStream = new BitInputStream(inputStream, byteOrder);

        assertEquals(-1, bitInputStream.readBits(1));
        bitInputStream.close();
    }
}