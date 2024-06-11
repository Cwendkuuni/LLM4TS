package org.apache.commons.compress.utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import static org.junit.Assert.*;

public class BitInputStreamTest {
    private InputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() throws Exception {
        byte[] inputData = new byte[]{0x34, 0x12, (byte) 0x8c, (byte) 0xa2};
        inputStream = new ByteArrayInputStream(inputData);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws Exception {
        bitInputStream.close();
    }

    @Test
    public void testReadBits() throws IOException {
        int bitCount = 16;
        long result = bitInputStream.readBits(bitCount);
        assertEquals(0x3412, result);
    }

    @Test
    public void testReadBitsWithNegativeCount() {
        int bitCount = -1;

        try {
            bitInputStream.readBits(bitCount);
            fail("Should throw IllegalArgumentException for negative count");
        } catch (IllegalArgumentException e) {
            // Pass
        } catch (IOException e) {
            fail("Should throw IllegalArgumentException for negative count");
        }
    }

    @Test
    public void testReadBitsWithCountGreaterThanMaximumCacheSize() {
        int bitCount = 64;

        try {
            bitInputStream.readBits(bitCount);
            fail("Should throw IllegalArgumentException for count exceeding maximum cache size");
        } catch (IllegalArgumentException e) {
            // Pass
        } catch (IOException e) {
            fail("Should throw IllegalArgumentException for count exceeding maximum cache size");
        }
    }

    @Test
    public void testReadBitsWithEndOfStream() throws IOException {
        int bitCount = 40;
        long result = bitInputStream.readBits(bitCount);
        assertEquals(-1, result);
    }

    @Test
    public void testClearBitCache() throws IOException {
        int bitCount = 16;
        bitInputStream.readBits(bitCount);
        bitInputStream.clearBitCache();
        long result = bitInputStream.readBits(bitCount);
        assertEquals(0x3412, result);
    }
}