import static org.junit.Assert.*;
import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws Exception {
        // Arrange
        byte[] input = new byte[10];
        for (int i = 0; i < input.length; i++) {
            input[i] = (byte)i;
        }
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        // Act
        int result = base64InputStream.read();
        
        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testRead_withOffsetAndLength() throws Exception {
        // Arrange
        byte[] input = new byte[10];
        for (int i = 0; i < input.length; i++) {
            input[i] = (byte)i;
        }
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] output = new byte[10];
        
        // Act
        int result = base64InputStream.read(output, 0, 5);
        
        // Assert
        assertEquals(5, result);
        for (int i = 0; i < output.length; i++) {
            if (i < 5) {
                assertEquals((byte)i, output[i]);
            } else {
                assertEquals(0, output[i]);
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void testRead_withNullArray() throws Exception {
        // Arrange
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        // Act
        base64InputStream.read(null, 0, 5);
        
        // Assert
        // Exception is expected
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_withInvalidOffset() throws Exception {
        // Arrange
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] output = new byte[10];
        
        // Act
        base64InputStream.read(output, -1, 5);
        
        // Assert
        // Exception is expected
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_withInvalidLength() throws Exception {
        // Arrange
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] output = new byte[10];
        
        // Act
        base64InputStream.read(output, 0, -1);
        
        // Assert
        // Exception is expected
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_withInvalidArraySize() throws Exception {
        // Arrange
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] output = new byte[5];
        
        // Act
        base64InputStream.read(output, 0, 10);
        
        // Assert
        // Exception is expected
    }
    
    @Test
    public void testMarkSupported() {
        // Arrange
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        // Act
        boolean result = base64InputStream.markSupported();
        
        // Assert
        assertFalse(result);
    }
    
    // TODO: Add test methods for other non-trivial methods in Base64InputStream
    
}