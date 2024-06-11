package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class BaseNCodecInputStreamTest {

    @Test
    public void testMarkSupported() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        assertFalse(inputStream.markSupported());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeOffset() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        inputStream.read(-1, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeLength() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        inputStream.read(new byte[0], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeOffsetAndLength() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        inputStream.read(new byte[0], -1, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullBuffer() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        inputStream.read(null, 0, 1);
    }

    @Test
    public void testReadZero() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 0x7e }), new BaseNCodec(), false);
        assertEquals(0, inputStream.read());
    }

    @Test
    public void testReadSingleByte() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 0x7e }), new BaseNCodec(), false);
        assertEquals(0x7e, inputStream.read());
        assertEquals(EOF, inputStream.read());
    }

    @Test
    public void testReadMultipleBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("BaseNCodecInputStreamTest".getBytes());
        byte[] data = outputStream.toByteArray();

        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(data), new BaseNCodec(), false);

        byte[] buffer = new byte[7];
        int readLen = inputStream.read(buffer, 0, 7);
        assertArrayEquals(new byte[] { 0x42, 0x61, 0x69, 0x65, 0x63, 0x6F, 0x6D }, buffer);
        assertEquals(EOF, inputStream.read());
    }

    @Test
    public void testReadLongerThanAvailableData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("BaseNCodecInputStreamTest".getBytes());
        byte[] data = outputStream.toByteArray();

        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(data), new BaseNCodec(), false);

        byte[] buffer = new byte[10];
        int readLen = inputStream.read(buffer, 0, 10);
        assertEquals(7, readLen);
    }

    @Test
    public void testReadChunks() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("BaseNCodecInputStreamTestChunks".getBytes());
        byte[] data = outputStream.toByteArray();

        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(data), new BaseNCodec(), false);

        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        int readLen;
        while ((readLen = inputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream2.write(buffer, 0, readLen);
        }

        assertArrayEquals(data, outputStream2.toByteArray());
    }

    @Test
    public void testReadEndOfStream() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        assertEquals(EOF, inputStream.read());
    }

    @Test
    public void testReadEndOfStreamAfterData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("BaseNCodecInputStreamTestEndOfStreamAfterData".getBytes());
        byte[] data = outputStream.toByteArray();

        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(data), new BaseNCodec(), false);
        assertEquals(EOF, inputStream.read());
    }
}