package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF;
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
    public void testMarkSupported() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        assertFalse(inputStream.markSupported());
    }

    @Test
    public void testReadByte() throws IOException {
        byte[] inputData = new byte[] { 0x1, 0x2, 0x3, EOF };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), false);

        assertEquals(0x1, baseNCodecInputStream.read());
        assertEquals(0x2, baseNCodecInputStream.read());
        assertEquals(0x3, baseNCodecInputStream.read());
        assertEquals(EOF, baseNCodecInputStream.read());
        assertEquals(EOF, baseNCodecInputStream.read());

        assertNull(baseNCodecInputStream.read());
    }

    @Test
    public void testReadByteWithEncode() throws IOException {
        byte[] inputData = new byte[] { 0x1, 0x2, 0x3, EOF };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec codec = new BaseNCodec(5, 6);
        InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(inputData), codec, true);

        int byteRead;
        byte[] encodedData = new byte[10];
        while ((byteRead = inputStream.read(encodedData, 0, 10)) > 0) {
            outputStream.write(encodedData, 0, byteRead);
        }

        byte[] expectedData = codec.decode(outputStream.toByteArray());
        assertArrayEquals(inputData, expectedData);
    }

    @Test
    public void testReadBytes() throws IOException {
        byte[] inputData = new byte[] { 0x1, 0x2, 0x3, 0x4, EOF };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), false);

        byte[] buffer = new byte[4];
        int length = baseNCodecInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[] { 0x1, 0x2, 0x3, 0x4 }, buffer);
        assertEquals(4, length);

        length = baseNCodecInputStream.read(buffer, 0, buffer.length);
        assertEquals(EOF, length);
    }

    @Test
    public void testReadBytesWithEncode() throws IOException {
        byte[] inputData = new byte[] { 0x1, 0x2, 0x3, EOF };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec codec = new BaseNCodec(5, 6);
        InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(inputData), codec, true);

        byte[] buffer = new byte[5];
        int length = inputStream.read(buffer, 0, buffer.length);
        assertEquals(5, length);
        byte[] decodedData = codec.decode(buffer, 0, length);
        assertArrayEquals(inputData, decodedData);
    }

    @Test(expected = NullPointerException.class)
    public void testReadBytesWithNullBuffer() throws IOException {
        byte[] inputData = new byte[] { 0x1, 0x2, 0x3, EOF };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), false);

        baseNCodecInputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesWithNegativeOffset() throws IOException {
        byte[] inputData = new byte[] { 0x1, 0x2, 0x3, EOF };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), false);

        byte[] buffer = new byte[3];
        baseNCodecInputStream.read(buffer, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesWithNegativeLength() throws IOException {
        byte[] inputData = new byte[] { 0x1, 0x2, 0x3, EOF };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), false);

        byte[] buffer = new byte[3];
        baseNCodecInputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesWithBufferSizeTooSmall() throws IOException {
        byte[] inputData = new byte[] { 0x1, 0x2, 0x3, EOF };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), false);

        byte[] buffer = new byte[1];
        baseNCodecInputStream.read(buffer, 0, buffer.length);
        baseNCodecInputStream.read(buffer, 0, 2);
    }
}