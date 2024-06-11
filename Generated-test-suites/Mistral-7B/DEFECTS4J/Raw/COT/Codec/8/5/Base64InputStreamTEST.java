package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    private InputStream inputStream;
    private Base64InputStream base64InputStream;
    private byte[] inputData;

    @Before
    public void setup() {
        inputData = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46};
        inputStream = new ByteArrayInputStream(inputData);
        base64InputStream = new Base64InputStream(inputStream, false); // decoding mode
    }

    /**
     * Test for Base64InputStream constructor with decoding mode
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamDecodingMode() throws IOException {
        byte[] outputData = new byte[inputData.length];
        int len = base64InputStream.read(outputData, 0, outputData.length);
        Assert.assertEquals(inputData.length, len);
        for (int i = 0; i < inputData.length; i++) {
            Assert.assertEquals(inputData[i], outputData[i]);
        }
    }

    /**
     * Test for Base64InputStream constructor with encoding mode
     *
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBase64InputStreamEncodingMode() throws IOException {
        new Base64InputStream(inputStream, true); // encoding mode
    }

    /**
     * Test for read method with decoding mode and different buffer sizes
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamReadDecodingModeDifferentBufferSizes() throws IOException {
        byte[] outputData = new byte[inputData.length];
        int len = base64InputStream.read(outputData, 0, outputData.length);
        Assert.assertEquals(inputData.length, len);

        byte[] outputDataSmall = new byte[inputData.length / 2];
        len = base64InputStream.read(outputDataSmall, 0, outputDataSmall.length);
        Assert.assertNotSame(inputData.length, len);
        for (int i = 0; i < outputDataSmall.length; i++) {
            Assert.assertEquals(inputData[i], outputDataSmall[i]);
        }
    }

    /**
     * Test for read method with decoding mode and empty buffer
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamReadDecodingModeEmptyBuffer() throws IOException {
        byte[] outputData = new byte[0];
        int len = base64InputStream.read(outputData, 0, outputData.length);
        Assert.assertEquals(-1, len);
    }

    /**
     * Test for read method with decoding mode and negative offset
     *
     * @throws IOException
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStreamReadDecodingModeNegativeOffset() throws IOException {
        byte[] outputData = new byte[inputData.length];
        base64InputStream.read(outputData, -1, outputData.length);
    }

    /**
     * Test for read method with decoding mode and negative length
     *
     * @throws IOException
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStreamReadDecodingModeNegativeLength() throws IOException {
        byte[] outputData = new byte[inputData.length];
        base64InputStream.read(outputData, 0, -1);
    }

    /**
     * Test for read method with decoding mode and buffer larger than input data
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamReadDecodingModeBufferLargerThanInputData() throws IOException {
        byte[] outputData = new byte[inputData.length + 1];
        int len = base64InputStream.read(outputData, 0, outputData.length);
        Assert.assertEquals(inputData.length, len);
        for (int i = 0; i < inputData.length; i++) {
            Assert.assertEquals(inputData[i], outputData[i]);
        }
    }

    /**
     * Test for read method with decoding mode and null buffer
     *
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testBase64InputStreamReadDecodingModeNullBuffer() throws IOException {
        base64InputStream.read(null, 0, 0);
    }

    /**
     * Test for Base64InputStream with different line lengths and line separators
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamLineLengthAndLineSeparator() throws IOException {
        byte[] inputDataLineBreaks = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46, (byte) 0x0A, (byte) 0x0D};
        InputStream inputStreamLineBreaks = new ByteArrayInputStream(inputDataLineBreaks);
        Base64InputStream base64InputStreamLineBreaks = new Base64InputStream(inputStreamLineBreaks, false, 5, new byte[]{(byte) 0x0A, (byte) 0x0D});
        byte[] outputDataLineBreaks = new byte[inputDataLineBreaks.length + 1];
        int lenLineBreaks = base64InputStreamLineBreaks.read(outputDataLineBreaks, 0, outputDataLineBreaks.length);
        Assert.assertEquals(inputDataLineBreaks.length + 1, lenLineBreaks);
        for (int i = 0; i < inputDataLineBreaks.length; i++) {
            Assert.assertEquals(inputDataLineBreaks[i], outputDataLineBreaks[i]);
        }
        Assert.assertEquals(inputDataLineBreaks[inputDataLineBreaks.length - 1], outputDataLineBreaks[outputDataLineBreaks.length - 2]);
        Assert.assertEquals(inputDataLineBreaks[inputDataLineBreaks.length - 2], outputDataLineBreaks[outputDataLineBreaks.length - 1]);
    }

    /**
     * Test for Base64InputStream with null InputStream
     *
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testBase64InputStreamNullInputStream() throws IOException {
        new Base64InputStream(null, false);
    }
}