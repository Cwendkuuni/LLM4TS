package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testDecodeBase64InputStream() throws IOException {
        byte[] inputBytes = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x3D, 0x3D, 0x3D, 0x00 };
        InputStream inputStream = new ByteArrayInputStream(inputBytes);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] outputBytes = new byte[inputBytes.length];

        int bytesRead = 0;
        while ((bytesRead = base64InputStream.read(outputBytes, 0, outputBytes.length)) > 0) {
            outputStream.write(outputBytes, 0, bytesRead);
        }

        byte[] expectedOutputBytes = new byte[] { 0x41, 0x42, 0x43, 0x44 };
        assertArrayEquals(expectedOutputBytes, outputStream.toByteArray());
    }

    @Test
    public void testDecodeBase64InputStreamWithLineLengthAndLineSeparator() throws IOException {
        byte[] inputBytes = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x3D, 0x3D, 0x3D, 0x0D, 0x0A, 0x2D, 0x2D, 0x2D,
                0x44, 0x45, 0x46, 0x47 };
        InputStream inputStream = new ByteArrayInputStream(inputBytes);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 76, "\r\n".getBytes());
        byte[] outputBytes = new byte[inputBytes.length];

        int bytesRead = 0;
        while ((bytesRead = base64InputStream.read(outputBytes, 0, outputBytes.length)) > 0) {
            outputStream.write(outputBytes, 0, bytesRead);
        }

        byte[] expectedOutputBytes = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47 };
        assertArrayEquals(expectedOutputBytes, outputStream.toByteArray());
    }

    @Test
    public void testEncodeBase64InputStream() throws IOException {
        byte[] inputBytes = new byte[] { 0x41, 0x42, 0x43, 0x44 };
        InputStream inputStream = new ByteArrayInputStream(inputBytes);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        int bytesRead = 0;
        byte[] readBytes = new byte[doEncode ? 4096 : 8192];
        while ((bytesRead = base64InputStream.read(readBytes, 0, readBytes.length)) > 0) {
            outputStream.write(readBytes, 0, bytesRead);
        }

        byte[] expectedOutputBytes = new byte[] { 0x40, 0x41, 0x41, 0x41, 0x3D, 0x3D, 0x3D, 0x3D, 0x00 };
        assertArrayEquals(expectedOutputBytes, outputStream.toByteArray());
    }

    @Test
    public void testDecodeBase64InputStreamWithInitialBuffer() throws IOException {
        byte[] inputBytes = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x3D, 0x3D, 0x3D, 0x00 };
        InputStream inputStream = new ByteArrayInputStream(inputBytes);

        byte[] outputBytes = new byte[4];
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        base64InputStream.read(outputBytes, 0, outputBytes.length);
        base64InputStream.read(outputBytes, 0, outputBytes.length);
        base64InputStream.read(outputBytes, 2, 2);

        byte[] expectedOutputBytes = new byte[] { 0x41, 0x42, 0x43, 0x44 };
        assertArrayEquals(expectedOutputBytes, outputBytes);
    }

    @Test
    public void testDecodeBase64InputStreamWithPartialData() throws IOException {
        byte[] inputBytes = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x3D, 0x3D, 0x3D, 0x0D, 0x0A, 0x2D, 0x2D, 0x2D,
                0x44, 0x45, 0x46, 0x47 };
        InputStream inputStream = new ByteArrayInputStream(inputBytes);

        byte[] outputBytes = new byte[inputBytes.length];
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        int bytesRead = base64InputStream.read(outputBytes, 0, outputBytes.length);
        assertEquals(bytesRead, 11);

        // Check the first part of the data
        byte[] expectedOutputBytesFirstPart = new byte[] { 0x41, 0x42, 0x43, 0x44 };
        assertArrayEquals(expectedOutputBytesFirstPart, Arrays.copyOfRange(outputBytes, 0, 4));

        // Check the last part of the data
        byte[] expectedOutputBytesLastPart = new byte[] { 0x45, 0x46, 0x47 };
        assertArrayEquals(expectedOutputBytesLastPart, Arrays.copyOfRange(outputBytes, 11, 14));

        // Check that the remaining data is not decoded properly
        assertNull(base64InputStream.read(outputBytes, 4, 11));
    }

    @Test(expected = NullPointerException.class)
    public void testDecodeBase64InputStreamWithNullByteArray() throws IOException {
        byte[] outputBytes = null;
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}), false);
        base64InputStream.read(outputBytes, 0, outputBytes.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDecodeBase64InputStreamWithNegativeByteArrayIndex() throws IOException {
        byte[] outputBytes = new byte[5];
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}), false);
        base64InputStream.read(outputBytes, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDecodeBase64InputStreamWithNegativeOutputByteIndex() throws IOException {
        byte[] outputBytes = new byte[5];
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}), false);
        base64InputStream.read(outputBytes, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDecodeBase64InputStreamWithNegativeOutputByteLength() throws IOException {
        byte[] outputBytes = new byte[5];
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}), false);
        base64InputStream.read(outputBytes, 0, outputBytes.length + 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDecodeBase64InputStreamWithNegativeInputByteArrayIndex() throws IOException {
        byte[] inputBytes = new byte[] { 0x41, 0x42, 0x43, 0x44 };
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[inputBytes.length];
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        base64InputStream.read(outputBytes, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDecodeBase64InputStreamWithNegativeInputByteLength() throws IOException {
        byte[] inputBytes = new byte[] { 0x41, 0x42, 0x43, 0x44 };
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[inputBytes.length];
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        base64InputStream.read(outputBytes, 0, inputBytes.length + 1);
    }
}