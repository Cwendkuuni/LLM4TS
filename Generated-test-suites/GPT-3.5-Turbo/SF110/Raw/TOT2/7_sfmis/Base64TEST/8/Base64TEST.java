import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        // Typical case
        String input = "SGVsbG8=";
        byte[] expected = { 72, 101, 108, 108, 111 };
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
        
        // Edge case empty string
        input = "";
        expected = new byte[0];
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
        
        // Error scenario: Invalid character
        input = "ABC!";
        assertThrows(IllegalArgumentException.class, () -> {
            Base64.altBase64ToByteArray(input);
        });
    }
    
    @Test
    public void testBase64ToByteArray() {
        // Typical case
        String input = "SGVsbG8=";
        byte[] expected = { 72, 101, 108, 108, 111 };
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
        
        // Edge case empty string
        input = "";
        expected = new byte[0];
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        // Typical case
        byte[] input = { 72, 101, 108, 108, 111 };
        String expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
        
        // edge case: empty byte array
        input = new byte[0];
        expected = "";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
    }
    
    @Test
    public void testByteArrayToBase64() {
        // Typical case
        byte[] input = { 72, 101, 108, 108, 111 };
        String expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToBase64(input));
        
        // edge case: empty byte array
        input = new byte[0];
        expected = "";
        assertEquals(expected, Base64.byteArrayToBase64(input));
    }
    
    // Add more test cases for other methods if needed
    
}