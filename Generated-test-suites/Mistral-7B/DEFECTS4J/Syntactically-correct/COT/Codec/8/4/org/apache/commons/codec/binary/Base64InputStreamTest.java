package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    @Test
    public void testConstructorWithDefaults() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{0x01}));
        Assert.assertFalse(base64InputStream.doEncode);
        Base64 base64 = base64InputStream.base64;
        Assert.assertEquals(76, base64.lineLength);
        Assert.assertArrayEquals(new byte[]{0x0d, 0x0a}, base64.lineSeparator);
    }

    @Test
    public void testConstructorWithoutDecoding() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testData = "TestData".getBytes();
        Base64 base64 = new Base64();
        byte[] encodedData = base64.encode(testData);
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(encodedData), true);
        byte[] resultData = new byte[testData.length];
        int bytesRead;
        while ((bytesRead = base64InputStream.read(resultData, 0, testData.length)) != -1) {
            Assert.assertEquals(testData.length, bytesRead);
            Assert.assertArrayEquals(testData, resultData);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputStream() {
        new Base64InputStream(null, false);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeOffset() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        new Base64InputStream(inputStream, false).read(new byte[1], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeLength() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        new Base64InputStream(inputStream, false).read(new byte[1], 0, -1);
    }

    @Test(expected = IOException.class)
    public void testNegativeOffsetAndLength() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        new Base64InputStream(inputStream, false).read(new byte[1], -1, -1);
    }

    @Test
    public void testReadByte() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int b = base64InputStream.read();
        Assert.assertEquals(0x01, b);
        int b2 = base64InputStream.read();
        Assert.assertEquals(-1, b2);
    }

    @Test
    public void testReadByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] result = new byte[3];
        int bytesRead = base64InputStream.read(result);
        Assert.assertEquals(3, bytesRead);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, result);
        int bytesRead2 = base64InputStream.read(result, 1, 1);
        Assert.assertEquals(1, bytesRead2);
        Assert.assertArrayEquals(new byte[]{0x02}, result);
        int bytesRead3 = base64InputStream.read(result, 3, 1);
        Assert.assertEquals(-1, bytesRead3);
    }

    @Test
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{0x01}), false);
        Assert.assertFalse(base64InputStream.markSupported());
    }
}