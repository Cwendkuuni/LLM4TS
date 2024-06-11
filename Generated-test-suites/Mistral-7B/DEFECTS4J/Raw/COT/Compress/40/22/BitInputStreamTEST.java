package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[]{0x01, 0x23, 0x45});
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN);
    }

    // Test constructor
    @Test
    public void testBitInputStreamConstructor() {
        assertNotNull(bitInputStream);
    }

    // Test close method
    @Test
    public void testCloseMethod() throws IOException {
        bitInputStream.close();
        assertFalse(bitInputStream.isOpen());
    }

    // Test clearBitCache method
    @Test
    public void testClearBitCacheMethod() throws IOException {
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCachedSize);
        assertEquals(0, bitInputStream.bitsCached);
    }

    // Test readBits method with valid count
    @Test
    public void testReadBitsValidCount() throws IOException {
        long bitValue = bitInputStream.readBits(5);
        assertEquals(0b110111, bitValue);

        bitValue = bitInputStream.readBits(3);
        assertEquals(0b111, bitValue);

        bitValue = bitInputStream.readBits(63);
        assertEquals(-1L, bitValue);
    }

    // Test readBits method with invalid count
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    // Test readBits method when end of stream is reached
    @Test
    public void testReadBitsEndOfStream() throws IOException {
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);

        long bitValue = bitInputStream.readBits(1);
        assertEquals(-1L, bitValue);
    }
}