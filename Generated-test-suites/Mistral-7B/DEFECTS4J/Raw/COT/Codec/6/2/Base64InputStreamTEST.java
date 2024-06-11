package org.apache.commons.codec.binary;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    private Base64InputStream inputStream;
    private InputStream originalInputStream;
    private byte[] testData;

    @Before
    public void setUp() throws IOException {
        testData = "HelloWorld!".getBytes();
        originalInputStream = new ByteArrayInputStream(testData);
        inputStream = new Base64InputStream(originalInputStream);
    }

    @Test
    public void testBase64InputStream_defaultConstructor() {
        // Test default constructor: Base64InputStream(InputStream in)
        Assert.assertNotNull(inputStream);
        Assert.assertFalse(inputStream.doEncode);
        Assert.assertNotNull(inputStream.base64);
    }

    @Test
    public void testBase64InputStream_constructorWithEncodeOption() throws IOException {
        // Test constructor: Base64InputStream(InputStream in, boolean doEncode)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream encodedInputStream = new Base64InputStream(originalInputStream, true);
        int data;

        while ((data = encodedInputStream.read()) != -1) {
            outputStream.write(data);
        }

        byte[] expectedEncodedData = Base64.encodeBase64(testData);
        byte[] actualEncodedData = outputStream.toByteArray();

        Assert.assertArrayEquals(expectedEncodedData, actualEncodedData);
    }

    @Test
    public void testBase64InputStream_constructorWithEncodeOptionAndLineLength() throws IOException {
        // Test constructor: Base64InputStream(InputStream in, boolean doEncode, int lineLength)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream encodedInputStream = new Base64InputStream(originalInputStream, true, 76);
        int data;

        while ((data = encodedInputStream.read()) != -1) {
            outputStream.write(data);
        }

        byte[] expectedEncodedData = Base64.encodeBase64LineLength(testData, 76);
        byte[] actualEncodedData = outputStream.toByteArray();

        Assert.assertArrayEquals(expectedEncodedData, actualEncodedData);
    }

    @Test
    public void testBase64InputStream_constructorWithEncodeOptionAndLineSeparator() throws IOException {
        // Test constructor: Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream encodedInputStream = new Base64InputStream(originalInputStream, true, 76, new byte[]{'\r', '\n'});
        int data;

        while ((data = encodedInputStream.read()) != -1) {
            outputStream.write(data);
        }

        byte[] expectedEncodedData = Base64.encodeBase64LineLengthWithSeparator(testData, 76, '\r', '\n');
        byte[] actualEncodedData = outputStream.toByteArray();

        Assert.assertArrayEquals(expectedEncodedData, actualEncodedData);
    }

    @Test
    public void testBase64InputStream_constructorWithDecodeOption() throws IOException {
        // Test constructor: Base64InputStream(InputStream in, boolean doEncode)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream decodedInputStream = new Base64InputStream(new ByteArrayInputStream(Base64.encodeBase64(testData)), false);
        int data;

        while ((data = decodedInputStream.read()) != -1) {
            outputStream.write(data);
        }

        Assert.assertEquals(new String(testData), new String(outputStream.toByteArray()));
    }

    @Test(expected = NullPointerException.class)
    public void testBase64InputStream_nullInputStream() throws IOException {
        // Test constructor: Base64InputStream(InputStream in) and Base64InputStream(InputStream in, boolean doEncode)
        new Base64InputStream(null, false);
        new Base64InputStream(null, true);
    }

    @Test(expected = IOException.class)
    public void testBase64InputStream_inputStreamThrowsIOException() throws IOException {
        // Test input stream throws an IOException, and Base64InputStream should propagate it.
        InputStream input = new FilterInputStream(new ByteArrayInputStream(testData)) {
            @Override
            public int read() throws IOException {
                throw new IOException("Test Exception");
            }
        };

        new Base64InputStream(input, true);
    }

    @Test
    public void testBase64InputStream_consumesInputData() throws IOException {
        // Test that data is consumed from the input stream while encoding or decoding.
        byte[] testData2 = new byte[]{1, 2, 3, 4};
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(testData2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        new Base64InputStream(inputStream2, true).writeTo(outputStream);

        byte[] expectedEncodedData = Base64.encodeBase64(testData2);
        byte[] actualEncodedData = outputStream.toByteArray();

        Assert.assertArrayEquals(expectedEncodedData, actualEncodedData);

        // Check if any data is left in the input stream.
        int data = inputStream2.read();
        Assert.assertEquals(-1, data);
    }

    @Test
    public void testBase64InputStream_defaultDecodeBehavior() {
        // Test default decode behavior.
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream("SGVsbG8sIHdvcmxkIQ==".getBytes()));
        byte[] buffer = new byte[1024];

        int read = base64InputStream.read(buffer);
        Assert.assertEquals("An error occurred while decoding.", "HelloWorld!", new String(buffer, 0, read));
    }

    @Test
    public void testBase64InputStream_customDecodeBehavior() {
        // Test custom decode behavior.
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream("SGVsbG8sIHdvcmxkIQ==".getBytes()), false);
        byte[] buffer = new byte[1024];

        int read = base64InputStream.read(buffer);
        Assert.assertEquals("An error occurred while encoding.", new String(testData).getBytes(), buffer, 0, read);
    }
}