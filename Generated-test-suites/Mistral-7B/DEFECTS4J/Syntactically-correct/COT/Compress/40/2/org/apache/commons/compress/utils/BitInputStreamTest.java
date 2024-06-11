package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.compress.utils.ByteOrder;
import org.apache.commons.compress.utils.BitInputStream;

public class BitInputStreamTest {

    @org.junit.Test
    public void testBitInputStreamConstruction() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertEquals(ByteOrder.LITTLE_ENDIAN, bitInputStream.byteOrder);
        bitInputStream.close();
    }

    @org.junit.Test
    public void testClearBitCache() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCachedSize);
        assertEquals(0, bitInputStream.bitsCached);
        bitInputStream.close();
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(-1);
        bitInputStream.close();
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testReadBitsGreaterThanMaximumCacheSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(64);
        bitInputStream.close();
    }

    @org.junit.Test
    public void testReadBits() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long expected = 0b1111011011011011L; // Binary representation of bytes 0x02 0x03

        long readBits1 = bitInputStream.readBits(3); // Read 3 bits from first byte
        assertEquals(expected >>> 5, readBits1); // Expected value is 00111, so readBits1 should be 011
        long readBits2 = bitInputStream.readBits(5); // Read next 5 bits from the first byte
        assertEquals(expected >>> 1, readBits2); // Expected value is 0111101, so readBits2 should be 11101
        long readBits3 = bitInputStream.readBits(7); // Read next 7 bits from second byte
        assertEquals(expected, readBits3); // Expected value is 11110110, so readBits3 should be 11110110

        bitInputStream.close();
    }

    @org.junit.Test(expected = IOException.class)
    public void testReadBitsEndOfStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(8);
        bitInputStream.readBits(1);
        inputStream.close(); // Close the input stream
        bitInputStream.readBits(1); // Read a bit after end of stream
    }

}