package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class BaseNCodecInputStreamTest {

    /**
     * Tests reading a single byte from the input stream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testReadSingleByte() throws IOException {
        String data = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        BaseNCodecInputStream bnis = new BaseNCodecInputStream(in, new BaseNCodec(), false);

        byte expected = 0;
        byte actual;

        for (int i = 0; i < 256; i++) {
            actual = (byte) bnis.read();
            assertEquals("Expected byte: " + expected + " but got: " + actual, expected, actual);
            expected++;

            if (expected == 256) {
                expected = 0;
            }
        }

        assertEquals("Expected EOF but got: " + bnis.read(), BaseNCodecInputStream.EOF, bnis.read());
    }

    /**
     * Tests reading multiple bytes into an array from the input stream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testReadMultipleBytes() throws IOException {
        String data = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        BaseNCodecInputStream bnis = new BaseNCodecInputStream(in, new BaseNCodec(), false);

        byte[] expected = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B, 0x4C, 0x4D, 0x4E, 0x4F };
        byte[] actual = new byte[12];

        int readLen = bnis.read(actual, 0, expected.length);
        assertArrayEquals("Expected: " + new String(expected) + " but got: " + new String(actual), expected, actual);

        assertEquals("Expected length read: " + expected.length + " but got: " + readLen, expected.length, readLen);
    }

    /**
     * Tests the markSupported() method.
     */
    @Test
    public void testMarkSupported() {
        BaseNCodecInputStream bnis = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        assertFalse("Expected false but got: " + bnis.markSupported(), bnis.markSupported());
    }

    /**
     * Tests the skip() method.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testSkip() throws IOException {
        String data = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        BaseNCodecInputStream bnis = new BaseNCodecInputStream(in, new BaseNCodec(), false);

        bnis.skip(1234);

        byte[] expected = new byte[12];
        byte[] actual = new byte[12];

        int readLen = bnis.read(actual, 0, expected.length);
        assertArrayEquals("Expected: " + new String(expected) + " but got: " + new String(actual), expected, actual);

        assertEquals("Expected 1234 bytes skipped but got: " + bnis.available(), 1234, bnis.available());
    }

    /**
     * Tests the available() method.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testAvailable() throws IOException {
        String data = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        BaseNCodecInputStream bnis = new BaseNCodecInputStream(in, new BaseNCodec(), false);

        assertEquals("Expected length: " + data.length() + " but got: " + bnis.available(), data.length(), bnis.available());

        byte[] buffer = new byte[1024];
        int readLen;
        do {
            readLen = bnis.read(buffer);
        } while (readLen > 0);

        assertEquals("Expected EOF but got: " + bnis.available(), 0, bnis.available());
    }
}