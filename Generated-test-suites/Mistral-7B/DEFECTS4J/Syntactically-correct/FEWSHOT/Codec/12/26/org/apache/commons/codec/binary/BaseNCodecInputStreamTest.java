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

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;

public class BaseNCodecInputStreamTest {

    private static final BaseNCodec BASE_N_CODEC = new BaseNCodec();

    @Test
    public void testBaseNCodecInputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x41, 0x41, 0x42, 0x42, 0x42 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, false);

        assertEquals(inputStream, baseNCodecInputStream.getInputStream());
        assertEquals(false, baseNCodecInputStream.markSupported());

        byte[] buffer = new byte[5];
        int readLength;

        readLength = baseNCodecInputStream.read(buffer, 0, buffer.length);
        assertEquals(5, readLength);
        assertArrayEquals(new byte[] { 0x41, 0x41, 0x41, 0x42, 0x42 }, buffer);

        readLength = baseNCodecInputStream.read(buffer, 0, buffer.length);
        assertEquals(0, readLength);

        baseNCodecInputStream.close();
    }

    @Test
    public void testBaseNCodecInputStreamEncode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x41, 0x41, 0x42, 0x42, 0x42 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[5];

        while (baseNCodecInputStream.read(buffer, 0, buffer.length) > 0) {
            outputStream.write(buffer);
        }
        outputStream.close();

        byte[] output = outputStream.toByteArray();
        assertEquals("YW55IGlzIGlzIGlzIHBsYW5ldC4=", new String(output));
    }

    @Test
    public void testBaseNCodecInputStreamDecode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("YW55IGlzIGlzIGlzIHBsYW5ldC4=".getBytes());
        outputStream.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, false);

        byte[] buffer = new byte[5];
        int readLength;

        readLength = baseNCodecInputStream.read(buffer, 0, buffer.length);
        assertEquals(5, readLength);
        assertArrayEquals(new byte[] { 0x41, 0x41, 0x41, 0x42, 0x42 }, buffer);

        readLength = baseNCodecInputStream.read(buffer, 0, buffer.length);
        assertEquals(0, readLength);

        baseNCodecInputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("YW55IGlzIGlzIGlzIHBsYW5ldC4=".getBytes());
        outputStream.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, false);

        long skippedBytes = 5;
        baseNCodecInputStream.skip(skippedBytes);

        byte[] buffer = new byte[5];
        int readLength;

        readLength = baseNCodecInputStream.read(buffer, 0, buffer.length);
        assertEquals(5, readLength);
        assertArrayEquals(new byte[] { 0x42, 0x42, 0x42, 0x42, 0x42 }, buffer);

        baseNCodecInputStream.close();
    }

    @Test
    public void testMarkNotSupported() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("YW55IGlzIGlzIGlzIHBsYW5ldC4=".getBytes());
        outputStream.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, false);

        baseNCodecInputStream.mark(10);
        assertFalse(baseNCodecInputStream.markSupported());

        byte[] buffer = new byte[5];
        int readLength;

        readLength = baseNCodecInputStream.read(buffer, 0, buffer.length);
        assertEquals(5, readLength);
        assertArrayEquals(new byte[] { 0x42, 0x42, 0x42, 0x42, 0x42 }, buffer);

        baseNCodecInputStream.reset();

        readLength = baseNCodecInputStream.read(buffer, 0, buffer.length);
        assertEquals(5, readLength);
        assertArrayEquals(new byte[] { 0x41, 0x41, 0x41, 0x42, 0x42 }, buffer);

        baseNCodecInputStream.close();
    }
}