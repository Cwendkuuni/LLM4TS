import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expectedOutput = { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        assertArrayEquals(expectedOutput, Base64.altBase64ToByteArray(input));
        
        // Edge case: empty string
        input = "";
        expectedOutput = new byte[0];
        assertArrayEquals(expectedOutput, Base64.altBase64ToByteArray(input));
        
        // Error scenario: String length not multiple of four
        input = "SGVsbG8gd29ybG==";
        try {
            Base64.altBase64ToByteArray(input);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }
    
    @Test
    public void testBase64ToByteArray() {
        // Typical use case
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expectedOutput = { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        assertArrayEquals(expectedOutput, Base64.base64ToByteArray(input));
        
        // Edge case: empty string
        input = "";
        expectedOutput = new byte[0];
        assertArrayEquals(expectedOutput, Base64.base64ToByteArray(input));
        
        // Error scenario: String length not multiple of four
        input = "SGVsbG8gd29ybG==";
        try {
            Base64.base64ToByteArray(input);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case
        byte[] input = { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        String expectedOutput = "SGVsbG8gd29ybGQ=";
        assertEquals(expectedOutput, Base64.byteArrayToAltBase64(input));
        
        // Edge case: empty byte array
        input = new byte[0];
        expectedOutput = "";
        assertEquals(expectedOutput, Base64.byteArrayToAltBase64(input));
    }
    
    @Test
    public void testByteArrayToBase64() {
        // Typical use case
        byte[] input = { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        String expectedOutput = "SGVsbG8gd29ybGQ=";
        assertEquals(expectedOutput, Base64.byteArrayToBase64(input));
        
        // Edge case: empty byte array
        input = new byte[0];
        expectedOutput = "";
        assertEquals(expectedOutput, Base64.byteArrayToBase64(input));
    }
}