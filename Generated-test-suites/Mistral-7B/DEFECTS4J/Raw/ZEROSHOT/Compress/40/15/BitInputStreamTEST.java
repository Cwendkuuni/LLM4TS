package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test file for BitInputStream class
 */
public class BitInputStreamTest {

    @org.junit.Before
    public void setUp() {
        // Initialize test data
    }

    @org.junit.After
    public void tearDown() {
        // Release resources after test
    }

    @org.junit.Test
    public void testConstructor() {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        assertNotNull(bitInputStream);
        assertEquals(byteOrder, bitInputStream.getByteOrder());
        assertEquals(inputStream, bitInputStream.getInputStream());
    }

    @org.junit.Test
    public void testClose() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.close();
        try {
            bitInputStream.readBits(1);
            fail("Expected IOException after close");
        } catch (IOException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testClearBitCache() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @org.junit.Test
    public void testReadBits() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        // Test reading less than a byte
        long result = bitInputStream.readBits(1);
        assertEquals(0x01 & 0x01, result);

        // Test reading a byte
        result = bitInputStream.readBits(8);
        assertEquals(0x01, result);

        // Test reading more than a byte
        result = bitInputStream.readBits(9);
        assertEquals(0x01 << 1, result);

        // Test reading across byte boundaries
        result = bitInputStream.readBits(11);
        assertEquals(0x02 & 0x03, result);

        // Test reading past the end of the stream
        result = bitInputStream.readBits(17);
        assertEquals(-1, result);

        // Test clearing cache and reading again
        bitInputStream.clearBitCache();
        result = bitInputStream.readBits(1);
        assertEquals(0, result);
    }
}