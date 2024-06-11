package org.apache.commons.compress.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Test cases for {@link BitInputStream}.
 */
public class BitInputStreamTest {

    private InputStream inputStream;
    private ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

    @Before
    public void setUp() throws IOException {
        String testData = "1111011011111101";
        inputStream = new ByteArrayInputStream(testData.getBytes());
    }

    @After
    public void tearDown() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testBitInputStreamConstructor() {
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        assertEquals(inputStream, bitInputStream.in);
        assertEquals(byteOrder, bitInputStream.byteOrder);
    }

    @Test
    public void testClearBitCache() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        long expectedBits = 0xFFL; // all bits set
        long bits = 0;

        for (int i = 0; i < 8; i++) {
            bits |= bitInputStream.readBits(1);
            assertEquals(expectedBits >>> (7 - i), bits & 1L);
        }
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsLessThanOneBit() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        assertEquals(0, bitInputStream.readBits(0));
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsOneBit() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        long expectedBits = 0b11110110_11111101L;
        long bits = 0;

        for (int i = 0; i < 16; i++) {
            bits |= bitInputStream.readBits(1);
            assertEquals(expectedBits >>> (15 - i), bits & 1L);
        }
        assertEquals(expectedBits, bitInputStream.bitsCached);
        assertEquals(16, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsMoreThanAvailableBits() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        long expectedBits = 0xFFL; // all bits set
        long bits = 0;

        for (int i = 0; i < 8; i++) {
            bits |= bitInputStream.readBits(1);
            assertEquals(expectedBits >>> (7 - i), bits & 1L);
        }

        inputStream.mark(0);
        inputStream.read(); // read the last byte
        inputStream.reset();

        for (int i = 0; i < 7; i++) {
            bits |= bitInputStream.readBits(1);
            assertEquals(expectedBits >>> (6 - i), bits & 1L);
        }

        // read one more bit than available
        long lastBit = bitInputStream.readBits(1);
        assertEquals(-1, lastBit);
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }
}