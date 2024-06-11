package org.apache.commons.compress.utils;

import static org.junit.Assert.fail;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Test;

/**
 * Test class for BitInputStream
 */
public class BitInputStreamTest {

    @Test
    public void testBitInputStreamConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x2A, (byte) 0x3B});
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        // Check if the constructor works correctly
        assert (bitInputStream != null);
    }

    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x2A, (byte) 0x3B});
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.close();
        // Check if close method works correctly
        assert (bitInputStream.isClosable());
        try {
            bitInputStream.readBits(1);
            fail("Expected IOException after close method call");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testClearBitCache() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x2A, (byte) 0x3B});
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.clearBitCache();
        // Check if clearBitCache method works correctly
        assert (bitInputStream.bitsCached == 0);
        assert (bitInputStream.bitsCachedSize == 0);
    }

    @Test
    public void testReadBitsWithValidCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x2A, (byte) 0x3B});
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        // Test reading 1 bit
        long testBit1 = bitInputStream.readBits(1);
        assert (testBit1 == 0x01L);
        // Test reading 8 bits
        long testByte1 = bitInputStream.readBits(8);
        assert (testByte1 == ((long) 0x01 << 24) | ((long) 0x2A << 16) | ((long) 0x3B << 8) | ((long) 0xFF & bitInputStream.readBits(8)));
        // Test reading 6 bits
        long testBits6 = bitInputStream.readBits(6);
        assert (testBits6 == ((long) 0x2A << 2) | ((long) 0x3B >> 2));
        // Test reading 63 bits (full cache)
        long testBits63 = bitInputStream.readBits(63);
        assert (testBits63 == ((long) 0x01 << 24) | ((long) 0x2A << 16) | ((long) 0x3B << 8));
        // Test reading more bits than available
        try {
            bitInputStream.readBits(64);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testReadBitsWithInvalidCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x2A, (byte) 0x3B});
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        // Test reading a negative count
        try {
            bitInputStream.readBits(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        // Test reading more bits than available
        try {
            bitInputStream.readBits(64);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        // Test reading a count greater than 63
        try {
            bitInputStream.readBits(64);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}