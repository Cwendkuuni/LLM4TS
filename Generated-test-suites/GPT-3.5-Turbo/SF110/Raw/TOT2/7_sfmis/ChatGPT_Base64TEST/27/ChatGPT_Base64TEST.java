import static org.junit.Assert.*;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.altBase64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Edge case - empty string input
        expected = new byte[0];
        actual = Base64.altBase64ToByteArray("");
        assertArrayEquals(expected, actual);
        
        // Exception handling - invalid base64 input
        try {
            Base64.altBase64ToByteArray("123"); // Invalid base64
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical use case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.base64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Edge case - empty string input
        expected = new byte[0];
        actual = Base64.base64ToByteArray("");
        assertArrayEquals(expected, actual);
        
        // Exception handling - invalid base64 input
        try {
            Base64.base64ToByteArray("123"); // Invalid base64
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
        
        // Edge case - empty byte array input
        input = new byte[0];
        expected = "";
        actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical use case
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
        
        // Edge case - empty byte array input
        input = new byte[0];
        expected = "";
        actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testMain() {
        // Typical use case - no assertion, just checking for exceptions
        String[] args = {};
        Base64.main(args);
    }
}