package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test cases for Base64InputStream class
 */
public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamConstructorWithDefaultValues() {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{0x41}));
        Assert.assertFalse(inputStream.doEncode);
        Base64 base64 = new Base64(false);
        Assert.assertSame(base64, inputStream.base64);
    }

    @Test
    public void testBase64InputStreamConstructorWithEncodeSetToTrue() {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{0x41}), true);
        Assert.assertTrue(inputStream.doEncode);
        Base64 base64 = new Base64(true);
        Assert.assertSame(base64, inputStream.base64);
    }

    @Test
    public void testBase64InputStreamConstructorWithLineLengthAndLineSeparator() {
        int lineLength = 5;
        byte[] lineSeparator = new byte[]{0x0D, 0x0A};
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{0x41}), false, lineLength, lineSeparator);
        Assert.assertFalse(inputStream.doEncode);
        Base64 base64 = new Base64(lineLength, lineSeparator);
        Assert.assertSame(base64, inputStream.base64);
        Assert.assertEquals(lineLength, inputStream.base64.getLineLength());
        Assert.assertArrayEquals(lineSeparator, inputStream.base64.getLineSeparator());
    }

    @Test
    public void testReadOneByte() throws IOException {
        byte[] inputData = new byte[]{0x41};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int readByte = base64InputStream.read();
        Assert.assertEquals(inputData[0] & 0xFF, readByte);
    }

    @Test
    public void testReadArrayOfBytes() throws IOException {
        byte[] inputData = new byte[]{0x41, 0x42};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] outputData = new byte[2];
        int readBytes = base64InputStream.read(outputData, 0, outputData.length);
        Assert.assertEquals(inputData.length, readBytes);
        Assert.assertArrayEquals(inputData, outputData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullByteArray() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{0x41}));
        base64InputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeOffset() {
        byte[] inputData = new byte[]{0x41};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] outputData = new byte[1];
        base64InputStream.read(outputData, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadPastEndOfArray() {
        byte[] inputData = new byte[]{0x41};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] outputData = new byte[2];
        int readBytes = base64InputStream.read(outputData, 0, 2);
        Assert.assertEquals(inputData.length, readBytes);
        base64InputStream.read(outputData, 1, 1); // Should throw an IndexOutOfBoundsException
    }

    @Test
    public void testReadPastEndOfStream() throws IOException {
        byte[] inputData = new byte[]{0x41, 0x42};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] outputData = new byte[2];
        int readBytes = base64InputStream.read(outputData, 0, outputData.length);
        Assert.assertEquals(inputData.length, readBytes);
        Assert.assertArrayEquals(inputData, outputData);

        readBytes = base64InputStream.read(outputData, 0, outputData.length);
        Assert.assertEquals(-1, readBytes);
    }

    @Test
    public void testReadPastEndOfStreamWithDecodeSetToFalse() throws IOException {
        byte[] inputData = new byte[]{0x41, 0x42};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] outputData = new byte[2];
        int readBytes = base64InputStream.read(outputData, 0, outputData.length);
        Assert.assertEquals(inputData.length, readBytes);
        Assert.assertArrayEquals(inputData, outputData);

        readBytes = base64InputStream.read(outputData, 0, outputData.length);
        Assert.assertEquals(0, readBytes);
    }

    @Test
    public void testReadWithEmptyInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] outputData = new byte[1];
        int readBytes = base64InputStream.read(outputData, 0, 1);
        Assert.assertEquals(-1, readBytes);
    }

    @Test(expected = IOException.class)
    public void testReadWithIOException() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = new ByteArrayOutputStream(outputStream);
        Base64OutputStream base64OutputStream = new Base64OutputStream(os, false);
        byte[] inputData = new byte[]{0x41};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] outputData = new byte[1];
        base64InputStream.read(outputData, 0, outputData.length);
        base64OutputStream.write(outputData);
        base64OutputStream.flush();
        Assert.assertTrue(outputStream.size() > 0);
        outputStream.close();
        base64OutputStream.close();
        inputStream.close();

        // Simulate IOException
        base64InputStream.close();
        base64InputStream = new Base64InputStream(inputStream);
        Assert.assertArrayEquals(outputData, base64InputStream.read(outputData, 0, outputData.length));
    }

    @Test
    public void testReadWithEncodedData() throws IOException {
        byte[] inputData = new byte[]{0x41, 0x42, 0x43};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        byte[] outputData = new byte[4];
        int readBytes = base64InputStream.read(outputData, 0, outputData.length);
        Assert.assertEquals(inputData.length, readBytes);
        Assert.assertArrayEquals(inputData, outputData, 0, inputData.length);

        outputData = new byte[5];
        readBytes = base64InputStream.read(outputData, 0, outputData.length);
        Assert.assertEquals(-1, readBytes);
    }
}