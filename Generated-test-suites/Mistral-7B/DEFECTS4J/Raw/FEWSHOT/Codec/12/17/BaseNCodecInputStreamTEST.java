package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {

    private static final BaseNCodec BASE_N_CODEC = new BaseNCodec();

    @Test
    public void testMarkSupported() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{}), BASE_N_CODEC, false);
        assertFalse(inputStream.markSupported());
    }

    @Test
    public void testReadBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BASE_N_CODEC.encode(new byte[]{'A'}, 0, 1, outputStream, false);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), BASE_N_CODEC, false);
        byte[] buffer = new byte[2];

        int read = inputStream.read(buffer, 0, 2);
        assertEquals(1, read);
        assertSame(new byte[]{'A'}, buffer);

        read = inputStream.read(buffer, 0, 2);
        assertEquals(-1, read);
    }

    @Test
    public void testReadBytesEncoded() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BASE_N_CODEC.encode(new byte[]{'A', 'B'}, 0, 2, outputStream, false);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), BASE_N_CODEC, true);
        byte[] buffer = new byte[4];

        int read = inputStream.read(buffer, 0, 4);
        assertEquals(2, read);
        assertSame(new byte[]{'A', 'B'}, new byte[] { buffer[0], buffer[1] });

        read = inputStream.read(buffer, 0, 4);
        assertEquals(-1, read);
    }

    @Test
    public void testReadBytesDecoded() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BASE_N_CODEC.encode(new byte[]{'T', 'S'}, 0, 2, outputStream, false);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BaseNCodecInputStream decodedInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, false);
        byte[] buffer = new byte[3];

        int read = decodedInputStream.read(buffer, 0, 3);
        assertEquals(2, read);
        assertSame(new byte[]{'T', 'S'}, new byte[] { buffer[0], buffer[1] });

        read = decodedInputStream.read(buffer, 0, 3);
        assertEquals(-1, read);
    }

    @Test
    public void testReadBytesDecodedLargeChunk() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BASE_N_CODEC.encode(new byte[]{'T', 'S', 'R', 'R', 'O', 'O', 'T'}, 0, 7, outputStream, false);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BaseNCodecInputStream decodedInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, false);
        byte[] buffer = new byte[8];

        int read = decodedInputStream.read(buffer, 0, 8);
        assertEquals(7, read);
        assertSame(new byte[]{'T', 'S', 'R', 'R', 'O', 'O', 'T'}, new byte[] { buffer[0], buffer[1], buffer[2], buffer[3], buffer[4], buffer[5], buffer[6] });

        read = decodedInputStream.read(buffer, 0, 8);
        assertEquals(-1, read);
    }
}