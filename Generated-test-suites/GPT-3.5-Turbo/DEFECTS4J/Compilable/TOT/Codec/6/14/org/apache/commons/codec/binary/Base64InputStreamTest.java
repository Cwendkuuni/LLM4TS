package org.apache.commons.codec.binary;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead_singleByte() throws Exception {
        // Arrange
        Base64InputStream base64InputStream = new Base64InputStream(null);
        
        // Act
        int result = base64InputStream.read();
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testRead_byteArray() throws Exception {
        // Arrange
        Base64InputStream base64InputStream = new Base64InputStream(null);
        byte[] buffer = new byte[10];
        
        // Act
        int result = base64InputStream.read(buffer, 0, 10);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testRead_nullArray() throws Exception {
        // Arrange
        Base64InputStream base64InputStream = new Base64InputStream(null);
        
        // Act
        base64InputStream.read(null, 0, 10);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_invalidOffset() throws Exception {
        // Arrange
        Base64InputStream base64InputStream = new Base64InputStream(null);
        byte[] buffer = new byte[10];
        
        // Act
        base64InputStream.read(buffer, -1, 10);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_invalidLength() throws Exception {
        // Arrange
        Base64InputStream base64InputStream = new Base64InputStream(null);
        byte[] buffer = new byte[10];
        
        // Act
        base64InputStream.read(buffer, 0, -1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_invalidBuffer() throws Exception {
        // Arrange
        Base64InputStream base64InputStream = new Base64InputStream(null);
        byte[] buffer = new byte[10];
        
        // Act
        base64InputStream.read(buffer, 0, 20);
    }
    
    @Test
    public void testMarkSupported() throws Exception {
        // Arrange
        Base64InputStream base64InputStream = new Base64InputStream(null);
        
        // Act
        boolean result = base64InputStream.markSupported();
        
        // Assert
        assertFalse(result);
    }
}