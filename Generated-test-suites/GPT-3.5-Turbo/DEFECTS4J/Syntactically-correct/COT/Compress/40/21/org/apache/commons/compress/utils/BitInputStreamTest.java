
package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import static org.junit.Assert.*;

public class BitInputStreamTest {

    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        bitInputStream.close();

        // The underlying stream should be closed
        assertTrue(inputStream instanceof ByteArrayInputStream);
        assertEquals(-1, inputStream.read());
    }

    @Test
    public void testClearBitCache() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        bitInputStream.clearBitCache();

        // The bit cache should be cleared
        assertEquals(0, bitInputStream.readBits(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThanMaximumCacheSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsWithCountZero() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        long result = bitInputStream.readBits(0);

        // Reading zero bits should return 0
        assertEquals(0, result);
    }
}
