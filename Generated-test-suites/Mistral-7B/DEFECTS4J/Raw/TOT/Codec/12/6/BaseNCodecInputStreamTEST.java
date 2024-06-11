package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Tests for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test reading a single byte using the InputStream.
     *
     * @throws IOException
     *             if an error occurs
     */
    @org.junit.Test
    public void testReadSingleByte() throws IOException {
        byte[] input = "foo".getBytes();
        BaseNCodec codec = new Base64();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(input), codec, false);
        assertEquals((int) 'f', inputStream.read());
        assertEquals((int) 'o', inputStream.read());
        assertEquals((int) 'o', inputStream.read());
        assertEquals(EOF, inputStream.read());
    }

    /**
     * Test reading multiple bytes using the InputStream.
     *
     * @throws IOException
     *             if an error occurs
     */
    @org.junit.Test
    public void testReadMultipleBytes() throws IOException {
        byte[] input = "foo".getBytes();
        BaseNCodec codec = new Base64();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream testInput = new ByteArrayInputStream(input);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(testInput, codec, false);

        byte[] expected = new byte[3];
        int read = inputStream.read(expected, 0, 3);
        assertNotNull(expected);
        assertTrue(read == 3);
        assertEquals(expected[0], 'f');
        assertEquals(expected[1], 'o');
        assertEquals(expected[2], 'o');
    }

    /**
     * Test reading multiple bytes with an empty input stream.
     *
     * @throws IOException
     *             if an error occurs
     */
    @org.junit.Test
    public void testReadMultipleBytesWithEmptyInputStream() throws IOException {
        BaseNCodec codec = new Base64();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), codec, false);
        byte[] expected = new byte[0];
        int read = inputStream.read(expected, 0, 0);
        assertNotNull(expected);
        assertTrue(read == 0);
    }

    /**
     * Test reading multiple bytes with a too-large buffer size.
     *
     * @throws IOException
     *             if an error occurs
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testReadMultipleBytesWithTooLargeBufferSize() throws IOException {
        byte[] input = "foo".getBytes();
        BaseNCodec codec = new Base64();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream testInput = new ByteArrayInputStream(input);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(testInput, codec, false);

        byte[] buffer = new byte[input.length + 1];
        inputStream.read(buffer, 0, input.length + 1);
    }

    /**
     * Test reading multiple bytes with the InputStream marking not supported.
     *
     * @throws IOException
     *             if an error occurs
     */
    @org.junit.Test
    public void testReadMultipleBytesWithMarkNotSupported() throws IOException {
        byte[] input = "foo".getBytes();
        BaseNCodec codec = new Base64();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream testInput = new ByteArrayInputStream(input);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(testInput, codec, false);

        inputStream.mark(0);
        inputStream.read(new byte[1], 0, 1);
        inputStream.reset();
        inputStream.read(new byte[1], 0, 1);

        inputStream.mark(0);
        int read = inputStream.read(new byte[3], 0, 3);
        assertFalse(inputStream.markSupported());
    }

    /**
     * Test reading multiple bytes with the InputStream encoding.
     *
     * @throws IOException
     *             if an error occurs
     */
    @org.junit.Test
    public void testReadMultipleBytesWithEncoding() throws IOException {
        byte[] input = "foo".getBytes();
        BaseNCodec codec = new BaseNCodec();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream testInput = new ByteArrayInputStream(input);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(testInput, codec, true);

        byte[] expected = new byte[3];
        int read = inputStream.read(expected, 0, 3);
        assertNotNull(expected);
        assertTrue(read == 3);
        assertEquals(expected[0], 'f');
        assertEquals(expected[1], 'o');
        assertEquals(expected[2], 'o');

        byte[] decoded = new byte[input.length];
        inputStream.read(decoded, 0, input.length);
        assertEquals(input, decoded);
    }

    /**
     * Test reading multiple bytes with the InputStream decoding.
     *
     * @throws IOException
     *             if an error occurs
     */
    @org.junit.Test
    public void testReadMultipleBytesWithDecoding() throws IOException {
        byte[] input = Base64.decodeBase64("ZGVmYXVsdGltX2lvdSBkYXRl");
        BaseNCodec codec = new BaseNCodec();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream testInput = new ByteArrayInputStream(input);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(testInput, codec, false);

        byte[] expected = new byte[3];
        int read = inputStream.read(expected, 0, 3);
        assertNotNull(expected);
        assertTrue(read == 3);
        assertEquals(expected[0], 'f');
        assertEquals(expected[1], 'o');
        assertEquals(expected[2], 'o');

        byte[] decoded = new byte[input.length];
        inputStream.read(decoded, 0, input.length);
        assertEquals(input, decoded);
    }

    /**
     * Test reading multiple bytes with a too-large data chunk size.
     *
     * @throws IOException
     *             if an error occurs
     */
    @org.junit.Test
    public void testReadMultipleBytesWithTooLargeDataChunkSize() throws IOException {
        byte[] input = "foo".getBytes();
        BaseNCodec codec = new BaseNCodec();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream testInput = new ByteArrayInputStream(input);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(testInput, codec, false);

        byte[] buffer = new byte[4097];
        inputStream.read(buffer, 0, 4097);
    }

    /**
     * Test the InputStream closing.
     *
     * @throws IOException
     *             if an error occurs
     */
    @org.junit.After
    public void tearDown() throws IOException {
        if (in != null) {
            in.close();
            in = null;
        }
    }

    /**
     * Test the InputStream marking.
     *
     * @throws IOException
     *             if an error occurs
     */
    @org.junit.Test
    public void testMarkSupported() throws IOException {
        byte[] input = "foo".getBytes();
        BaseNCodec codec = new BaseNCodec();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream testInput = new ByteArrayInputStream(input);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(testInput, codec, false);

        inputStream.mark(0);
        inputStream.read(new byte[1], 0, 1);
        inputStream.reset();
        inputStream.read(new byte[1], 0, 1);

        assertFalse(inputStream.markSupported());
    }
}