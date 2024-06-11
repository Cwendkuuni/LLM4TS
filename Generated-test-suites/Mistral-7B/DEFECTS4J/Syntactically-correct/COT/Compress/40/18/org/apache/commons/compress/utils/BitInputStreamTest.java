package org.apache.commons.compress.utils;

import static org.junit.Assert.fail;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Test class for BitInputStream
 */
public class BitInputStreamTest {

    private InputStream inputStream;

    @Before
    public void setUp() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04});
        new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    // Test case for constructor
    @Test
    public void testBitInputStreamConstructor() {
        // No test implementation needed as the constructor does not throw any exception
    }

    // Test case for readBits() with valid count
    @Test
    public void testReadBitsValidCount() throws IOException {
        for (int count = 1; count <= 63; count++) {
            long bits = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN).readBits(count);
            System.out.printf("Read %d bits: %s%n", count, Long.toBinaryString(bits));
            if (bits == -1) {
                fail("Unexpected end of stream reached before reading the requested number of bits.");
            }
        }
    }

    // Test case for readBits() with zero count
    @Test
    public void testReadBitsZeroCount() throws IOException {
        long bits = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN).readBits(0);
        assertEquals(0, bits);
    }

    // Test case for readBits() with negative count
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN).readBits(-1);
    }

    // Test case for readBits() with count greater than 63
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsCountGreaterThan63() throws IOException {
        new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN).readBits(64);
    }

    // Test case for clearBitCache()
    @Test
    public void testClearBitCache() throws IOException {
        new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN).readBits(1);
        new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN).clearBitCache();
        long bits = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN).readBits(1);
        assertEquals(0, bits);
    }

    // Test case for readBits() with end of stream
    @Test
    public void testReadBitsEndOfStream() throws IOException {
        long bits = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN).readBits(1);
        InputStream inputStreamSpy = new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
        new BitInputStream(inputStreamSpy, ByteOrder.LITTLE_ENDIAN).readBits(1);
        assertEquals(-1, bits);
    }
}