package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for Base64InputStream class.
 */
public class Base64InputStreamTest {

    private static final String TEST_BASE64_ENCODED = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgUm9sbG93ID0gZGlyZWN0IHdpdGggPSBlYXNlIHNpbXBsZSBoYXZlIHRoZSBmaWxlIElucHV0IHBsYW5ldC4K";
    private static final String TEST_BASE64_DECODED = "This is a test";
    private static final String TEST_LINE_SEPARATOR = "\r\n";

    private Base64 base64;
    private Base64InputStream inputStream;
    private Base64OutputStream outputStream;
    private InputStream testInputStream;
    private ByteArrayInputStream boneInputStream;
    private ByteArrayOutputStream boneOutputStream;

    @Before
    public void setUp() throws IOException {
        base64 = new Base64();
        outputStream = new Base64OutputStream(new ByteArrayOutputStream());
        testInputStream = new ByteArrayInputStream(TEST_BASE64_ENCODED.getBytes());
        boneInputStream = new ByteArrayInputStream(new byte[0]);
        boneOutputStream = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }

    /**
     * Test constructor without any arguments.
     * @throws IOException
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        inputStream = new Base64InputStream(testInputStream);
        assertEquals(Base64.DECODE, inputStream.doEncode);
        assertNull(inputStream.lineLength);
        assertNull(inputStream.lineSeparator);
    }

    /**
     * Test constructor when decoding is required.
     * @throws IOException
     */
    @Test
    public void testConstructorWhenDecoding() throws IOException {
        inputStream = new Base64InputStream(testInputStream, false);
        assertEquals(Base64.DECODE, inputStream.doEncode);
        assertNull(inputStream.lineLength);
        assertNull(inputStream.lineSeparator);
    }

    /**
     * Test constructor when encoding is required.
     * @throws IOException
     */
    @Test
    public void testConstructorWhenEncoding() throws IOException {
        inputStream = new Base64InputStream(testInputStream, true);
        assertEquals(Base64.ENCODE, inputStream.doEncode);
        assertNull(inputStream.lineLength);
        assertNull(inputStream.lineSeparator);
    }

    /**
     * Test constructor with specified line length and line separator.
     * @throws IOException
     */
    @Test
    public void testConstructorWithLineLengthAndLineSeparator() throws IOException {
        inputStream = new Base64InputStream(testInputStream, false, 76, TEST_LINE_SEPARATOR.getBytes());
        assertEquals(Base64.DECODE, inputStream.doEncode);
        assertEquals(76, inputStream.lineLength.intValue());
        assertArrayEquals(TEST_LINE_SEPARATOR.getBytes(), inputStream.lineSeparator);
    }

    /**
     * Test read method when decoding is required.
     * @throws IOException
     */
    @Test
    public void testReadWhenDecoding() throws IOException {
        byte[] result = new byte[TEST_BASE64_DECODED.length()];
        int read = inputStream.read(result, 0, result.length);
        assertEquals(TEST_BASE64_DECODED.length(), read);
        assertArrayEquals(TEST_BASE64_DECODED.getBytes(), result);
    }

    /**
     * Test read method when decoding with line length and line separator.
     * @throws IOException
     */
    @Test
    public void testReadWithLineLengthAndLineSeparator() throws IOException {
        byte[] result = new byte[TEST_BASE64_DECODED.length()];
        inputStream = new Base64InputStream(testInputStream, false, 76, TEST_LINE_SEPARATOR.getBytes());
        int read = inputStream.read(result, 0, result.length);
        assertEquals(TEST_BASE64_DECODED.length(), read);
        assertArrayEquals(TEST_BASE64_DECODED.getBytes(), result);
    }

    /**
     * Test read method when decoding with empty input.
     * @throws IOException
     */
    @Test
    public void testReadWithEmptyInput() throws IOException {
        byte[] result = new byte[0];
        int read = inputStream.read(result, 0, result.length);
        assertEquals(0, read);
    }

    /**
     * Test read method when decoding with null input.
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testReadWithNullInput() throws IOException {
        byte[] result = null;
        inputStream.read(result, 0, result.length);
    }

    /**
     * Test read method when encoding is required.
     * @throws IOException
     */
    @Test
    public void testReadWhenEncoding() throws IOException {
        outputStream.write(TEST_BASE64_ENCODED.getBytes());
        outputStream.flush();
        byte[] result = new byte[TEST_BASE64_ENCODED.length()];
        int read = inputStream.read(result, 0, result.length);
        assertEquals(TEST_BASE64_ENCODED.length(), read);
        assertArrayEquals(TEST_BASE64_ENCODED.getBytes(), result);
    }

    /**
     * Test read method when encoding with empty input.
     * @throws IOException
     */
    @Test
    public void testReadWithEmptyInputWhenEncoding() throws IOException {
        byte[] result = new byte[0];
        int read = inputStream.read(result, 0, result.length);
        assertEquals(0, read);
    }

    /**
     * Test read method when encoding with null input.
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testReadWithNullInputWhenEncoding() throws IOException {
        byte[] result = null;
        inputStream.read(result, 0, result.length);
    }

    /**
     * Test markSupported method.
     */
    @Test
    public void testMarkSupported() {
        assertFalse(inputStream.markSupported());
    }

    /**
     * Test read method when decoding with wrong input.
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testReadWithWrongInput() throws IOException {
        byte[] wrongInput = new byte[2];
        inputStream.read(wrongInput, 0, wrongInput.length);
    }

    /**
     * Test read method when decoding with wrong input length.
     * @throws IOException
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithWrongInputLength() throws IOException {
        byte[] result = new byte[TEST_BASE64_DECODED.length() + 1];
        int read = inputStream.read(result, 0, result.length);
    }
}