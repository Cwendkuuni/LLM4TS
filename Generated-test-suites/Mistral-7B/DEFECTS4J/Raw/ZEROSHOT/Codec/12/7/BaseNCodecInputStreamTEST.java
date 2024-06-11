package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

import org.junit.Test;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        BaseNCodec codec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(new byte[] { 0x41, 0x42 });
        BaseNCodecInputStream stream = new BaseNCodecInputStream(input, codec, false);
    }

    /**
     * Test {@link #read()} method when data is available.
     */
    @Test
    public void testReadDataAvailable() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        codec.encode(new byte[] { 0x61, 0x62, 0x63 }, 0, 3, output.toByteArray(), 0);
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        BaseNCodecInputStream stream = new BaseNCodecInputStream(input, codec, false);

        assertEquals(0x61, stream.read());
        assertEquals(0x62, stream.read());
        assertEquals(0x63, stream.read());
        assertEquals(EOF, stream.read());
    }

    /**
     * Test {@link #read()} method when EOF is reached.
     */
    @Test
    public void testReadEndOfStream() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(new byte[] { 0x61, 0x62 });
        BaseNCodecInputStream stream = new BaseNCodecInputStream(input, codec, false);

        assertEquals(0x61, stream.read());
        assertEquals(0x62, stream.read());
        assertEquals(EOF, stream.read());
    }

    /**
     * Test {@link #read(byte[], int, int)} method when data is available.
     */
    @Test
    public void testReadDataAvailableArray() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BaseNCodec codec = new BaseNCodec();
        codec.encode(new byte[] { 0x61, 0x62, 0x63 }, 0, 3, output.toByteArray(), 0);
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        BaseNCodecInputStream stream = new BaseNCodecInputStream(input, codec, false);

        byte[] buffer = new byte[3];
        int length = stream.read(buffer, 0, buffer.length);

        assertEquals(length, 3);
        assertArrayEquals(new byte[] { 0x61, 0x62, 0x63 }, buffer);
    }

    /**
     * Test {@link #read(byte[], int, int)} method when EOF is reached.
     */
    @Test
    public void testReadEndOfStreamArray() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BaseNCodec codec = new BaseNCodec();
        codec.encode(new byte[] { 0x61, 0x62 }, 0, 2, output.toByteArray(), 0);
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        BaseNCodecInputStream stream = new BaseNCodecInputStream(input, codec, false);

        byte[] buffer = new byte[3];
        int length = stream.read(buffer, 0, buffer.length);

        assertEquals(length, 2);
        assertArrayEquals(new byte[] { 0x61, 0x62 }, buffer);
    }

    /**
     * Test {@link #markSupported()} method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() {
        BaseNCodecInputStream stream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        stream.markSupported();
    }

    /**
     * Test {@link #skip(long)} method.
     */
    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BaseNCodec codec = new BaseNCodec();
        codec.encode(new byte[] { 0x61, 0x62, 0x63, 0x61, 0x62 }, 0, 5, output.toByteArray(), 0);
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        BaseNCodecInputStream stream = new BaseNCodecInputStream(input, codec, false);

        long skipped = stream.skip(3L);
        assertEquals(3, skipped);

        int length = stream.read(new byte[1], 0, 1);
        assertEquals(EOF, length);
    }
}