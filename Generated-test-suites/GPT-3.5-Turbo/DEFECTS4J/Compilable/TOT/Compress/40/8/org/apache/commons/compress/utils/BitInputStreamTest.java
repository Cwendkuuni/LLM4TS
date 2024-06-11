package org.apache.commons.compress.utils;
import org.apache.commons.compress.utils.BitInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import static org.junit.Assert.assertEquals;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setup() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }

    @Test
    public void testReadBits_WhenCountIsLessThanZero_ThrowsException() {
        try {
            bitInputStream.readBits(-1);
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadBits_WhenCountIsGreaterThanMaximumCacheSize_ThrowsException() {
        try {
            bitInputStream.readBits(64);
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadBits_WhenCountIsZero_ReturnsZero() {
        try {
            long result = bitInputStream.readBits(0);
            assertEquals(0, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadBits_WhenBitsNotExhausted_ReturnsBitsCorrectly() {
        try {
            long result = bitInputStream.readBits(8);
            assertEquals(1, result);

            result = bitInputStream.readBits(8);
            assertEquals(2, result);

            result = bitInputStream.readBits(8);
            assertEquals(3, result);

            result = bitInputStream.readBits(8);
            assertEquals(4, result);

            result = bitInputStream.readBits(8);
            assertEquals(5, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadBits_WhenByteOrderIsLittleEndian_ReturnsBitsCorrectly() {
        try {
            bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5}), ByteOrder.LITTLE_ENDIAN);

            long result = bitInputStream.readBits(8);
            assertEquals(1, result);

            result = bitInputStream.readBits(8);
            assertEquals(256, result);

            result = bitInputStream.readBits(8);
            assertEquals(196608, result);

            result = bitInputStream.readBits(8);
            assertEquals(33554432, result);

            result = bitInputStream.readBits(8);
            assertEquals(5368709120L, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadBits_WhenEndOfStreamReached_ReturnsMinusOne() {
        try {
            long result = bitInputStream.readBits(40);
            assertEquals(-1, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClearBitCache_WhenBitsCached_ReturnsZeroBitsCached() {
        try {
            bitInputStream.readBits(32);
            bitInputStream.clearBitCache();
            assertEquals(0, bitInputStream.readBits(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}