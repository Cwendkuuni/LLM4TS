package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class BitInputStreamTest {

    @Test
    public void testReadBits() throws Exception {
        byte[] testData = new byte[] { 0x01, 0x2A, 0x3B, 0x4C, 0x5D, 0x6E, 0x7F };
        InputStream inputStream = new ByteArrayInputStream(testData);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // Test reading 1 bit
        assertEquals(1L, bitInputStream.readBits(1));
        assertEquals(0L, bitInputStream.readBits(1));

        // Test reading multiple bits from the same byte
        assertEquals(0b1011_0110L, bitInputStream.readBits(4)); // read "abcd"
        assertEquals(0b1110_1111_0110_1111L, bitInputStream.readBits(8)); // read "cdabcdab"

        // Test reading multiple bytes
        assertEquals(0b1111_0000_1111_1111_1111_1111_1111_1111_1111_0110_1111_0110_1111_0110_1111_1111_1111L, bitInputStream.readBits(32));

        // Test reading beyond available bits
        try {
            bitInputStream.readBits(33);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test clearing bit cache
        bitInputStream.clearBitCache();
        assertEquals(0L, bitInputStream.readBits(1));
        assertEquals(0L, bitInputStream.readBits(1));

        // Test reaching end of stream
        InputStream endOfStreamMock = new ByteArrayInputStream(new byte[0]);
        BitInputStream endOfStreamBitInputStream = new BitInputStream(endOfStreamMock, ByteOrder.LITTLE_ENDIAN);
        assertEquals(-1L, endOfStreamBitInputStream.readBits(1));
    }
}