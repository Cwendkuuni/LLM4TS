package org.apache.commons.compress.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for BitInputStream
 */
public class BitInputStreamTest {

    private InputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        byte[] data = new byte[]{0x01, 0x23, 0x45, 0x67};
        inputStream = new ByteArrayInputStream(data);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws Exception {
        bitInputStream.close();
    }

    @Test
    public void testReadBits_countLessThanZero() {
        try {
            bitInputStream.readBits(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testReadBits_countBiggerThanMaximumCacheSize() {
        try {
            bitInputStream.readBits(64);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testReadBits_countZero() throws IOException {
        assertEquals(0, bitInputStream.readBits(0));
    }

    @Test
    public void testReadBits_countLessThanCacheSize() throws IOException {
        long expected = 0b01111111;
        long actual = bitInputStream.readBits(7);
        assertEquals(expected, actual);
    }

    @Test
    public void testReadBits_countEqualCacheSize() throws IOException {
        long expected = 0b01111111_00110011;
        long actual = bitInputStream.readBits(15);
        assertEquals(expected, actual);
    }

    @Test
    public void testReadBits_countMoreThanCacheSize() throws IOException {
        when(inputStream.read()).thenReturn(0x89).thenReturn((byte) 0xFF);

        long expected = 0b01111111_00110011_11111111;
        long actual = bitInputStream.readBits(24);
        assertEquals(expected, actual);

        verify(inputStream, times(2)).read();
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.readBits(8);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(8));
    }
}