package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

import org.junit.Test;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor Base64InputStream(InputStream in).
     */
    @Test
    public void testBase64InputStream() {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64is = new Base64InputStream(input);
        assertEquals("Test", base64is.readAllBytes());
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode).
     */
    @Test
    public void testBase64InputStreamWithEncoding() {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64is = new Base64InputStream(input, true);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] decoded = new byte[base64is.available()];
        base64is.read(decoded);
        assertEquals(decoded.length, input.available());
        assertEquals("Test", new String(decoded));
        ByteArrayOutputStream encodedOutput = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int length;
        while ((length = base64is.read(buffer)) > 0) {
            encodedOutput.write(buffer, 0, length);
        }
        assertEquals(encodedOutput.size(), input.available());
        byte[] encoded = encodedOutput.toByteArray();
        assertEquals(encoded.length, input.available());
        InputStream inputToDecode = new ByteArrayInputStream(encoded);
        Base64InputStream base64isToDecode = new Base64InputStream(inputToDecode);
        byte[] decodedData = new byte[base64isToDecode.available()];
        base64isToDecode.read(decodedData);
        assertEquals(decodedData.length, encoded.length);
        assertEquals(new String(decoded), "Test");
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength).
     */
    @Test
    public void testBase64InputStreamWithLineLength() {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64is = new Base64InputStream(input, true, 50);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] decoded = new byte[base64is.available()];
        base64is.read(decoded);
        assertEquals(decoded.length, input.available());
        assertEquals("Test", new String(decoded));
        ByteArrayOutputStream encodedOutput = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int length;
        while ((length = base64is.read(buffer)) > 0) {
            encodedOutput.write(buffer, 0, length);
        }
        assertEquals(encodedOutput.size(), input.available());
        byte[] encoded = encodedOutput.toByteArray();
        assertEquals(encoded.length, input.available());
        InputStream inputToDecode = new ByteArrayInputStream(encoded);
        Base64InputStream base64isToDecode = new Base64InputStream(inputToDecode, 50);
        byte[] decodedData = new byte[base64isToDecode.available()];
        base64isToDecode.read(decodedData);
        assertEquals(decodedData.length, encoded.length);
        assertEquals(new String(decoded), "Test");
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator).
     */
    @Test
    public void testBase64InputStreamWithLineSeparator() {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64 base64 = new Base64();
        byte[] lineSeparator = base64.lineSeparator();
        Base64InputStream base64is = new Base64InputStream(input, true, 50, lineSeparator);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] decoded = new byte[base64is.available()];
        base64is.read(decoded);
        assertEquals(decoded.length, input.available());
        assertEquals("Test", new String(decoded));
        ByteArrayOutputStream encodedOutput = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int length;
        while ((length = base64is.read(buffer)) > 0) {
            encodedOutput.write(buffer, 0, length);
        }
        assertEquals(encodedOutput.size(), input.available());
        byte[] encoded = encodedOutput.toByteArray();
        assertEquals(encoded.length, input.available());
        InputStream inputToDecode = new ByteArrayInputStream(encoded);
        Base64InputStream base64isToDecode = new Base64InputStream(inputToDecode, 50, lineSeparator);
        byte[] decodedData = new byte[base64isToDecode.available()];
        base64isToDecode.read(decodedData);
        assertEquals(decodedData.length, encoded.length);
        assertEquals(new String(decoded), "Test");
    }

    /**
     * Test method read().
     */
    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64is = new Base64InputStream(input);
        byte[] decoded = new byte[base64is.available()];
        int read = base64is.read(decoded);
        assertEquals(read, decoded.length);
        assertEquals(decoded.length, input.available());
        assertEquals("Test", new String(decoded));
    }

    /**
     * Test method read(byte[] b, int offset, int len).
     */
    @Test
    public void testReadByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64is = new Base64InputStream(input);
        byte[] decoded = new byte[base64is.available()];
        int read = base64is.read(decoded);
        assertEquals(read, decoded.length);
        assertEquals(decoded.length, input.available());
        assertEquals("Test", new String(decoded));
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode) with null InputStream.
     */
    @Test(expected = NullPointerException.class)
    public void testBase64InputStreamWithNullInputStream() {
        new Base64InputStream(null, false);
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode) with Stream which does not support read.
     */
    @Test(expected = IOException.class)
    public void testBase64InputStreamWithIOException() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test".getBytes());
        input.close();
        new Base64InputStream(input, false);
    }
}