package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.BitInputStream;
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

    private ByteArrayInputStream inputStream;
    private BitInputStream bitInputStream;

    /**
     * Set up.
     */
    @Before
    public void setUp() {
        inputStream = new ByteArrayInputStream(new byte[]{(byte) 0b11111111, (byte) 0b01111111, (byte) 0b10111111, (byte) 0b11110001});
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Tear down.
     */
    @After
    public void tearDown() throws IOException {
        inputStream.close();
        bitInputStream.close();
    }

    /**
     * Test reading expected number of bits.
     */
    @Test
    public void testReadExpectedNumberOfBits() throws IOException {
        Assert.assertEquals(0b11111111_11111111, bitInputStream.readBits(16));
    }

    /**
     * Test reading less than expected number of bits.
     */
    @Test
    public void testReadLessThanExpectedNumberOfBits() throws IOException {
        Assert.assertEquals(0b11111111, bitInputStream.readBits(8));
    }

    /**
     * Test reading more than available number of bits.
     */
    @Test(expected = IOException.class)
    public void testReadMoreThanAvailableNumberOfBits() throws IOException {
        bitInputStream.readBits(17);
    }

    /**
     * Test clearing the cache.
     */
    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.readBits(16);
        bitInputStream.clearBitCache();
        Assert.assertEquals(0, bitInputStream.readBits(1));
    }

    /**
     * Test end of stream.
     */
    @Test(expected = IOException.class)
    public void testEndOfStream() throws IOException {
        inputStream.close();
        bitInputStream.readBits(1);
    }
}