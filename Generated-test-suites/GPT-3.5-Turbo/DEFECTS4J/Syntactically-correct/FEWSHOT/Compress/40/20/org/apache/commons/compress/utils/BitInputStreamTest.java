package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BitInputStreamTest {
    @Test
    public void testReadBits() throws Exception {
        // create a dummy InputStream to test with
        InputStream dummyInputStream = new InputStream() {
            private int[] data = {0b00000000, 0b11110000};
            private int position = 0;

            @Override
            public int read() throws IOException {
                if (position >= data.length) {
                    return -1;
                }
                return data[position++];
            }
        };

        BitInputStream bitInputStream = new BitInputStream(dummyInputStream, ByteOrder.LITTLE_ENDIAN);

        // test reading 1 bit
        long result1 = bitInputStream.readBits(1);
        assertEquals(0b0, result1);

        // test reading 4 bits
        long result2 = bitInputStream.readBits(4);
        assertEquals(0b1111, result2);

        // test reading 8 bits
        long result3 = bitInputStream.readBits(8);
        assertEquals(0b11110000, result3);

        // test reading more than available bits
        long result4 = bitInputStream.readBits(10);
        assertEquals(-1, result4);
    }
}