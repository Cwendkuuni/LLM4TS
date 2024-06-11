package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class BitInputStreamTest {

    @Test
    public void testReadBitsWithValidCount() throws Exception {
        final InputStream inputStream = new MockInputStream();
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        // test reading single bits
        for (int i = 0; i < 64; i++) {
            long expectedBit = i < 32 ? 1 : 0;
            long actualBit = bitInputStream.readBits(1);
            assertEquals(expectedBit, actualBit);
        }

        // test reading multiple bits
        for (int i = 1; i <= 64; i++) {
            int count = i;
            long expectedBits = 0;
            long mask = 1;
            for (int j = 0; j < count; j++) {
                expectedBits |= mask;
                mask = mask << 1;
            }
            long actualBits = bitInputStream.readBits(count);
            assertEquals(expectedBits, actualBits);
        }

        // test reading all available bits and then reading again
        bitInputStream.readBits(63);
        long expectedBits = inputStream.read();
        long actualBits = bitInputStream.readBits(8);
        assertEquals(expectedBits & 0xFF, actualBits);
    }

    @Test
    public void testReadBitsWithInvalidCount() throws Exception {
        final InputStream inputStream = new MockInputStream();
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-1));
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));
    }

    @Test
    public void testReadBitsWithEndOfStream() throws Exception {
        final InputStream inputStream = new MockInputStreamWithEndOfStream();
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        assertThrows(IOException.class, () -> bitInputStream.readBits(1));
    }

    @Test
    public void testClearBitCache() throws Exception {
        final InputStream inputStream = new MockInputStream();
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        bitInputStream.readBits(63);
        bitInputStream.clearBitCache();
        assertNull(bitInputStream.bitsCached);
    }

    private static class MockInputStream implements InputStream {
        @Override
        public int read() throws IOException {
            return 0xFF & (System.in.read());
        }

        @Override
        public void close() throws IOException {
        }
    }

    private static class MockInputStreamWithEndOfStream extends MockInputStream {
        @Override
        public int read() throws IOException {
            return -1;
        }
    }
}