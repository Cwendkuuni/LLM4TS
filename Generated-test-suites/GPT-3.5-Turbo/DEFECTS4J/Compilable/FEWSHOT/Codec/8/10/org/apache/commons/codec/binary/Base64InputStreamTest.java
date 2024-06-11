package org.apache.commons.codec.binary;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class Base64InputStreamTest {
    
    private Base64InputStream base64Input;
    
    private byte[] testData;
    
    @Before
    public void setup() {
        testData = "Hello World".getBytes();
        InputStream inputStream = new ByteArrayInputStream(testData);
        base64Input = new Base64InputStream(inputStream);
    }
    
    @Test
    public void testRead() throws IOException {
        int result = base64Input.read();
        assertEquals(testData[0], result);
        result = base64Input.read();
        assertEquals(testData[1], result);
    }
    
    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] result = new byte[5];
        int bytesRead = base64Input.read(result, 0, 5);
        assertEquals(5, bytesRead);
        assertEquals(testData[0], result[0]);
        assertEquals(testData[1], result[1]);
        assertEquals(testData[2], result[2]);
        assertEquals(testData[3], result[3]);
        assertEquals(testData[4], result[4]);
    }
    
    @Test
    public void testReadEndOfFile() throws IOException {
        byte[] result = new byte[11];
        int bytesRead = base64Input.read(result, 0, 11);
        assertEquals(-1, base64Input.read());
        assertNull(result[10]);
    }
    
    @Test
    public void testMarkSupported() {
        assertFalse(base64Input.markSupported());
    }
    
    @Test
    public void testBase64InputStreamWithoutEncoding() throws IOException {
        byte[] result = new byte[11];
        InputStream inputStream = new ByteArrayInputStream(testData);
        base64Input = new Base64InputStream(inputStream, false);
        int bytesRead = base64Input.read(result, 0, 11);
        assertEquals(11, bytesRead);
        assertEquals(testData[0], result[0]);
        assertEquals(testData[1], result[1]);
        assertEquals(testData[2], result[2]);
        assertEquals(testData[3], result[3]);
        assertEquals(testData[4], result[4]);
        assertEquals(testData[5], result[5]);
        assertEquals(testData[6], result[6]);
        assertEquals(testData[7], result[7]);
        assertEquals(testData[8], result[8]);
        assertEquals(testData[9], result[9]);
        assertEquals(testData[10], result[10]);
    }
    
    @Test
    public void testBase64InputStreamWithEncoding() throws IOException {
        byte[] result = new byte[11];
        InputStream inputStream = new ByteArrayInputStream(testData);
        base64Input = new Base64InputStream(inputStream, true);
        int bytesRead = base64Input.read(result, 0, 11);
        assertEquals(15, bytesRead);
        assertEquals(-1, base64Input.read());
        assertEquals(testData[0], result[0]);
        assertEquals(testData[1], result[1]);
        assertEquals(testData[2], result[2]);
        assertEquals(testData[3], result[3]);
        assertEquals(testData[4], result[4]);
        assertEquals(testData[5], result[5]);
        assertEquals(testData[6], result[6]);
        assertEquals(testData[7], result[7]);
        assertEquals(testData[8], result[8]);
        assertEquals(testData[9], result[9]);
        assertEquals(testData[10], result[10]);
    }
    
    @Test
    public void testBase64InputStreamWithEncodingAndLineLength() throws IOException {
        byte[] result = new byte[11];
        InputStream inputStream = new ByteArrayInputStream(testData);
        base64Input = new Base64InputStream(inputStream, true, 10, null);
        int bytesRead = base64Input.read(result, 0, 11);
        assertEquals(15, bytesRead);
        assertEquals(-1, base64Input.read());
        assertEquals(testData[0], result[0]);
        assertEquals(testData[1], result[1]);
        assertEquals(testData[2], result[2]);
        assertEquals(testData[3], result[3]);
        assertEquals(testData[4], result[4]);
        assertEquals(testData[5], result[5]);
        assertEquals(testData[6], result[6]);
        assertEquals(testData[7], result[7]);
        assertEquals(testData[8], result[8]);
        assertEquals(testData[9], result[9]);
        assertEquals(testData[10], result[10]);
    }
    
    @Test
    public void testBase64InputStreamWithEncodingAndLineSeparator() throws IOException {
        byte[] result = new byte[11];
        InputStream inputStream = new ByteArrayInputStream(testData);
        byte[] lineSeparator = { 13, 10 };
        base64Input = new Base64InputStream(inputStream, true, 0, lineSeparator);
        int bytesRead = base64Input.read(result, 0, 11);
        assertEquals(15, bytesRead);
        assertEquals(-1, base64Input.read());
        assertEquals(testData[0], result[0]);
        assertEquals(testData[1], result[1]);
        assertEquals(testData[2], result[2]);
        assertEquals(testData[3], result[3]);
        assertEquals(testData[4], result[4]);
        assertEquals(testData[5], result[5]);
        assertEquals(testData[6], result[6]);
        assertEquals(testData[7], result[7]);
        assertEquals(testData[8], result[8]);
        assertEquals(testData[9], result[9]);
        assertEquals(testData[10], result[10]);
    }
}