package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    private InputStream inputStream;

    @Before
    public void setup() {
        byte[] testData = new byte[]{(byte) 0b1111_0001, (byte) 0b1111_1111};
        inputStream = new ByteArrayInputStream(testData);
    }

    @Test
    public void testBitInputStreamConstruction() {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertEquals("Expected correct BitInputStream instance", bitInputStream, new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN));
    }

    @Test
    public void testClose() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.close();
        assertThrows("Expected IOException on closed input stream", IOException.class, () -> bitInputStream.readBits(1));
    }

    @Test
    public void testClearBitCache() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(8);
        bitInputStream.clearBitCache();
        assertEquals("Expected cleared bit cache", 0, bitInputStream.bitsCached);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsGreaterThanMaximumCacheSize() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsValidCount() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long readBits = bitInputStream.readBits(5);
        assertEquals("Expected correctly read bits", 0b11111_01111, readBits);
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.read();
        bitInputStream.readBits(7);
        long readBits = bitInputStream.readBits(1);
        assertEquals("Expected end of stream", -1, readBits);
    }
}