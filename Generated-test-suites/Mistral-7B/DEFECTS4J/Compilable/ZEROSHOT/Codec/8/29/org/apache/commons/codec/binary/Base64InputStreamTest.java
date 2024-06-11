package org.apache.commons.codec.binary;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    private static final String TEST_DATA = "Hello, World!";

    /**
     * Test constructor without encoding.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testDecodeWithoutEncoding() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[TEST_DATA.length()];
        int length = base64InputStream.read(result);
        Assert.assertEquals(TEST_DATA.length(), length);
        Assert.assertArrayEquals(TEST_DATA.getBytes(), result);
    }

    /**
     * Test constructor with encoding.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testEncodeWithoutDecoding() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4];
        int bytesRead;

        while ((bytesRead = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        byte[] result = outputStream.toByteArray();
        String expectedResult = "VG8gZXhwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgIA==";
        Assert.assertEquals(expectedResult.length(), result.length);
        Assert.assertArrayEquals(expectedResult.getBytes(), result);
    }

    /**
     * Test constructor with encoding and custom line length and separator.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testEncodeWithCustomLineLengthAndSeparator() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 50, "\r\n\r\n".getBytes());
        byte[] buffer = new byte[8];
        int bytesRead;

        while ((bytesRead = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        byte[] result = outputStream.toByteArray();
        String expectedResult = "VG8gZXhwIHlvdSBuZWVkIGEgY29tcHV0ZXIu\r\nCiAg\r\n";
        Assert.assertEquals(expectedResult.length(), result.length);
        Assert.assertArrayEquals(expectedResult.getBytes(), result);
    }

    /**
     * Test markSupported method.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.mark(0);
    }
}