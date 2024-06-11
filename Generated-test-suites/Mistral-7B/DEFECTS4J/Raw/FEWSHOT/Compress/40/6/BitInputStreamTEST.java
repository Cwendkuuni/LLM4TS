package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class BitInputStreamTest {

    @Test
    public void testReadBitsPositiveCount() throws Exception {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = createMockInputStream(new byte[] { 0x1a, 0x2b, 0xc1, 0x34, 0x5e });
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        long bits = bitInputStream.readBits(5);
        assertEquals(0x2402L, bits);

        bits = bitInputStream.readBits(3);
        assertEquals(0x014L, bits);

        bits = bitInputStream.readBits(1);
        assertEquals(0x01L, bits);

        bits = bitInputStream.readBits(62);
        assertEquals(0x12345e61c12b1aL, bits);

        bits = bitInputStream.readBits(11);
        assertEquals(0x602c1L, bits);

        bits = bitInputStream.readBits(63);
        assertThrows(IOException.class, () -> bitInputStream.readBits(63));

        bitInputStream.close();
    }

    @Test
    public void testReadBitsNegativeCount() throws Exception {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = createMockInputStream(new byte[] { 0x1a, 0x2b, 0xc1, 0x34, 0x5e });
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-1));
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-5));
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-10));

        bitInputStream.close();
    }

    @Test
    public void testReadBitsCountGreaterThanMaximum() throws Exception {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = createMockInputStream(new byte[] { 0x1a, 0x2b, 0xc1, 0x34, 0x5e });
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(65));

        bitInputStream.close();
    }

    @Test
    public void testReadBitsEndOfStream() throws Exception {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = createMockInputStream(new byte[] { 0x1a, 0x2b, 0xc1 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        long bits = bitInputStream.readBits(5);
        assertEquals(0x2402L, bits);

        bits = bitInputStream.readBits(1);
        assertEquals(0x01L, bits);

        bits = bitInputStream.readBits(63);
        assertEquals(-1L, bits);

        bitInputStream.close();
    }

    @Test
    public void testClearBitCache() throws Exception {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = createMockInputStream(new byte[] { 0x1a, 0x2b, 0xc1, 0x34, 0x5e });
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        long bits = bitInputStream.readBits(5);
        assertEquals(0x2402L, bits);

        bitInputStream.clearBitCache();

        bits = bitInputStream.readBits(5);
        assertNotEquals(0x2402L, bits);

        bitInputStream.close();
    }

    private InputStream createMockInputStream(byte[] bytes) {
        return new MockInputStream() {
            private int index = 0;

            @Override
            public int read() throws IOException {
                if (index >= bytes.length) {
                    return -1;
                }
                return (int) bytes[index++] & 0xFF;
            }
        };
    }
}