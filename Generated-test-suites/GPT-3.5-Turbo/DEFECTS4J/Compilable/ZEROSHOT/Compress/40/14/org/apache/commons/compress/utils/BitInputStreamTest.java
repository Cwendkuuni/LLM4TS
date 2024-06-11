package org.apache.commons.compress.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testReadBits() throws IOException {
        // Test reading zero bits
        InputStream dummyStream = new InputStream() {
            public int read() throws IOException {
                return -1;
            }
        };
        BitInputStream bitInputStream = new BitInputStream(dummyStream, ByteOrder.BIG_ENDIAN);
        long result = bitInputStream.readBits(0);
        assertEquals(-1, result);

        // Test reading positive number of bits
        byte[] inputBytes = {0x12, 0x34, 0x56, 0x78};
        dummyStream = new InputStream() {
            private int index = 0;

            public int read() throws IOException {
                return index < inputBytes.length ? inputBytes[index++] & 0xff : -1;
            }
        };
        bitInputStream = new BitInputStream(dummyStream, ByteOrder.BIG_ENDIAN);
        result = bitInputStream.readBits(8);
        assertEquals(0x12, result);
        result = bitInputStream.readBits(16);
        assertEquals(0x1234, result);
        result = bitInputStream.readBits(24);
        assertEquals(0x123456, result);
        result = bitInputStream.readBits(32);
        assertEquals(0x12345678, result);

        // Test reading more bits than available
        dummyStream = new InputStream() {
            private int index = 0;

            public int read() throws IOException {
                return index < inputBytes.length ? inputBytes[index++] & 0xff : -1;
            }
        };
        bitInputStream = new BitInputStream(dummyStream, ByteOrder.BIG_ENDIAN);
        result = bitInputStream.readBits(50);
        assertEquals(-1, result);

        // Test reading with LITTLE_ENDIAN byte order
        dummyStream = new InputStream() {
            private int index = 0;

            public int read() throws IOException {
                return index < inputBytes.length ? inputBytes[index++] & 0xff : -1;
            }
        };
        bitInputStream = new BitInputStream(dummyStream, ByteOrder.LITTLE_ENDIAN);
        result = bitInputStream.readBits(16);
        assertEquals(0x3412, result);
        result = bitInputStream.readBits(24);
        assertEquals(0x785634, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        InputStream dummyStream = new InputStream() {
            public int read() throws IOException {
                return -1;
            }
        };
        BitInputStream bitInputStream = new BitInputStream(dummyStream, ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThanMaxSize() throws IOException {
        InputStream dummyStream = new InputStream() {
            public int read() throws IOException {
                return -1;
            }
        };
        BitInputStream bitInputStream = new BitInputStream(dummyStream, ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(64);
    }

    @Test
    public void testClearBitCache() throws IOException {
        byte[] inputBytes = {0x12, 0x34, 0x56, 0x78};
        InputStream dummyStream = new InputStream() {
            private int index = 0;

            public int read() throws IOException {
                return index < inputBytes.length ? inputBytes[index++] & 0xff : -1;
            }
        };
        BitInputStream bitInputStream = new BitInputStream(dummyStream, ByteOrder.BIG_ENDIAN);
        long result = bitInputStream.readBits(16);
        assertEquals(0x1234, result);
        bitInputStream.clearBitCache();
        result = bitInputStream.readBits(16);
        assertEquals(0x1234, result);
    }
}