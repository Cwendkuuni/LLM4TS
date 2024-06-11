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
    public void testRead() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BaseNCodecInputStream testInput = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int read = testInput.read();
        assertEquals(-1, read);

        outputStream.write("test".getBytes());
        outputStream.flush();

        read = testInput.read();
        assertEquals('t', read);

        read = testInput.read();
        assertEquals('e', read);

        read = testInput.read();
        assertEquals('s', read);

        read = testInput.read();
        assertEquals('t', read);

        read = testInput.read();
        assertEquals(-1, read);
    }

    @Test
    public void testReadArray() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BaseNCodecInputStream testInput = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] buffer = new byte[4];
        int len = testInput.read(buffer, 0, buffer.length);
        assertArrayEquals("test".getBytes(), buffer);

        len = testInput.read(buffer, 0, buffer.length);
        assertEquals(-1, len);
    }

    @Test
    public void testMarkSupported() throws IOException {
        BaseNCodecInputStream testInput = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        assertFalse(testInput.markSupported());
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BaseNCodecInputStream testInput = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        testInput.skip(512);
        testInput.skip(512);
        testInput.skip(512);

        byte[] buffer = new byte[4];
        int len = testInput.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[0], buffer);
    }

    @Test
    public void testAvailable() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BaseNCodecInputStream testInput = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] buffer = new byte[4096];
        int len = testInput.read(buffer, 0, buffer.length);

        assertEquals(1, testInput.available());

        testInput.skip(len);

        assertEquals(0, testInput.available());
    }

    @Test
    public void testClosed() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BaseNCodecInputStream testInput = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        testInput.close();

        assertEquals(-1, testInput.read());
    }
}