package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

/**
 * Test class for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Test for {@link Base64InputStream#Base64InputStream(InputStream)} constructor.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBase64InputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertFalse(base64InputStream.markSupported());
        assertSame(inputStream, base64InputStream.in);
        assertFalse(base64InputStream.doEncode);
        assertSame(new Base64(false), base64InputStream.base64);
    }

    /**
     * Test for {@link Base64InputStream#Base64InputStream(InputStream, boolean)} constructor.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBase64InputStreamWithEncodeOption() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        assertFalse(base64InputStream.markSupported());
        assertSame(inputStream, base64InputStream.in);
        assertTrue(base64InputStream.doEncode);
        assertSame(new Base64(false), base64InputStream.base64);
    }

    /**
     * Test for {@link Base64InputStream#Base64InputStream(InputStream, boolean, int, byte[])} constructor.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBase64InputStreamWithEncodeOptionLineLengthAndLineSeparator() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 52, new byte[]{'C', 'R', 'L', 'F'});

        assertFalse(base64InputStream.markSupported());
        assertSame(inputStream, base64InputStream.in);
        assertTrue(base64InputStream.doEncode);
        assertSame(new Base64(52, new byte[]{'C', 'R', 'L', 'F'}), base64InputStream.base64);
    }

    /**
     * Test for {@link Base64InputStream#read()} method.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expected = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
        byte[] actual = new byte[expected.length];
        int read = 0;

        while ((read = base64InputStream.read(actual)) > -1) {
            assertArrayEquals(expected, actual, 0, read);
        }

        assertNull(base64InputStream.in);
    }

    /**
     * Test for {@link Base64InputStream#read(byte[], int, int)} method.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testReadWithByteArray() throws IOException {
        String input = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64(input));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expected = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
        byte[] actual = new byte[expected.length];

        int read = base64InputStream.read(actual);
        assertArrayEquals(expected, actual, 0, read);
    }

    /**
     * Test for {@link Base64InputStream#close()} method.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.close();

        assertNull(base64InputStream.in);
    }

    /**
     * Test for {@link Base64InputStream#Base64InputStream(InputStream, boolean, int, byte[])} constructor with empty line separator array.
     *
     * @throws IOException
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBase64InputStreamWithEmptyLineSeparatorArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        new Base64InputStream(inputStream, true, 52, new byte[0]);
    }

    /**
     * Test for {@link Base64InputStream#Base64InputStream(InputStream, boolean, int, byte[])} constructor with null line separator array.
     *
     * @throws IOException
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testBase64InputStreamWithNullLineSeparatorArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        new Base64InputStream(inputStream, true, 52, null);
    }

    /**
     * Test for {@link Base64InputStream#Base64InputStream(InputStream, boolean, int, byte[])} constructor with negative line length.
     *
     * @throws IOException
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBase64InputStreamWithNegativeLineLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        new Base64InputStream(inputStream, true, -1, new byte[0]);
    }

    /**
     * Test for {@link Base64InputStream#Base64InputStream(InputStream, boolean, int, byte[])} constructor with invalid line length.
     *
     * @throws IOException
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBase64InputStreamWithInvalidLineLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        new Base64InputStream(inputStream, true, 80, new byte[0]);
    }

}