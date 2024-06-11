package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamConstructorWithDefaults() {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}));
        Assert.assertFalse(inputStream.doEncode);
        Assert.assertNotNull(inputStream.base64);
    }

    @Test
    public void testBase64InputStreamConstructorWithEncodeMode() {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}), true);
        Assert.assertTrue(inputStream.doEncode);
        Assert.assertNotNull(inputStream.base64);
    }

    @Test
    public void testBase64InputStreamConstructorWithDecodeModeAndDefaults() {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}), false);
        Assert.assertFalse(inputStream.doEncode);
        Assert.assertNotNull(inputStream.base64);
    }

    @Test
    public void testBase64InputStreamConstructorWithEncodeModeLineLengthAndLineSeparator() {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}), true, 5, new byte[]{'\r', '\n'});
        Assert.assertTrue(inputStream.doEncode);
        Assert.assertNotNull(inputStream.base64);
        Assert.assertEquals(5, inputStream.base64.getLineLength());
        Assert.assertArrayEquals(new byte[]{'\r', '\n'}, inputStream.base64.getLineSeparator());
    }

    @Test
    public void testBase64InputStreamReadSingleByte() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("ABCDEFG".getBytes());
        byte[] bytes = byteArrayOutputStream.toByteArray();
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(bytes), false);
        byte testByte = 0;
        int readByte = inputStream.read();
        while (readByte != -1) {
            testByte = (byte) readByte;
            Assert.assertNotEquals(-1, testByte);
            Assert.assertTrue(testByte >= 0 && testByte <= 255);
            readByte = inputStream.read();
        }
    }

    @Test
    public void testBase64InputStreamReadByteArray() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("ABCDEFG".getBytes());
        byte[] bytes = byteArrayOutputStream.toByteArray();
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(bytes), false);
        byte[] expectedResult = new byte[]{'A', 'B', 'C', 'D', 'E', 'F', 'G'};
        byte[] testArray = new byte[7];
        int len = inputStream.read(testArray, 0, 7);
        Assert.assertEquals(len, 7);
        Assert.assertArrayEquals(expectedResult, testArray);
    }

    @Test(expected = NullPointerException.class)
    public void testBase64InputStreamNullByteArray() {
        Base64InputStream inputStream = new Base64InputStream(null, false);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStreamNegativeByteArrayOffset() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("ABCDEFG".getBytes());
        byte[] bytes = byteArrayOutputStream.toByteArray();
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(bytes), false);
        byte[] testArray = new byte[7];
        int offset = -1;
        int len = inputStream.read(testArray, offset, 7);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStreamByteArraySizeTooSmall() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("ABCDEFG".getBytes());
        byte[] bytes = byteArrayOutputStream.toByteArray();
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(bytes), false);
        byte[] testArray = new byte[5];
        int len = inputStream.read(testArray, 0, 5);
    }

    @Test(expected = IOException.class)
    public void testBase64InputStreamNoData() throws IOException {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0][]), false);
        byte[] testArray = new byte[1];
        int len = inputStream.read(testArray, 0, 1);
    }

    @Test(expected = IOException.class)
    public void testBase64InputStreamNoDataOnRead() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), false);
        inputStream.mark(1);
        inputStream.read();
        inputStream.reset();
        byte[] testArray = new byte[1];
        int len = inputStream.read(testArray, 0, 1);
    }
}