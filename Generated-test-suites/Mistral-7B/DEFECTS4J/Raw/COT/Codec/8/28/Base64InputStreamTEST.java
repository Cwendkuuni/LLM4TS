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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

import org.junit.Test;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default decoding behavior.
     */
    @Test
    public void testBase64InputStreamDefaultDecoder() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"abc\r\ndef\r\nghij".getBytes()});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expectedOutput = new byte[]{"abc\r\ndef\r\nghij".getBytes()};
        byte[] actualOutput = new byte[1024];
        int length = base64InputStream.read(actualOutput, 0, actualOutput.length);
        assertEquivalentBytes(expectedOutput, actualOutput, 0, length);
    }

    /**
     * Test constructor with encoding behavior.
     */
    @Test
    public void testBase64InputStreamEncoder() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"abc\r\ndef\r\nghij".getBytes()});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int length;

        do {
            length = base64InputStream.read(outputStream.getBuffer(), 0, outputStream.getBuffer().length);
        } while (length > 0);

        byte[] expectedOutput = Base64.encodeBase64(new byte[]{"abc\r\ndef\r\nghij".getBytes()});
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    /**
     * Test constructor with line length and separator.
     */
    @Test
    public void testBase64InputStreamCustomLineLengthAndSeparator() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"abc\r\ndef\r\nghij\r\n".getBytes()});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 72, new byte[]{13, 10});

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int length;

        do {
            length = base64InputStream.read(outputStream.getBuffer(), 0, outputStream.getBuffer().length);
        } while (length > 0);

        byte[] expectedOutput = Base64.encodeBase64LineLength72WithCRLF(new byte[]{"abc\r\ndef\r\nghij\r\n".getBytes()});
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    /**
     * Test the read method when the given InputStream is empty.
     */
    @Test
    public void testBase64InputStreamEmptyInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] actualOutput = new byte[1024];
        int length = base64InputStream.read(actualOutput, 0, actualOutput.length);
        assertEquals(-1, length);
    }

    /**
     * Test the markSupported method.
     */
    @Test
    public void testBase64InputStreamMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertFalse(base64InputStream.markSupported());
    }

    /**
     * Compares two arrays of bytes.
     */
    private void assertEquivalentBytes(byte[] expected, byte[] actual, int offset, int length) {
        for (int i = offset; i < offset + length; i++) {
            assertEquals(expected[i % expected.length], actual[i % actual.length]);
        }
    }
}