package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Tests for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Tests that {@link BaseNCodecInputStream#read()} returns -1 when EOF is reached.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testReadEof() throws IOException {
        BaseNCodec codec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        InputStream input = new ByteArrayInputStream(new byte[] { BaseNCodec.EOF });
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);
        assertEquals(-1, inputStream.read());
    }

    /**
     * Tests that {@link BaseNCodecInputStream#read(byte[], int, int)} reads the expected number of bytes.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testReadBytes() throws IOException {
        BaseNCodec codec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(BaseNCodec.BYTES_BASE32_ENCODED_3);
        output.write(BaseNCodec.BYTES_BASE32_ENCODED_4);
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        byte[] buffer = new byte[5];
        int len = input.read(buffer);
        ByteArrayOutputStream output2 = new ByteArrayOutputStream();
        output2.write(buffer, 0, len);
        byte[] expected = BaseNCodec.BYTES_BASE32_ENCODED_3_AND_4.getBytes();
        assertArrayEquals(expected, output2.toByteArray());
    }

    /**
     * Tests that {@link BaseNCodecInputStream#markSupported()} returns false.
     */
    @org.junit.Test
    public void testMarkSupported() {
        BaseNCodec codec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), codec, false);
        assertFalse(inputStream.markSupported());
    }

    /**
     * Tests that {@link BaseNCodecInputStream#skip(long)} skips bytes in chunks of 512.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testSkipBytes() throws IOException {
        BaseNCodec codec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(BaseNCodec.BYTES_BASE32_ENCODED_1_TO_10);
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);
        long amount = 511;
        inputStream.skip(amount);
        assertEquals(BaseNCodec.BASE32_ENCODED_LENGTH_10 - amount, inputStream.available());
    }

    /**
     * Tests that {@link BaseNCodecInputStream#available()} returns the number of available bytes.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testAvailableBytes() throws IOException {
        BaseNCodec codec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(BaseNCodec.BYTES_BASE32_ENCODED_1_TO_10);
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);
        assertEquals(BaseNCodec.BASE32_ENCODED_LENGTH_10, inputStream.available());
    }

    /**
     * Tests that {@link BaseNCodecInputStream#close()} closes the stream.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testClose() throws IOException {
        BaseNCodec codec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);
        inputStream.close();
        assertNull(inputStream.in);
    }

    /**
     * Tests that {@link BaseNCodecInputStream#read(byte[], int, int)} returns the expected number of decoded bytes.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testReadDecodedBytes() throws IOException {
        BaseNCodec codec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(BaseNCodec.BYTES_BASE32_ENCODED_3_AND_4);
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        byte[] buffer = new byte[5];
        int read = new BaseNCodecInputStream(input, codec, false).read(buffer);
        assertEquals(BaseNCodec.BYTES_BASE32_ENCODED_3.length + BaseNCodec.BYTES_BASE32_ENCODED_4.length, read);
    }

    /**
     * Tests that {@link BaseNCodecInputStream#read(byte[], int, int)} decodes the expected bytes.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testReadDecodedBytesExpected() throws IOException {
        BaseNCodec codec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        byte[] input = BaseNCodec.BYTES_BASE32_ENCODED_3_AND_4;
        byte[] output = new byte[BaseNCodec.BYTES_BASE32_ENCODED_3.length + BaseNCodec.BYTES_BASE32_ENCODED_4.length];
        int read = new BaseNCodecInputStream(new ByteArrayInputStream(input), codec, false).read(output);
        byte[] expected = BaseNCodec.BYTES_3_AND_4;
        assertArrayEquals(expected, output, 0, read);
    }

    /**
     * Tests that {@link BaseNCodecInputStream#read()} returns the expected decoded byte.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testReadDecodedByte() throws IOException {
        BaseNCodec codec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER);
        byte[] input = BaseNCodec.BYTES_BASE32_ENCODED_3;
        byte[] output = new byte[BaseNCodec.BYTES_BASE32_ENCODED_3.length];
        int read = new BaseNCodecInputStream(new ByteArrayInputStream(input), codec, false).read(output);
        assertEquals(BaseNCodec.BYTE_3, output[0]);
    }
}