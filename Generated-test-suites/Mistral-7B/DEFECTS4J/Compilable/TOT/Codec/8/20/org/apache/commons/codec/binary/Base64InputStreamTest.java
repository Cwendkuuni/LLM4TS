package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Test;

/**
 * Test class for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Test encoding and decoding a simple data stream.
     */
    @Test
    public void testSimpleEncodingAndDecoding() throws IOException {
        byte[] input = "HelloWorld".getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64Output = new Base64OutputStream(outputStream, true);
        base64Output.write(input, 0, input.length);
        base64Output.flush();
        byte[] expectedOutput = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(expectedOutput);
        Base64InputStream base64Input = new Base64InputStream(inputStream, false);

        byte[] result = new byte[input.length];
        base64Input.read(result, 0, result.length);
        assertEquals(input, result);
    }

    /**
     * Test decoding an invalid Base64 stream.
     */
    @Test
    public void testInvalidBase64Stream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x80 });
        Base64InputStream base64Input = new Base64InputStream(inputStream, false);
        assertThrows(IOException.class, () -> base64Input.read(new byte[1], 0, 1));
    }

    /**
     * Test encoding a large data stream.
     */
    @Test
    public void testLargeEncoding() throws IOException {
        byte[] input = new byte[1024 * 1024]; // 1MB
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64Output = new Base64OutputStream(outputStream, true, 76, "\r\n".getBytes());
        base64Output.write(input, 0, input.length);
        base64Output.flush();
        byte[] expectedOutput = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(expectedOutput);
        Base64InputStream base64Input = new Base64InputStream(inputStream, false);

        byte[] result = new byte[input.length];
        int bytesRead = 0;
        while (bytesRead < result.length) {
            int numBytesRead = base64Input.read(result, bytesRead, result.length - bytesRead);
            if (numBytesRead <= 0) {
                throw new AssertionError("Failed to read all expected bytes.");
            }
            bytesRead += numBytesRead;
        }

        assertEquals(input, result);
    }

    /**
     * Test decoding a large data stream.
     */
    @Test
    public void testLargeDecoding() throws IOException {
        byte[] input = new byte[1024 * 1024]; // 1MB
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64Output = new Base64OutputStream(outputStream, false, 76, "\r\n".getBytes());
        base64Output.write(input, 0, input.length);
        base64Output.flush();
        byte[] encodedInput = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(encodedInput);
        Base64InputStream base64Input = new Base64InputStream(inputStream, true);

        byte[] result = new byte[input.length];
        int bytesRead = 0;
        while (bytesRead < result.length) {
            int numBytesRead = base64Input.read(result, bytesRead, result.length - bytesRead);
            if (numBytesRead <= 0) {
                throw new AssertionError("Failed to read all expected bytes.");
            }
            bytesRead += numBytesRead;
        }

        assertEquals(input, result);
    }

    /**
     * Test encoding with no line length or line separator.
     */
    @Test
    public void testNoLineLengthOrSeparator() throws IOException {
        byte[] input = "HelloWorld".getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64Output = new Base64OutputStream(outputStream, true, 0, null);
        base64Output.write(input, 0, input.length);
        base64Output.flush();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64Input = new Base64InputStream(inputStream, false);

        byte[] result = new byte[input.length];
        base64Input.read(result, 0, result.length);
        assertEquals(input, result);
    }

    /**
     * Test decoding with no line length or line separator.
     */
    @Test
    public void testNoLineLengthOrSeparatorDecoding() throws IOException {
        byte[] input = new byte[] { 0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x2c, 0x20, 0x57, 0x6f, 0x72, 0x6c, 0x64, 0x21 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64Output = new Base64OutputStream(outputStream, false, 0, null);
        base64Output.write(input, 0, input.length);
        base64Output.flush();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64Input = new Base64InputStream(inputStream, true);

        byte[] result = new byte[input.length];
        base64Input.read(result, 0, result.length);
        assertEquals(input, result);
    }

    /**
     * Test encoding with a null input stream.
     */
    @Test(expected = NullPointerException.class)
    public void testNullInputStreamEncoding() {
        new Base64InputStream(null, true);
    }

    /**
     * Test decoding with a null input stream.
     */
    @Test(expected = NullPointerException.class)
    public void testNullInputStreamDecoding() {
        new Base64InputStream(null, false);
    }

    /**
     * Test encoding with an empty input stream.
     */
    @Test
    public void testEmptyInputStreamEncoding() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64Input = new Base64InputStream(inputStream, true);
        assertNull(base64Input.read(new byte[1], 0, 1));
    }

    /**
     * Test decoding with an empty input stream.
     */
    @Test
    public void testEmptyInputStreamDecoding() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64Input = new Base64InputStream(inputStream, false);
        assertNull(base64Input.read(new byte[1], 0, 1));
    }
}