package org.apache.commons.codec.binary;

import static org.junit.Assert.*;
import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testConstructor1() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        
        // Act
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        // Assert
        assertNotNull(base64InputStream);
    }

    @Test
    public void testConstructor2() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        
        // Act
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        
        // Assert
        assertNotNull(base64InputStream);
    }

    @Test
    public void testConstructor3() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        int lineLength = 76;
        byte[] lineSeparator = "\r\n".getBytes();
        
        // Act
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, lineLength, lineSeparator);
        
        // Assert
        assertNotNull(base64InputStream);
    }

    @Test
    public void testRead() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        // Act
        int result = base64InputStream.read();
        
        // Assert
        assertEquals(116, result);
    }

    @Test
    public void testReadWithBytes() {
        // Arrange
        byte[] inputBytes = "test".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[4];
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        // Act
        int result = base64InputStream.read(outputBytes, 0, 4);
        
        // Assert
        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBytes() throws IOException {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        // Act
        base64InputStream.read(null, 0, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        // Act
        byte[] outputBytes = new byte[4];
        base64InputStream.read(outputBytes, 5, 4);
    }

    @Test
    public void testMarkSupported() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        // Act
        boolean result = base64InputStream.markSupported();
        
        // Assert
        assertFalse(result);
    }
}