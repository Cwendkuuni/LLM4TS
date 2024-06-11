import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        // Test case 1 - Reading a single byte from the input stream
        // Arrange
        InputStream in = new ByteArrayInputStream(new byte[]{0x61}); // "a" in ASCII
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        // Act
        int result = base64InputStream.read();
        
        // Assert
        assertEquals(97, result);
    }
    
    @Test
    public void testReadArray() {
        // Test case 1 - Reading array of bytes from the input stream
        // Arrange
        InputStream in = new ByteArrayInputStream(new byte[]{0x61, 0x62, 0x63}); // "abc" in ASCII
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] result = new byte[3];
        
        // Act
        base64InputStream.read(result, 0, 3);
        
        // Assert
        assertArrayEquals(new byte[]{97, 98, 99}, result);
    }

    @Test
    public void testMarkSupported() {
        // Test case 1 - Checking mark support
        // Arrange
        InputStream in = new ByteArrayInputStream(new byte[]{0x61}); // "a" in ASCII
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        // Act
        boolean result = base64InputStream.markSupported();
        
        // Assert
        assertFalse(result);
    }
}