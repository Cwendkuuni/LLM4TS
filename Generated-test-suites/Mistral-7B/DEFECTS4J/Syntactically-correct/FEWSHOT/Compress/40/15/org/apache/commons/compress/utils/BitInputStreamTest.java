package org.apache.commons.compress.utils;

import static org.junit.Assert.fail;

import org.junit.Test;

public class BitInputStreamTest {

    @Test
    public void testReadBitsWithValidInputStreamAndByteOrder() throws Exception {
        final InputStream inputStream = new MockInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        try (BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder)) {
            // Test reading 1 bit
            assertEquals(0b00000001L, bitInputStream.readBits(1));
            assertEquals(0b00000001L, bitInputStream.readBits(1));
            assertEquals(0b00000000L, bitInputStream.readBits(1));

            // Test reading multiple bits
            assertEquals(0b11111111L, bitInputStream.readBits(8));
            assertEquals(0b00111100L, bitInputStream.readBits(6));
            assertEquals(0b00011110L, bitInputStream.readBits(5));
            assertEquals(0b00001111L, bitInputStream.readBits(4));

            // Test reading all available bits from the input stream
            assertEquals(0b1111111111111111L, bitInputStream.readBits(14));
            assertFalse(bitInputStream.readBits(1) >= 0);
        }
    }

    @Test
    public void testReadBitsWithNullInputStream() {
        try {
            new BitInputStream(null, ByteOrder.LITTLE_ENDIAN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testReadBitsWithInvalidByteOrder() {
        final InputStream inputStream = new MockInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });
        try {
            new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected, BitInputStream does not support BIG_ENDIAN byte order
        }
    }

    @Test
    public void testClearBitCache() throws Exception {
        final InputStream inputStream = new MockInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        try (BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder)) {
            bitInputStream.readBits(1);
            bitInputStream.clearBitCache();
            assertEquals(0, bitInputStream.readBits(1));
        }
    }

    @Test
    public void testReadBitsWithEndOfStream() throws Exception {
        final InputStream inputStream = new MockInputStream(new byte[] {});
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        try (BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder)) {
            assertEquals(-1, bitInputStream.readBits(1));
        }
    }

    private static class MockInputStream extends InputStream {
        private final byte[] data;
        private int index = 0;

        public MockInputStream(byte[] data) {
            this.data = data;
        }

        @Override
        public int read() throws IOException {
            if (index < data.length) {
                return data[index++] & 0xFF;
            }
            return -1;
        }
    }
}