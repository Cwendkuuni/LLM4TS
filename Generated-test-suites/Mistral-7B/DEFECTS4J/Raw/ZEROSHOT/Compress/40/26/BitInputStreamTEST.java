package org.apache.commons.compress.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    private InputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        byte[] data = new byte[]{(byte) 0b11111111, (byte) 0b01111111};
        inputStream = new ByteArrayInputStream(data);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        if (bitInputStream != null) {
            bitInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testReadBits() throws IOException {
        // Test reading 1 bit
        Assert.assertEquals(1L, bitInputStream.readBits(1));
        Assert.assertEquals(0L, bitInputStream.readBits(1));

        // Test reading multiple bits from a single byte
        Assert.assertEquals(0b11111111L, bitInputStream.readBits(8));
        Assert.assertEquals(0b01111111L, bitInputStream.readBits(8));

        // Test reading less bits than available in a byte
        Assert.assertEquals(0b11110000L, bitInputStream.readBits(4));
        Assert.assertEquals(0b01111100L, bitInputStream.readBits(6));

        // Test reading at the end of the stream
        Assert.assertEquals(-1L, bitInputStream.readBits(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsGreaterThanMaximumCacheSize() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.readBits(8);
        bitInputStream.clearBitCache();
        Assert.assertEquals(-1L, bitInputStream.readBits(1));
    }
}