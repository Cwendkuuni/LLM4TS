
package org.apache.commons.compress.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testReadBits() throws IOException {
        byte[] bytes = { (byte) 0b11010001, (byte) 0b01011011, (byte) 0b10101100 };
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);

        // Test reading 1 bit
        assertEquals(1, bitInputStream.readBits(1));
        assertEquals(1, bitInputStream.readBits(1));
        assertEquals(0, bitInputStream.readBits(1));
        assertEquals(1, bitInputStream.readBits(1));

        // Test reading 2 bits
        assertEquals(1, bitInputStream.readBits(2));
        assertEquals(0, bitInputStream.readBits(2));
        assertEquals(2, bitInputStream.readBits(2)); // Reached end of stream, should return -1

        // Test reading 4 bits
        assertEquals(0b0001, bitInputStream.readBits(4));
        assertEquals(0b1111, bitInputStream.readBits(4));
        assertEquals(-1, bitInputStream.readBits(4)); // Reached end of stream, should return -1

        // Test reading 8 bits
        assertEquals(0b11010001, bitInputStream.readBits(8));

        bitInputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        byte[] bytes = {};
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(-1);
        bitInputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithExceedingCount() throws IOException {
        byte[] bytes = {};
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(64);
        bitInputStream.close();
    }
}
