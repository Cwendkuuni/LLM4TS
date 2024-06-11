package org.apache.commons.codec.binary;

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

public class BaseNCodecInputStreamTest {

    private final static int BUFFER_SIZE = 1024;

    /**
     * Test method for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)}.
     */
    @Test
    public void testConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x14, 0x15, 0x16, 0x17, 0x18 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        assertEquals(input, inputStream.in);
        assertEquals(baseNCodec, inputStream.baseNCodec);
        assertFalse(inputStream.doEncode);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read()}.
     */
    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x14, 0x15, 0x16, 0x17, 0x18 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] bytes = new byte[BUFFER_SIZE];
        int len;

        len = inputStream.read(bytes, 0, BUFFER_SIZE);
        assertEquals(5, len);
        assertArrayEquals(new byte[] { 0x14, 0x15, 0x16, 0x17, 0x18 }, bytes);

        len = inputStream.read();
        assertEquals(-1, len);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read(byte[], int, int)}.
     */
    @Test
    public void testRead_byteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x14, 0x15, 0x16, 0x17, 0x18 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] bytes = new byte[BUFFER_SIZE];
        int len;

        len = inputStream.read(bytes, 0, BUFFER_SIZE / 2);
        assertEquals(3, len);
        assertArrayEquals(new byte[] { 0x14, 0x15, 0x16 }, bytes, 0, 3);

        len = inputStream.read(bytes, BUFFER_SIZE / 2, BUFFER_SIZE / 2);
        assertEquals(2, len);
        assertArrayEquals(new byte[] { 0x17, 0x18 }, bytes, BUFFER_SIZE / 2, 2);

        len = inputStream.read(bytes, 0, BUFFER_SIZE / 2);
        assertEquals(0, len);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#markSupported()}.
     */
    @Test
    public void testMarkSupported() {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x14, 0x15, 0x16, 0x17, 0x18 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        assertFalse(inputStream.markSupported());
    }

    /**
     * Test method for {@link BaseNCodecInputStream#skip(long)}.
     */
    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x14, 0x15, 0x16, 0x17, 0x18 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        long len = 5;
        long skipped = inputStream.skip(len);
        assertEquals(5, skipped);
        assertEquals(0, inputStream.read());
        assertEquals(0, inputStream.read());
        assertEquals(0, inputStream.read());
        assertEquals(0, inputStream.read());
        assertEquals(0, inputStream.read());
    }

    /**
     * Test method for {@link BaseNCodecInputStream#available()}.
     */
    @Test
    public void testAvailable() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x14, 0x15, 0x16, 0x17, 0x18 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        int len = inputStream.available();
        assertEquals(5, len);
        len = inputStream.read();
        assertEquals(0x14, len);
        len = inputStream.available();
        assertEquals(4, len);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#close()}.
     */
    @Test
    public void testClose() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x14, 0x15, 0x16, 0x17, 0x18 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        inputStream.close();
        assertNull(inputStream.in);
        assertNull(inputStream.baseNCodec);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)}.
     */
    @Test
    public void testConstructor_encode() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x14, 0x15, 0x16, 0x17, 0x18 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];

        int len;

        while ((len = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
            output.write(buffer, 0, len);
        }

        byte[] encoded = output.toByteArray();
        byte[] decoded = baseNCodec.decode(encoded);
        assertArrayEquals(new byte[] { 0x14, 0x15, 0x16, 0x17, 0x18 }, decoded);
    }

}