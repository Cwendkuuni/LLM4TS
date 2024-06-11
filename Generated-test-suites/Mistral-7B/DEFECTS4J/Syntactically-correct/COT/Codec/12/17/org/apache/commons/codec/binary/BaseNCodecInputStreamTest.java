package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

/**
 * Test class for BaseNCodecInputStream.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor.
     *
     * @throws IOException
     */
    @Test
    public void testConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x31, 0x32 });
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream instance = new BaseNCodecInputStream(inputStream, codec, false);
        assertEquals(inputStream, instance.in);
        assertEquals(codec, instance.baseNCodec);
        assertFalse(instance.doEncode);
    }

    /**
     * Test read method with single byte.
     *
     * @throws IOException
     */
    @Test
    public void testReadSingleByte() throws IOException {
        String testData = "01";
        InputStream inputStream = new ByteArrayInputStream(Hex.decodeHex(testData.getBytes(StandardCharsets.UTF_8)));
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream instance = new BaseNCodecInputStream(inputStream, codec, false);
        int result = instance.read();
        assertEquals(testData.charAt(0), result);
    }

    /**
     * Test read method with multiple bytes.
     *
     * @throws IOException
     */
    @Test
    public void testReadMultipleBytes() throws IOException {
        String testData = "0102";
        InputStream inputStream = new ByteArrayInputStream(Hex.decodeHex(testData.getBytes(StandardCharsets.UTF_8)));
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream instance = new BaseNCodecInputStream(inputStream, codec, false);
        byte[] result = new byte[testData.length()];
        int length = instance.read(result, 0, result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(testData.charAt(i), (byte) result[i]);
        }
    }

    /**
     * Test markSupported method.
     *
     * @throws IOException
     */
    @Test
    public void testMarkSupported() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x31 });
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream instance = new BaseNCodecInputStream(inputStream, codec, false);
        assertFalse(instance.markSupported());
    }

    /**
     * Test skip method with valid length.
     *
     * @throws IOException
     */
    @Test
    public void testSkipValidLength() throws IOException {
        String testData = "010203";
        InputStream inputStream = new ByteArrayInputStream(Hex.decodeHex(testData.getBytes(StandardCharsets.UTF_8)));
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream instance = new BaseNCodecInputStream(inputStream, codec, false);
        long length = 2;
        instance.skip(length);
        byte[] result = new byte[testData.length() - (int) length];
        int readLength = instance.read(result, 0, result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(testData.charAt(i + 2), result[i]);
        }
    }

    /**
     * Test skip method with negative length.
     *
     * @throws IOException
     */
    @Test
    public void testSkipNegativeLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x31 });
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream instance = new BaseNCodecInputStream(inputStream, codec, false);
        long length = -1;
        assertThrows(IllegalArgumentException.class, () -> instance.skip(length));
    }

    /**
     * Test skip method with length larger than remaining data.
     *
     * @throws IOException
     */
    @Test
    public void testSkipLengthLargerThanData() throws IOException {
        String testData = "01";
        InputStream inputStream = new ByteArrayInputStream(Hex.decodeHex(testData.getBytes(StandardCharsets.UTF_8)));
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream instance = new BaseNCodecInputStream(inputStream, codec, false);
        long length = 2;
        instance.skip(length);
        assertEquals(EOF, instance.read());
    }
}