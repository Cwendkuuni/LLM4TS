package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testReadBits() throws IOException {
        byte[] bytes = {0x12, 0x34, 0x56, 0x78};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Read 8 bits, should return 0x12
        long result = bitInputStream.readBits(8);
        assertEquals(0x12, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsCountNegative() throws IOException {
        byte[] bytes = {0x12, 0x34, 0x56, 0x78};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Try to read -1 bits, should throw IllegalArgumentException
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsCountGreaterThanMaximum() throws IOException {
        byte[] bytes = {0x12, 0x34, 0x56, 0x78};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Try to read 64 bits, should throw IllegalArgumentException
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        byte[] bytes = {0x12};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Read 8 bits, should return 0x12
        long result = bitInputStream.readBits(8);
        assertEquals(0x12, result);

        // Read 8 bits again, should return -1 since end of stream has been reached
        result = bitInputStream.readBits(8);
        assertEquals(-1, result);
    }
}
