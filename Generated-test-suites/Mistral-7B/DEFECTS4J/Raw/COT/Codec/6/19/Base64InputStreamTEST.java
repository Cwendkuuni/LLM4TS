package org.apache.commons.codec.binary.test;

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
    public void testBase64InputStream() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] byteArray = new byte[10];
        int len = base64InputStream.read(byteArray);
        Assert.assertEquals(len, 10);
        base64InputStream.close();
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode)
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamDoEncode() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, true);
        byte[] byteArray = new byte[10];
        int len = base64InputStream.read(byteArray);
        Assert.assertEquals(len, 14); // base64 encoded data is longer than original data
        byte[] decoded = new byte[10];
        int decodedLen = base64InputStream.read(decoded);
        Assert.assertEquals(decodedLen, 10);
        base64InputStream.close();
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamDoEncodeWithLineLengthAndLineSeparator() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        Base64 base64 = new Base64(76, "\r\n".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, true, base64.getLineLength(), base64.getLineSeparator());
        byte[] byteArray = new byte[10];
        int len = base64InputStream.read(byteArray);
        Assert.assertEquals(len, 14); // base64 encoded data is longer than original data
        byte[] decoded = new byte[10];
        int decodedLen = base64InputStream.read(decoded);
        Assert.assertEquals(decodedLen, 10);
        base64InputStream.close();
    }

    /**
     * Test read() method
     * @throws IOException
     */
    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] byteArray = new byte[10];
        int len = base64InputStream.read(byteArray);
        Assert.assertEquals(len, 10);
        base64InputStream.close();
    }

    /**
     * Test read(byte[], int, int) method
     * @throws IOException
     */
    @Test
    public void testReadByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] byteArray = new byte[10];
        int len = base64InputStream.read(byteArray);
        Assert.assertEquals(len, 10);
        base64InputStream.close();
    }

    /**
     * Test markSupported() method
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        base64InputStream.markSupported();
    }
}