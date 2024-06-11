import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Test typical use case
        String input = "SGVsbG8gV29ybGQh"; // "Hello World!"
        byte[] expected = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
        
        // Test edge case: empty string
        input = "";
        expected = new byte[0];
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
        
        // Test exception handling: string length not multiple of four
        input = "SGVsbG8="; // Invalid base64 string
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray() {
        // Test typical use case
        String input = "SGVsbG8gV29ybGQh"; // "Hello World!"
        byte[] expected = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
        
        // Test edge case: empty string
        input = "";
        expected = new byte[0];
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
        
        // Test exception handling: string length not multiple of four
        input = "SGVsbG8="; // Invalid base64 string
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Test typical use case
        byte[] input = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33}; // "Hello World!"
        String expected = "SGVsbG8gV29ybGQh";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
        
        // Test edge case: empty byte array
        input = new byte[0];
        expected = "";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
    }

    @Test
    public void testByteArrayToBase64() {
        // Test typical use case
        byte[] input = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33}; // "Hello World!"
        String expected = "SGVsbG8gV29ybGQh";
        assertEquals(expected, Base64.byteArrayToBase64(input));
        
        // Test edge case: empty byte array
        input = new byte[0];
        expected = "";
        assertEquals(expected, Base64.byteArrayToBase64(input));
    }

    // Additional test methods can be added here

}