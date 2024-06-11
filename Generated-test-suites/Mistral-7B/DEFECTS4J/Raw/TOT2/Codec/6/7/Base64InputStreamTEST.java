package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Test;

/**
 * Test cases for Base64InputStream class.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor without arguments.
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        assertEquals(Base64.DEFAULT, inputStream.getBase64());
        assertEquals(false, inputStream.isEncode());
        assertNull(inputStream.getLineLength());
        assertNull(inputStream.getLineSeparator());
    }

    /**
     * Test constructor with decode flag.
     */
    @Test
    public void testDecodeConstructor() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] inputData = "VG8gZXJyIGlzIGh1bWFuLCB7IGRhdGFbMF0gT1MgW1s=".getBytes();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        base64OutputStream.write(inputData, 0, inputData.length);
        base64OutputStream.finish();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Base64InputStream testInput = new Base64InputStream(inputStream);
        assertEquals(Base64.DEFAULT, testInput.getBase64());
        assertEquals(false, testInput.isEncode());
        assertNull(testInput.getLineLength());
        assertNull(testInput.getLineSeparator());

        byte[] resultData = new byte[inputData.length];
        int length = testInput.read(resultData);
        assertEqualByteArrays(inputData, resultData, 0, inputData.length);

        testInput.close();
        base64OutputStream.close();
        inputStream.close();
        outputStream.close();
    }

    /**
     * Test constructor with encode flag.
     */
    @Test
    public void testEncodeConstructor() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] inputData = "Hello World!".getBytes();
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(inputData), true);
        Base64 base64 = new Base64(76, new byte[] { '\r', '\n' });
        Base64InputStream testInput = new Base64InputStream(new ByteArrayInputStream(inputData), true, base64.getLineLength(), base64.getLineSeparator());

        assertEquals(base64, testInput.getBase64());
        assertEquals(true, testInput.isEncode());
        assertEquals(76, testInput.getLineLength().intValue());
        assertEquals(new byte[] { '\r', '\n' }, testInput.getLineSeparator());

        ByteArrayOutputStream resultOutput = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(resultOutput);
        testInput.writeTo(base64OutputStream);
        base64OutputStream.finish();

        assertEqualByteArrays(resultOutput.toByteArray(), inputData, 0, inputData.length);

        testInput.close();
        base64InputStream.close();
        base64OutputStream.close();
        outputStream.close();
        resultOutput.close();
    }

    /**
     * Test constructor with invalid input stream.
     */
    @Test
    public void testInvalidConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new Base64InputStream(null));
    }

    /**
     * Test read method with empty input stream.
     */
    @Test
    public void testEmptyInputStream() throws IOException {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        byte[] resultData = new byte[0];
        int length = inputStream.read(resultData, 0, resultData.length);
        assertEquals(-1, length);

        inputStream.close();
    }

    /**
     * Test read method with non-empty input stream.
     */
    @Test
    public void testReadMethod() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] inputData = "VG8gZXJyIGlzIGh1bWFuLCB7IGRhdGFbMF0gT1MgW1s=".getBytes();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        base64OutputStream.write(inputData, 0, inputData.length);
        base64OutputStream.finish();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Base64InputStream testInput = new Base64InputStream(inputStream);
        byte[] resultData = new byte[inputData.length];
        int length = testInput.read(resultData);
        assertEqualByteArrays(inputData, resultData, 0, inputData.length);

        testInput.close();
        base64OutputStream.close();
        inputStream.close();
        outputStream.close();
    }

    /**
     * Test writeTo method with empty output stream.
     */
    @Test
    public void testEmptyOutputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        Base64InputStream testInput = new Base64InputStream(inputStream, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        testInput.writeTo(outputStream);
        assertEquals(-1, outputStream.size());

        testInput.close();
        inputStream.close();
        outputStream.close();
    }

    /**
     * Test writeTo method with non-empty output stream.
     */
    @Test
    public void testWriteToMethod() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Base64InputStream testInput = new Base64InputStream(inputStream, true);
        testInput.writeTo(outputStream);

        byte[] resultData = outputStream.toByteArray();
        String expectedResult = "SGVsbG8sIFdvcmxkIQ==";
        assertEquals(expectedResult, new String(resultData, "US-ASCII"));

        testInput.close();
        inputStream.close();
        outputStream.close();
    }

    /**
     * Test markSupported method.
     */
    @Test
    public void testMarkSupportedMethod() {
        Base64InputStream testInput = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        assertFalse(testInput.markSupported());
        testInput.close();
    }

    /**
     * Compares two arrays for equality.
     *
     * @param a
     *            first array
     * @param b
     *            second array
     * @param offset
     *            offset in the arrays
     * @param length
     *            number of bytes to compare
     */
    private void assertEqualByteArrays(byte[] a, byte[] b, int offset, int length) {
        for (int i = 0; i < length; i++) {
            assertEquals(a[offset + i], b[offset + i]);
        }
    }
}