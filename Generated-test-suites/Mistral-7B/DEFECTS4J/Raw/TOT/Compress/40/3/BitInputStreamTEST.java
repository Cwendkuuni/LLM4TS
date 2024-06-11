package org.apache.commons.compress.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test cases for BitInputStream class.
 */
public class BitInputStreamTest {

    private InputStream testInputStream;
    private ByteOrder byteOrder;
    private BitInputStream bitInputStream;

    @Before
    public void setup() throws Exception {
        testInputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67});
        byteOrder = ByteOrder.LITTLE_ENDIAN;
        bitInputStream = new BitInputStream(testInputStream, byteOrder);
    }

    @Test
    public void testReadBitsWithValidCount() throws Exception {
        // Test reading 1 bit
        assertEquals(0x01L, bitInputStream.readBits(1));
        
        // Test reading 63 bits
        long expected = 0x01234567L;
        for (int i = 0; i < 6; i++) {
            expected <<= 8;
            expected |= testInputStream.read();
        }
        assertEquals(expected, bitInputStream.readBits(63));
    }

    @Test
    public void testReadBitsWithInvalidCount() throws Exception {
        // Test reading a negative number of bits
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-1));
        
        // Test reading a count greater than 63
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));
    }

    @Test
    public void testReadBitsAfterClearBitCache() throws Exception {
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(1));
    }

    @Test
    public void testEndOfStream() throws Exception {
        testInputStream = new ByteArrayInputStream(new byte[0]);
        bitInputStream = new BitInputStream(testInputStream, byteOrder);
        assertEquals(-1L, bitInputStream.readBits(1));
    }
}