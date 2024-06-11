package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class BaseNCodecInputStreamTest {

    @Test
    public void testMarkSupported() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        assertFalse(inputStream.markSupported());
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        int expectedSkipped = 0;
        int skipped = inputStream.skip(expectedSkipped);
        assertEquals(expectedSkipped, skipped);

        expectedSkipped = 512;
        skipped = inputStream.skip(expectedSkipped);
        assertEquals(expectedSkipped, skipped);

        // Test skipping bytes beyond the available data
        expectedSkipped = 1024;
        skipped = inputStream.skip(expectedSkipped);
        assertEquals(expectedSkipped - inputStream.available(), skipped);

        // Test skipping bytes when the input is already at EOF
        byte[] eofBytes = new byte[0];
        InputStream eofInput = new ByteArrayInputStream(eofBytes);
        BaseNCodecInputStream eofInputStream = new BaseNCodecInputStream(eofInput, baseNCodec, false);
        expectedSkipped = 1;
        skipped = eofInputStream.skip(expectedSkipped);
        assertEquals(0, skipped);
    }

    @Test
    public void testAvailable() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        assertEquals(2, inputStream.available());

        byte[] data = new byte[1024];
        int readLen = inputStream.read(data, 0, data.length);
        assertEquals(2, readLen);
        assertEquals(0, inputStream.available());
    }

    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        // Test read with buffer size large enough to hold all data
        byte[] buffer = new byte[input.available()];
        int readLen = inputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(buffer, input.readAllBytes());

        // Test read with buffer size smaller than available data
        buffer = new byte[input.available() / 2];
        readLen = inputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(buffer, new byte[] { 0x01, 0x02, 0x03, 0x04 });

        // Test read at EOF
        byte[] eofBuffer = new byte[1];
        readLen = inputStream.read(eofBuffer, 0, eofBuffer.length);
        assertEquals(EOF, readLen);
    }

    @Test
    public void testReadWithDecoding() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x2A, 0x2B, 0x3D, 0x3E, 0x3F, 0x3F, 0x3F });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);

        assertEquals("abcdef", new String(inputStream.readAllBytes()));
    }

    @Test
    public void testReadWithEncoding() throws IOException {
        String inputString = "abcdef";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(inputString.getBytes()), baseNCodec, false);

        byte[] encodedData = new byte[inputStream.available()];
        int readLen = inputStream.read(encodedData, 0, encodedData.length);
        assertArrayEquals(encodedData, baseNCodec.encode(inputString.getBytes()));
    }

    @Test
    public void testReadResultsWithDecoding() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x2A, 0x2B, 0x3D, 0x3E, 0x3F, 0x3F, 0x3F });
        BaseNCodec baseNCodec = new BaseNCodec();
        byte[] buffer = new byte[1024];

        int readLen = baseNCodec.decode(buffer, 0, 1024);
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/", new String(buffer, 0, readLen));
    }

    @Test
    public void testReadResultsWithEncoding() throws IOException {
        String inputString = "abcdef";
        byte[] buffer = new byte[4096];

        int readLen = baseNCodec.encode(inputString.getBytes(), buffer, buffer.length);
        assertArrayEquals(new byte[] { 0x31, 0x32, 0x33, 0x34, 0x35, 0x36 }, Arrays.copyOfRange(buffer, 0, readLen + 5));
    }
}