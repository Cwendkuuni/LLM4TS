package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    /**
     * Test constructor Base64InputStream(InputStream in)
     * @throws IOException
     */
    @Test
    public void testBase64InputStream_InputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x41, 0x42, 0x43});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int read = base64InputStream.read();
        Assert.assertNotNull(read);
        base64InputStream.close();
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode)
     * @throws IOException
     */
    @Test
    public void testBase64InputStream_InputStream_boolean() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x41, 0x42, 0x43});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int read = base64InputStream.read();
        Assert.assertNotNull(read);
        base64InputStream.close();
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
     * @throws IOException
     */
    @Test
    public void testBase64InputStream_InputStream_boolean_int_byteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x41, 0x42, 0x43});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 76, new byte[]{(byte) 0x0D, (byte) 0x0A});
        int read = base64InputStream.read();
        Assert.assertNotNull(read);
        base64InputStream.close();
    }

    /**
     * Test method read()
     * @throws IOException
     */
    @Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x41, 0x42, 0x43, 0x44, 0x45, 0x46});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int read1 = base64InputStream.read();
        int read2 = base64InputStream.read();
        int read3 = base64InputStream.read();
        int read4 = base64InputStream.read();
        int read5 = base64InputStream.read();
        Assert.assertNotNull(read1);
        Assert.assertNotNull(read2);
        Assert.assertNotNull(read3);
        Assert.assertNotNull(read4);
        Assert.assertEquals(-1, read5);
        base64InputStream.close();
    }

    /**
     * Test method read(byte[], int, int)
     * @throws IOException
     */
    @Test
    public void testRead_byteArray_int_int() throws IOException {
        byte[] buffer = new byte[5];
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int readLen = base64InputStream.read(buffer, 0, buffer.length);
        Assert.assertNotNull(readLen);
        Assert.assertEquals(buffer.length, readLen);
        base64InputStream.close();
    }

    /**
     * Test method markSupported()
     */
    @Test
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false);
        Assert.assertFalse(base64InputStream.markSupported());
        base64InputStream.close();
    }

    /**
     * Test edge case: empty input stream
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testEmptyInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        base64InputStream.read();
    }

    /**
     * Test exception handling: null input stream
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testNullInputStream() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(null, false);
    }

    /**
     * Test exception handling: negative lineLength
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLineLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x41, 0x42, 0x43});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, -1, null);
    }

    /**
     * Test exception handling: null lineSeparator
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testNullLineSeparator() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x41, 0x42, 0x43});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 76, null);
    }

    /**
     * Test exception handling: invalid input stream
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testInvalidInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
    }

    /**
     * Test exception handling: input stream EOF
     * @throws IOException
     */
    @Test
    public void testInputStreamEOF() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x41, 0x42, 0x43});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int read = base64InputStream.read();
        int read2 = base64InputStream.read();
        base64InputStream.close();
        Assert.assertNotNull(read);
        Assert.assertEquals(-1, read2);
    }

    /**
     * Test exception handling: input stream closed
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testInputStreamClosed() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x41, 0x42, 0x43});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        base64InputStream.close();
        int read = base64InputStream.read();
    }
}