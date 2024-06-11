package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;

/**
 * Tests for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);
        assertEquals(inputStream.getClass(), BaseNCodecInputStream.class);
        assertEquals(inputStream.doEncode, false);
        assertEquals(inputStream.baseNCodec, codec);
    }

    /**
     * Test {@link BaseNCodecInputStream#read()}.
     */
    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x30 });
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);

        assertEquals(inputStream.read(), 0x31); // decode 0x30 to 0x31

        input = new ByteArrayInputStream(new byte[] { 0x2A });
        inputStream = new BaseNCodecInputStream(input, codec, false);
        assertEquals(inputStream.read(), EOF);

        input = new ByteArrayInputStream(new byte[] { 0x00 });
        inputStream = new BaseNCodecInputStream(input, codec, false);
        assertEquals(inputStream.read(), EOF);
    }

    /**
     * Test {@link BaseNCodecInputStream#read(byte[], int, int)}.
     */
    @Test
    public void testReadByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33, 0x34 });
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);

        byte[] buffer = new byte[4];

        int read = inputStream.read(buffer, 0, buffer.length);
        assertEquals(read, 4);
        for (int i = 0; i < read; i++) {
            assertEquals(buffer[i], (byte) (i + 0x31));
        }

        input = new ByteArrayInputStream(new byte[] { 0x2A, 0x2B, 0x2C, 0x2D });
        inputStream = new BaseNCodecInputStream(input, codec, false);
        read = inputStream.read(buffer, 0, buffer.length);
        assertEquals(read, 0);
        assertNull(buffer);
    }

    /**
     * Test {@link BaseNCodecInputStream#markSupported()}.
     */
    @Test
    public void testMarkSupported() {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);
        assertFalse(inputStream.markSupported());
    }

    /**
     * Test {@link BaseNCodecInputStream#skip(long)}.
     */
    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33, 0x34 });
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);

        // skip 1 byte
        long skipped = inputStream.skip(1);
        assertEquals(skipped, 1);

        // decode next byte
        byte b = inputStream.read();
        assertEquals(b, (byte) 0x32);

        // skip next 3 bytes
        skipped = inputStream.skip(3);
        assertEquals(skipped, 3);

        // check EOF
        b = inputStream.read();
        assertEquals(b, EOF);
    }

    /**
     * Test {@link BaseNCodecInputStream#close()}.
     */
    @Test
    public void testClose() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33, 0x34 });
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);

        byte[] buffer = new byte[4];
        int bytesRead = 0;
        while (inputStream.read(buffer) > 0) {
            output.write(buffer);
            bytesRead += buffer.length;
        }

        // check output
        byte[] outputBytes = output.toByteArray();
        assertEquals(outputBytes.length, bytesRead);
        for (int i = 0; i < bytesRead; i++) {
            assertEquals(outputBytes[i], (byte) (i + 0x31));
        }

        inputStream.close();
        input.close();
        output.close();
    }

    /**
     * Test {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)} with encoding.
     */
    @Test
    public void testEncode() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x61, 0x62, 0x63, 0x64 });
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BaseNCodec codec = new BaseNCodec(BaseNCodec.ALPHABET_LOWER_CASE, BaseNCodec.ALPHABET_LOWER_CASE, 6);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, true);

        byte[] buffer = new byte[4];
        int bytesRead = 0;
        while (inputStream.read(buffer) > 0) {
            output.write(buffer);
            bytesRead += buffer.length;
        }

        // check output
        byte[] outputBytes = output.toByteArray();
        assertEquals(outputBytes.length, bytesRead);
        String expected = "jklm";
        assertEquals(new String(outputBytes), expected);

        inputStream.close();
        input.close();
        output.close();
    }

}