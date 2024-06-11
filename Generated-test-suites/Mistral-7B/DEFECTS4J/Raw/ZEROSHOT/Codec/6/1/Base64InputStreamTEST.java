package org.apache.commons.codec.binary;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class Base64InputStreamTest {

    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        byte[] inputData = "HelloWorld!".getBytes();
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(inputData);
    }

    @After
    public void tearDown() {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBase64InputStreamDecoding() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        int readByte;
        while ((readByte = base64InputStream.read()) != -1) {
            output.write(readByte);
        }

        byte[] expectedOutputData = "HelloWorld!".getBytes();
        byte[] outputData = output.toByteArray();
        assertEquals("Expected decoded data to match original data", expectedOutputData, outputData);
    }

    @Test
    public void testBase64InputStreamEncoding() throws IOException {
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        int readByte;
        while ((readByte = base64InputStream.read()) != -1) {
            base64OutputStream.write(readByte);
        }

        base64OutputStream.finish();

        String expectedOutputData = "SGVsbG8gV29ybGQh";
        String outputData = new String(outputStream.toByteArray(), "US-ASCII");
        assertEquals("Expected encoded data to match base64 encoding of input data", expectedOutputData, outputData);
    }

    @Test
    public void testBase64InputStreamEmptyInput() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        int readByte = base64InputStream.read();
        assertNull("Expected EOF after reading empty input", readByte);
    }

    @Test(expected = IOException.class)
    public void testBase64InputStreamInvalidInput() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read();
        base64InputStream.read(); // Should read next byte, but will throw an IOException instead
    }
}