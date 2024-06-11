package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class BaseNCodecInputStreamTest {

    @Test
    public void testMarkSupported() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(BaseNCodec.ALPHABET_LOWER, false), false);
        assertFalse(inputStream.markSupported());
    }

    @Test
    public void testRead() throws IOException {
        byte[] input = {0};
        InputStream is = new ByteArrayInputStream(input);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(is, new BaseNCodec(BaseNCodec.ALPHABET_LOWER, false), false);
        assertEquals(-1, baseNCodecInputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] input = {0x31, 0x32};
        InputStream is = new ByteArrayInputStream(input);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(is, new BaseNCodec(BaseNCodec.ALPHABET_LOWER, false), false);
        byte[] result = new byte[2];
        int bytesRead = baseNCodecInputStream.read(result, 0, 2);
        assertArrayEquals(input, result);
        assertEquals(2, bytesRead);
    }

    @Test
    public void testReadWhenHasData() throws IOException {
        byte[] input = {0x31, 0x32};
        InputStream is = new ByteArrayInputStream(input);
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER, false);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(is, baseNCodec, false);
        byte[] result = new byte[2];
        int bytesRead = baseNCodecInputStream.read(result, 0, 2);
        assertArrayEquals(input, result);
        assertEquals(2, bytesRead);

        byte[] buffer = new byte[8192];
        int bytesToDecode = 512;
        int bytesDecoded = baseNCodec.decode(buffer, 0, bytesToDecode);
        assertTrue(bytesDecoded > 0);
    }

    @Test
    public void testReadWhenNoData() throws IOException {
        byte[] buffer = new byte[8192];
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER, false);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), baseNCodec, false);

        int bytesRead = baseNCodecInputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testReadWhenDecodeError() throws IOException {
        byte[] input = {0x31, 0x33}; // invalid character "3" is present
        InputStream is = new ByteArrayInputStream(input);
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER, false);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(is, baseNCodec, false);

        byte[] result = new byte[2];
        int bytesRead = baseNCodecInputStream.read(result, 0, 2);
        assertArrayEquals(new byte[0], result);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testReadWhenEncodeError() throws IOException {
        byte[] input = {0x61, 0x62}; // input contains characters not allowed for encoding
        InputStream is = new ByteArrayInputStream(input);
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER, true);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(is, baseNCodec, false);

        byte[] result = new byte[32];
        int bytesRead = baseNCodecInputStream.read(result, 0, 32);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testReadWhenDataIsEmpty() throws IOException {
        byte[] buffer = new byte[8192];
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER, false);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), baseNCodec, false);

        int bytesRead = baseNCodecInputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testReadLine() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("FOO\r\nBAR\r\nBAZ".getBytes());
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER, false);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(is, baseNCodec, false);

        String result = baseNCodecInputStream.readLine();
        assertEquals("FOO", result);

        result = baseNCodecInputStream.readLine();
        assertEquals("BAR", result);

        result = baseNCodecInputStream.readLine();
        assertNull(result);
    }

    @Test
    public void testReadLineWithTruncatedInput() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("FOO\r\nBAR".getBytes()); // input is truncated
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER, false);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(is, baseNCodec, false);

        String result = baseNCodecInputStream.readLine();
        assertEquals("FOO", result);

        result = baseNCodecInputStream.readLine();
        assertNull(result);
    }
}