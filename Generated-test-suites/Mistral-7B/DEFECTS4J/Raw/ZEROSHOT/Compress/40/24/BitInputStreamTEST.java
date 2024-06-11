package org.apache.commons.compress.utils;

import static org.junit.Assert.fail;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for {@link BitInputStream}.
 */
public class BitInputStreamTest {
    private InputStream inputStream;

    @Before
    public void setUp() {
        byte[] data = new byte[]{0x0F, 0x1A, 0x2B, 0x3C};
        inputStream = new ByteArrayInputStream(data);
    }

    @After
    public void tearDown() throws Exception {
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testBitInputStreamConstruction() {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        assertNull(bitInputStream.bitsCached);
        assertFalse(bitInputStream.hasBitsCached());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBitInputStreamNegativeCount() {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBitInputStreamTooManyBits() {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.readBits(64);
    }

    @Test
    public void testBitInputStreamReadBits() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        long expectedBits = 0b00111111_01101010_10111101_11001111;
        long bits = 0;

        for (int i = 0; i < 3; i++) {
            bits |= bitInputStream.readBits(6);
            assertEquals(expectedBits >>> (i * 6), bits & MASKS[6]);
            assertEquals(expectedBits >>> (i * 6 + 6), (bits >> 6) & MASKS[6]);
        }

        // Test reading all remaining bits
        bits |= bitInputStream.readBits(11);
        assertEquals(expectedBits, bits);
        assertFalse(bitInputStream.hasBitsCached());
    }

    @Test
    public void testBitInputStreamReadBitsEndOfStream() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[0]), byteOrder);
        long bits = bitInputStream.readBits(1);
        assertEquals(-1, bits);
        assertFalse(bitInputStream.hasBitsCached());
    }

    @Test
    public void testBitInputStreamClearBitCache() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        long bits = bitInputStream.readBits(16);
        assertEquals(expectedValue(0), bits);
        assertFalse(bitInputStream.hasBitsCached());

        bitInputStream.clearBitCache();
        bits = bitInputStream.readBits(16);
        assertEquals(-1, bits);
        assertFalse(bitInputStream.hasBitsCached());
    }

    private long expectedValue(int index) {
        byte[] data = ((ByteArrayInputStream) inputStream).getData();
        return (data[index] << (8 * (inputStream.available() > 0 ? 1 : 0) + (2 * index))) & MASKS[MAXIMUM_CACHE_SIZE];
    }
}