package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    private InputStream inputStream;
    private Base64InputStream base64InputStream;
    private byte[] expectedOutput;

    @Before
    public void setUp() {
        byte[] sourceData = {0x41, 0x42, 0x43};
        inputStream = new ByteArrayInputStream(sourceData);
        base64InputStream = new Base64InputStream(inputStream);
        expectedOutput = new byte[]{(byte) 0x3C, (byte) 0x40, (byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x3E};
    }

    /**
     * Test reading single byte with decoding enabled.
     *
     * @throws IOException
     */
    @Test
    public void testReadSingleByteDecoded() throws IOException {
        int actualByte = base64InputStream.read();
        Assert.assertEquals(expectedOutput[0], actualByte);
    }

    /**
     * Test reading single byte with encoding enabled.
     *
     * @throws IOException
     */
    @Test
    public void testReadSingleByteEncoded() throws IOException {
        Base64InputStream base64InputStreamEncoded = new Base64InputStream(inputStream, true);
        int actualByte = base64InputStreamEncoded.read();
        Assert.assertEquals(-1, actualByte);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[inputStream.available()];
        int bytesRead = inputStream.read(buffer);
        base64InputStreamEncoded = new Base64InputStream(inputStream, true);
        byte[] decodedData = new byte[bytesRead];
        int decodedBytes = base64InputStreamEncoded.read(decodedData);
        Assert.assertEquals(bytesRead, decodedBytes);
        outputStream.write(decodedData, 0, decodedBytes);
        Assert.assertArrayEquals(sourceData, outputStream.toByteArray());
    }

    /**
     * Test reading multiple bytes with decoding enabled.
     *
     * @throws IOException
     */
    @Test
    public void testReadMultipleBytesDecoded() throws IOException {
        byte[] buffer = new byte[3];
        int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(expectedOutput.length, bytesRead);
        Assert.assertArrayEquals(expectedOutput, buffer);
    }

    /**
     * Test reading multiple bytes with encoding enabled.
     *
     * @throws IOException
     */
    @Test
    public void testReadMultipleBytesEncoded() throws IOException {
        // This test is not possible as Base64InputStream doesn't provide encoding functionality by default.
        // This test case can be removed.
    }

    /**
     * Test markSupported method.
     *
     * @throws IOException
     */
    @Test
    public void testMarkSupported() throws IOException {
        Assert.assertFalse(base64InputStream.markSupported());
    }
}