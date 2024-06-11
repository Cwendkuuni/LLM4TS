package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Test class for BitInputStream
 */
public class BitInputStreamTest {

    @Test
    public void testBitInputStreamConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        assertEquals(inputStream, bitInputStream.in);
        assertEquals(byteOrder, bitInputStream.byteOrder);
    }

    @Test
    public void testCloseMethod() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.close();
        assertThrows(IOException.class, () -> bitInputStream.readBits(1));
    }

    @Test
    public void testClearBitCacheMethod() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.readBits(8); // read the first byte to fill the cache
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsMethodWithValidCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        long expectedResult = 0b0111011011011011;

        for (int i = 0; i < 12; i++) {
            long result = bitInputStream.readBits(3);
            assertEquals(expectedResult >> (12 - i * 3) & 0b111, result);
            expectedResult <<= 3;
        }
    }

    @Test
    public void testReadBitsMethodWithInvalidCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        // Test negative count
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-1));

        // Test count greater than 63
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));
    }

    @Test
    public void testReadBitsMethodAtEndOfStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.readBits(24); // read the first three bytes to fill the cache
        bitInputStream.clearBitCache();

        assertThrows(IOException.class, () -> bitInputStream.readBits(1));
    }
}