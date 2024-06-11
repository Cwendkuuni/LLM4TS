package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class BitInputStreamTest {

    @Test
    public void testReadBits() throws IOException {
        byte[] testData = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
        InputStream inputStream = new ByteArrayInputStream(testData);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // Test reading 1 bit
        assertEquals(1, bitInputStream.readBits(1));

        // Test reading multiple bits from a single byte
        assertEquals(128 | 64, bitInputStream.readBits(2)); // read MSB 2 bits from first byte
        assertEquals(32, bitInputStream.readBits(3)); // read 3 LSB bits from first byte
        assertEquals(0, bitInputStream.readBits(33)); // read 1 bit from first byte, then reach end of stream

        // Test reading multiple bits across multiple bytes
        bitInputStream.clearBitCache(); // clear cache after first read
        assertEquals(1 | (1 << 1), bitInputStream.readBits(2)); // read MSB 1 bit and LSB 1 bit from second byte
        assertEquals(128, bitInputStream.readBits(7)); // read 7 LSB bits from second byte

        // Test reading at the end of the stream
        bitInputStream.clearBitCache();
        assertEquals(-1, bitInputStream.readBits(1));
    }
}