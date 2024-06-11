package org.apache.commons.codec.binary;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        BaseNCodecCodec codec = new BaseNCodecCodec();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, codec, true);

        assertEquals(0, codecInputStream.read());
        assertEquals(1, codecInputStream.read());
        assertEquals(2, codecInputStream.read());
        assertEquals(3, codecInputStream.read());
        assertEquals(4, codecInputStream.read());
        assertEquals(5, codecInputStream.read());
        assertEquals(6, codecInputStream.read());
        assertEquals(7, codecInputStream.read());
        assertEquals(8, codecInputStream.read());
        assertEquals(9, codecInputStream.read());
        assertEquals(-1, codecInputStream.read());
    }

    @Test
    public void testRead_withOffsetAndLength() throws IOException {
        byte[] data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] buffer = new byte[5];
        BaseNCodecCodec codec = new BaseNCodecCodec();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, codec, true);

        int bytesRead = codecInputStream.read(buffer, 1, 3);

        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0, 0, 1, 2, 0}, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testRead_withNullBuffer() throws IOException {
        byte[] data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        BaseNCodecCodec codec = new BaseNCodecCodec();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, codec, true);

        codecInputStream.read(null, 0, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_withNegativeOffset() throws IOException {
        byte[] data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] buffer = new byte[5];
        BaseNCodecCodec codec = new BaseNCodecCodec();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, codec, true);

        codecInputStream.read(buffer, -1, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_withInvalidOffset() throws IOException {
        byte[] data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] buffer = new byte[5];
        BaseNCodecCodec codec = new BaseNCodecCodec();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, codec, true);

        codecInputStream.read(buffer, 6, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_withInvalidLength() throws IOException {
        byte[] data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] buffer = new byte[5];
        BaseNCodecCodec codec = new BaseNCodecCodec();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, codec, true);

        codecInputStream.read(buffer, 1, 6);
    }

    @Test
    public void testRead_withZeroLength() throws IOException {
        byte[] data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] buffer = new byte[5];
        BaseNCodecCodec codec = new BaseNCodecCodec();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, codec, true);

        int bytesRead = codecInputStream.read(buffer, 1, 0);

        assertEquals(0, bytesRead);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0}, buffer);
    }

    @Test
    public void testMarkSupported() {
        byte[] data = {};
        BaseNCodecCodec codec = new BaseNCodecCodec();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, codec, true);

        assertEquals(false, codecInputStream.markSupported());
    }

    @Test
    public void testRead_withEOF() throws IOException {
        byte[] data = {};
        BaseNCodecCodec codec = new BaseNCodecCodec();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, codec, true);

        assertEquals(-1, codecInputStream.read());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        BaseNCodecCodec codec = new BaseNCodecCodec();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, codec, true);

        long skipped = codecInputStream.skip(3);
        assertEquals(3, skipped);
        assertEquals(3, codecInputStream.read());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkip_withNegativeLength() throws IOException {
        byte[] data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        BaseNCodecCodec codec = new BaseNCodecCodec();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, codec, true);

        codecInputStream.skip(-1);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        BaseNCodecCodec codec = new BaseNCodecCodec();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, codec, true);

        assertEquals(10, codecInputStream.available());
    }

}