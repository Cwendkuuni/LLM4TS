package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {
    @Test
    public void testConstructor() {
        // Test constructor with InputStream parameter
        InputStream inputStream = new ByteArrayInputStream("Hello".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertNotNull(base64InputStream);
        
        // Test constructor with InputStream and doEncode parameters
        inputStream = new ByteArrayInputStream("World".getBytes());
        base64InputStream = new Base64InputStream(inputStream, true);
        assertNotNull(base64InputStream);
        
        // Test constructor with InputStream, doEncode, lineLength, and lineSeparator parameters
        byte[] lineSeparator = {'\r', '\n'};
        base64InputStream = new Base64InputStream(inputStream, false, 76, lineSeparator);
        assertNotNull(base64InputStream);
    }
    
    @Test
    public void testRead() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        // Test reading bytes from the stream
        int bytesRead = 0;
        int expectedByte = Base64.decode(inputBytes[bytesRead]);
        while ((bytesRead = base64InputStream.read()) != -1) {
            assertEquals(expectedByte, bytesRead);
            expectedByte = Base64.decode(inputBytes[++bytesRead]);
        }
    }
    
    @Test
    public void testReadArray() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        byte[] outputBytes = new byte[100];
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        // Test reading bytes into a byte array
        int bytesRead = base64InputStream.read(outputBytes, 0, inputBytes.length);
        assertEquals(inputBytes.length, bytesRead);
        
        // Compare the input bytes with the output bytes
        assertArrayEquals(inputBytes, outputBytes);
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadArrayWithNullArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello, world!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        // Test with null array parameter
        base64InputStream.read(null, 0, 10);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayWithInvalidLength() throws IOException {
        byte[] outputBytes = new byte[100];
        InputStream inputStream = new ByteArrayInputStream("Hello, world!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        // Test with invalid length parameter
        base64InputStream.read(outputBytes, 0, -1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayWithInvalidLengthAndOffset() throws IOException {
        byte[] outputBytes = new byte[100];
        InputStream inputStream = new ByteArrayInputStream("Hello, world!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        // Test with invalid length and offset parameters
        base64InputStream.read(outputBytes, 100, -1);
    }
    
    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream("Hello, world!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        // Test mark supported
        assertFalse(base64InputStream.markSupported());
    }
}