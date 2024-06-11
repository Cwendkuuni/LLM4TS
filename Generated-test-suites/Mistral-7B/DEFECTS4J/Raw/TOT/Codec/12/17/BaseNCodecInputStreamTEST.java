package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for BaseNCodecInputStream.
 */
public class BaseNCodecInputStreamTest {

    private BaseNCodec baseNCodec;
    private BaseNCodecInputStream inputStream;
    private ByteArrayInputStream input;
    private ByteArrayOutputStream output;

    @Before
    public void setUp() {
        baseNCodec = new BaseNCodec();
        input = new ByteArrayInputStream(new byte[]{"IQ==".getBytes()});
        output = new ByteArrayOutputStream();
        inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
    }

    /**
     * Test reading a single byte.
     *
     * @throws IOException on error
     */
    @Test
    public void testReadByte() throws IOException {
        final int b = inputStream.read();
        Assert.assertEquals(49, b); // 'I' ASCII code
    }

    /**
     * Test reading multiple bytes.
     *
     * @throws IOException on error
     */
    @Test
    public void testReadBytes() throws IOException {
        final byte[] b = new byte[3];
        final int len = inputStream.read(b, 0, 3);
        Assert.assertArrayEquals(new byte[]{'I', 'Q', '='}, b);
    }

    /**
     * Test reading past the end of the input.
     *
     * @throws IOException on error
     */
    @Test(expected = IOException.class)
    public void testReadPastEndOfInput() throws IOException {
        inputStream.read();
        inputStream.read();
        inputStream.read(); // should throw an exception
    }

    /**
     * Test markSupported method.
     */
    @Test
    public void testMarkSupported() {
        Assert.assertFalse(inputStream.markSupported());
    }

    /**
     * Test skip method.
     *
     * @throws IOException on error
     */
    @Test
    public void testSkip() throws IOException {
        final long skip = 3;
        inputStream.skip(skip);

        final int b;
        final byte[] bytes = new byte[4];

        // read the first byte which was already read before skipping
        b = inputStream.read();
        Assert.assertEquals(49, b); // 'I' ASCII code

        // read the next byte
        b = inputStream.read();
        Assert.assertEquals(83, b); // 'Q' ASCII code

        // read the remaining bytes
        inputStream.read(bytes, 0, 2);
        Assert.assertArrayEquals(new byte[]{'=', '='}, bytes);

        // test if the next read will return EOF
        b = inputStream.read();
        Assert.assertEquals(-1, b);
    }

    /**
     * Test reading encoded data.
     *
     * @throws IOException on error
     */
    @Test
    public void testReadEncodedData() throws IOException {
        output.write("IQ==".getBytes());
        output.flush();

        input = new ByteArrayInputStream(output.toByteArray());
        inputStream = new BaseNCodecInputStream(input, baseNCodec, true);

        final byte[] b = new byte[3];
        final int len = inputStream.read(b, 0, 3);
        Assert.assertArrayEquals(new byte[]{'I', 'Q', '='}, b);
    }
}