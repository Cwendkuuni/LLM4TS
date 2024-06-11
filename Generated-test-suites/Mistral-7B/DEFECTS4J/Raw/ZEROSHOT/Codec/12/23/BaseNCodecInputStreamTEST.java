package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

/**
 * Test file for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor when doEncode is true.
     *
     * @throws IOException
     */
    @Test
    public void testBaseNCodecInputStreamConstructEncode() throws IOException {
        BaseNCodec baseNCodec = new Base32();
        InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZW5jb2RlIGZvdWw="));
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        assertNotNull(baseNCodecInputStream);
        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Test constructor when doEncode is false.
     *
     * @throws IOException
     */
    @Test
    public void testBaseNCodecInputStreamConstructDecode() throws IOException {
        BaseNCodec baseNCodec = new Base32();
        InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64("SSBvbGx5IGZvdWwgdGhpcyBmaWxlIGZvdWw="));
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertNotNull(baseNCodecInputStream);
        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Test read method when doEncode is true.
     *
     * @throws IOException
     */
    @Test
    public void testBaseNCodecInputStreamReadEncode() throws IOException {
        BaseNCodec baseNCodec = new Base32();
        InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZW5jb2RlIGZvdWw="));
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        byte[] result = new byte[2];
        assertEquals(1, baseNCodecInputStream.read(result, 0, result.length));
        assertEquals(0x23, result[0]);
        assertEquals(0x2a, result[1]);
    }

    /**
     * Test read method when doEncode is false.
     *
     * @throws IOException
     */
    @Test
    public void testBaseNCodecInputStreamReadDecode() throws IOException {
        BaseNCodec baseNCodec = new Base32();
        InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64("SSBvbGx5IGZvdWwgdGhpcyBmaWxlIGZvdWw="));
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[3];
        assertEquals(3, baseNCodecInputStream.read(result, 0, result.length));
        assertEquals(0x30, result[0]);
        assertEquals(0x31, result[1]);
        assertEquals(0x32, result[2]);
    }

    /**
     * Test read method when EOF is reached.
     *
     * @throws IOException
     */
    @Test
    public void testBaseNCodecInputStreamReadEOF() throws IOException {
        BaseNCodec baseNCodec = new Base32();
        InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64("SSBvbGx5IGZvdWwgdGhpcyBmaWxlIGZvdWw="));
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[1];
        assertEquals(-1, baseNCodecInputStream.read(result, 0, result.length));
    }

    /**
     * Test markSupported method.
     */
    @Test
    public void testBaseNCodecInputStreamMarkSupported() {
        BaseNCodec baseNCodec = new Base32();
        InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64("SSBvbGx5IGZvdWwgdGhpcyBmaWxlIGZvdWw="));
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Test skip method.
     *
     * @throws IOException
     */
    @Test
    public void testBaseNCodecInputStreamSkip() throws IOException {
        BaseNCodec baseNCodec = new Base32();
        InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64("SSBvbGx5IGZvdWwgdGhpcyBmaWxlIGZvdWw="));
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertEquals(0, baseNCodecInputStream.skip(0));
        assertEquals(1, baseNCodecInputStream.skip(1));
        assertEquals(2, baseNCodecInputStream.skip(2));
        assertEquals(-1, baseNCodecInputStream.skip(3));
    }

    /**
     * Test available method.
     *
     * @throws IOException
     */
    @Test
    public void testBaseNCodecInputStreamAvailable() throws IOException {
        BaseNCodec baseNCodec = new Base32();
        InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64("SSBvbGx5IGZvdWwgdGhpcyBmaWxlIGZvdWw="));
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] buffer = new byte[512];

        assertEquals(1, baseNCodecInputStream.read(buffer, 0, buffer.length));
        assertEquals(3, baseNCodecInputStream.available());

        assertEquals(0, baseNCodecInputStream.skip(3));
        assertEquals(0, baseNCodecInputStream.available());
    }

    /**
     * Test close method.
     *
     * @throws IOException
     */
    @Test
    public void testBaseNCodecInputStreamClose() throws IOException {
        BaseNCodec baseNCodec = new Base32();
        InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64("SSBvbGx5IGZvdWwgdGhpcyBmaWxlIGZvdWw="));
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] buffer = new byte[512];

        assertEquals(1, baseNCodecInputStream.read(buffer, 0, buffer.length));
        baseNCodecInputStream.close();
        assertNull(baseNCodecInputStream.in);
    }

    /**
     * Test toString method.
     */
    @Test
    public void testBaseNCodecInputStreamToString() {
        BaseNCodec baseNCodec = new Base32();
        InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64("SSBvbGx5IGZvdWwgdGhpcyBmaWxlIGZvdWw="));
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        String toString = baseNCodecInputStream.toString();

        assertTrue(toString.contains("BaseNCodecInputStream"));
        assertTrue(toString.contains("BaseNCodec"));
        assertTrue(toString.contains("InputStream"));
        assertTrue(toString.contains("doEncode=" + Boolean.toString(false)));
    }
}