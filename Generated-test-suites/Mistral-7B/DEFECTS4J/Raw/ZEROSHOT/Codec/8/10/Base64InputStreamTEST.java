package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    /**
     * Test constructor Base64InputStream(InputStream in)
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBase64InputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"Hello".getBytes()});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expected = new byte[]{"SGVsbG8=".getBytes()};
        byte[] actual = new byte[expected.length];
        int length = base64InputStream.read(actual, 0, expected.length);
        assertArrayEquals("Read incorrect data", expected, actual);
        assertEquals("Length incorrect", expected.length, length);
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode)
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBase64InputStreamDecode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"SGVsbG8=".getBytes()});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] expected = new byte[]{"Hello".getBytes()};
        byte[] actual = new byte[expected.length];
        int length = base64InputStream.read(actual, 0, expected.length);
        assertArrayEquals("Read incorrect data", expected, actual);
        assertEquals("Length incorrect", expected.length, length);
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBase64InputStreamCRLF() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"SGVsbG8=".getBytes()});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, -1, new byte[]{(byte) 0x0D, (byte) 0x0A});

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        do {
            length = base64InputStream.read(buffer, 0, buffer.length);
            if (length > 0) {
                outputStream.write(buffer, 0, length);
            }
        } while (length > 0);

        byte[] expected = new byte[]{"Hello\r\n".getBytes()};
        byte[] actual = outputStream.toByteArray();
        assertArrayEquals("Read incorrect data", expected, actual);
    }

    /**
     * Test method read
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testReadOneByte() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"Hello".getBytes()});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] expected = new byte[]{'H'};
        byte[] actual = new byte[1];
        int length = base64InputStream.read(actual, 0, 1);
        assertArrayEquals("Read incorrect data", expected, actual);
        assertEquals("Length incorrect", 1, length);
    }

    /**
     * Test method read
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testReadBytes() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"Hello".getBytes()});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] expected = new byte[]{"Hello".getBytes()};
        byte[] actual = new byte[expected.length];
        int length = base64InputStream.read(actual, 0, expected.length);
        assertArrayEquals("Read incorrect data", expected, actual);
        assertEquals("Length incorrect", expected.length, length);
    }

    /**
     * Test method markSupported
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}), false);
        base64InputStream.markSupported();
    }
}