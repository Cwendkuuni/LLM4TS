package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class BaseNCodecInputStreamTest {

    private static final byte[] TEST_DATA = new byte[] { -128, -127, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 255, 256, 257 };

    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream(TEST_DATA);
        BaseNCodecInputStream baseNStream = new BaseNCodecInputStream(input, new BaseNCodec(), false);

        byte[] buffer = new byte[TEST_DATA.length];
        int bytesRead = 0;
        while (bytesRead < TEST_DATA.length) {
            int readByte = baseNStream.read();
            if (readByte >= 0) {
                buffer[bytesRead++] = (byte) readByte;
            }
        }

        assertArrayEquals(TEST_DATA, buffer);
    }

    @Test
    public void testReadWithEofReached() throws IOException {
        InputStream input = new ByteArrayInputStream(TEST_DATA);
        BaseNCodecInputStream baseNStream = new BaseNCodecInputStream(input, new BaseNCodec(), false);

        byte[] buffer = new byte[TEST_DATA.length + 1];
        int bytesRead = 0;
        while (bytesRead < buffer.length - 1) {
            int readByte = baseNStream.read();
            if (readByte >= 0) {
                buffer[bytesRead++] = (byte) readByte;
            }
        }

        assertEquals(BaseNCodecInputStream.EOF, baseNStream.read()); // EOF should be read after all other bytes
    }

    @Test
    public void testReadWithLargeChunkSize() throws IOException {
        InputStream input = new ByteArrayInputStream(TEST_DATA);
        BaseNCodecInputStream baseNStream = new BaseNCodecInputStream(input, new BaseNCodec(), false);

        byte[] buffer = new byte[TEST_DATA.length * 2];
        int bytesRead = baseNStream.read(buffer, 0, buffer.length);

        assertArrayEquals(TEST_DATA, buffer, 0, TEST_DATA.length);
    }

    @Test
    public void testMarkAndReset() throws IOException {
        InputStream input = new ByteArrayInputStream(TEST_DATA);
        BaseNCodecInputStream baseNStream = new BaseNCodecInputStream(input, new BaseNCodec(), false);

        baseNStream.mark(0);
        int bytesRead = baseNStream.read(new byte[1], 0, 1);

        assertFalse(baseNStream.markSupported());

        baseNStream.reset();

        int readAgain = baseNStream.read(new byte[1], 0, 1);
        assertEquals(bytesRead, readAgain); // should return the same byte as before marking
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(TEST_DATA);
        BaseNCodecInputStream baseNStream = new BaseNCodecInputStream(input, new BaseNCodec(), false);

        baseNStream.skip(TEST_DATA.length - 1);

        byte[] buffer = new byte[1];
        int bytesRead = baseNStream.read(buffer, 0, 1);

        assertEquals(-1, bytesRead); // EOF should be reached after skipping the last byte
    }
}