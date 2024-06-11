package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;

/**
 * Test cases for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test reading bytes from {@link BaseNCodecInputStream} using Base32 encoding.
     *
     * @throws IOException on error
     */
    @Test
    public void testBase32Reading() throws IOException {
        BaseNCodec baseNCodec = new Base32();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(10);
        outputStream.write("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLM".getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        byte[] inputBytes = outputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        BaseNCodecInputStream baseNInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] testBytes = new byte[5];
        int actualBytesRead;

        actualBytesRead = baseNInputStream.read(testBytes, 0, 5);
        assertEquals(5, actualBytesRead);
        assertArrayEquals(new byte[]{"A", "B", "C", "D", "E"}, testBytes);

        actualBytesRead = baseNInputStream.read(testBytes, 0, 1);
        assertEquals(1, actualBytesRead);
        assertArrayEquals(new byte[]{'F'}, testBytes);

        actualBytesRead = baseNInputStream.read(testBytes, 0, 1);
        assertEquals(-1, actualBytesRead);
    }

    /**
     * Test encoding and decoding bytes using {@link BaseNCodecInputStream}.
     *
     * @throws IOException on error
     */
    @Test
    public void testBase32EncodingDecoding() throws IOException {
        BaseNCodec baseNCodec = new Base32();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(10);
        outputStream.write("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLM".getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        byte[] inputBytes = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        BaseNCodecInputStream baseNInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        ByteArrayOutputStream decodedOutput = new ByteArrayOutputStream(10);

        int actualBytesRead;
        byte[] decodedBytes = new byte[5];

        actualBytesRead = baseNInputStream.read(decodedBytes, 0, 5);
        assertEquals(5, actualBytesRead);
        assertArrayEquals(new byte[]{"A", "B", "C", "D", "E"}, decodedBytes);

        actualBytesRead = baseNInputStream.read(decodedBytes, 0, 1);
        assertEquals(-1, actualBytesRead);

        byte[] encodedBytes = new byte[decodedBytes.length * 2 + 1];
        actualBytesRead = baseNInputStream.read(encodedBytes, 0, encodedBytes.length);
        assertEquals(encodedBytes.length, actualBytesRead);

        String encodedBase32 = new String(encodedBytes, StandardCharsets.UTF_8);
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLM=".substring(0, encodedBase32.length()), encodedBase32);

        InputStream encodedInputStream = new ByteArrayInputStream(encodedBytes);
        BaseNCodecInputStream decodedInputStream = new BaseNCodecInputStream(encodedInputStream, baseNCodec, false);

        actualBytesRead = decodedInputStream.read(decodedBytes, 0, decodedBytes.length);
        assertEquals(decodedBytes.length, actualBytesRead);
        assertArrayEquals(new byte[]{"A", "B", "C", "D", "E"}, decodedBytes);

        actualBytesRead = decodedInputStream.read(decodedBytes, 0, 1);
        assertEquals(-1, actualBytesRead);
    }

}