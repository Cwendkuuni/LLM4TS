package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    private ByteArrayInputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04});
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
        inputStream.close();
    }

    /**
     * Test reading 1 bit.
     * @throws IOException on error
     */
    @Test
    public void testReadOneBit() throws IOException {
        assertEquals(0x01 & 0x01L, bitInputStream.readBits(1));
        assertEquals(0x02 & 0x01L, bitInputStream.readBits(1));
        assertEquals(0x04 & 0x01L, bitInputStream.readBits(1));
        assertEquals(-1L, bitInputStream.readBits(1)); // End of stream
    }

    /**
     * Test reading multiple bits.
     * @throws IOException on error
     */
    @Test
    public void testReadMultipleBits() throws IOException {
        assertEquals(0x0101L, bitInputStream.readBits(2));
        assertEquals(0x0203L, bitInputStream.readBits(3));
        assertEquals(0x04030201L, bitInputStream.readBits(7));
        assertEquals(-1L, bitInputStream.readBits(1)); // End of stream
    }

    /**
     * Test clearing the cache.
     * @throws IOException on error
     */
    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.readBits(8); // Read the first byte
        bitInputStream.clearBitCache();
        assertEquals(-1L, bitInputStream.readBits(1)); // End of stream
    }

    /**
     * Test handling negative count.
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    /**
     * Test handling count greater than MAXIMUM_CACHE_SIZE.
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMaximumCacheSize() throws IOException {
        bitInputStream.readBits(64);
    }
}