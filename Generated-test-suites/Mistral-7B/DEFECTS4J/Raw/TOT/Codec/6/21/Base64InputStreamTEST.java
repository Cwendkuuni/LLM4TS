package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

/**
 * Test cases for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default parameters.
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);

        byte[] expectedOutput = Base64.encodeBase64("Hello World!".getBytes());

        byte[] output = new byte[expectedOutput.length];
        int read = base64InputStream.read(output, 0, expectedOutput.length);
        assertFalse(base64InputStream.hasNextLine());

        assertEquals(new String(expectedOutput), new String(output, 0, read));
    }

    /**
     * Test constructor with encoding enabled.
     */
    @org.junit.Test
    public void testEncodingConstructor() {
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, true);

        byte[] expectedOutput = Base64.encodeBase64("Hello World!".getBytes());

        byte[] output = new byte[expectedOutput.length];
        int read = base64InputStream.read(output, 0, expectedOutput.length);
        assertFalse(base64InputStream.hasNextLine());

        assertEquals(new String(expectedOutput), new String(output, 0, read));
    }

    /**
     * Test constructor with decoding enabled and standard line length and line separator.
     */
    @org.junit.Test
    public void testDecodingConstructor() throws IOException {
        InputStream input = new ByteArrayInputStream(Base64.decodeBase64("SGVsbG8sIFdvcmxkIQ=="));
        Base64InputStream base64InputStream = new Base64InputStream(input);

        byte[] expectedOutput = "Hello World!".getBytes();

        byte[] output = new byte[expectedOutput.length];
        int read = base64InputStream.read(output, 0, expectedOutput.length);
        assertTrue(base64InputStream.hasNextLine());

        assertEquals(expectedOutput.length, read);
        assertEquals(new String(expectedOutput), new String(output, 0, read));
        assertEquals("\r\n", base64InputStream.readLine());

        assertFalse(base64InputStream.hasNextLine());
    }

    /**
     * Test constructor with decoding enabled and custom line length and line separator.
     */
    @org.junit.Test
    public void testDecodingCustomConstructor() throws IOException {
        InputStream input = new ByteArrayInputStream(Base64.decodeBase64("SGVsbG8sIHdvcmxkIQ==\r\n"));
        Base64InputStream base64InputStream = new Base64InputStream(input, true, 72, "\r\n\r\n".getBytes());

        byte[] expectedOutput = "Hello World!".getBytes();

        byte[] output = new byte[expectedOutput.length];
        int read = base64InputStream.read(output, 0, expectedOutput.length);
        assertTrue(base64InputStream.hasNextLine());

        assertEquals(expectedOutput.length, read);
        assertEquals(new String(expectedOutput), new String(output, 0, read));
        assertEquals("\r\n\r\n", base64InputStream.readLine());

        assertFalse(base64InputStream.hasNextLine());
    }

    /**
     * Test reading from input stream when no data is available.
     */
    @org.junit.Test
    public void testReadWhenNoDataIsAvailable() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(input);

        byte[] output = new byte[1];

        int read = base64InputStream.read(output, 0, 1);
        assertFalse(base64InputStream.hasNextLine());

        assertEquals(-1, read);
    }

    /**
     * Test marking the input stream.
     */
    @org.junit.Test(expected = IOException.class)
    public void testMarkingTheInputStream() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);

        base64InputStream.mark(0);
        base64InputStream.read();
        base64InputStream.reset();
        base64InputStream.read();
    }
}