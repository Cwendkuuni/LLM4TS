import static org.junit.Assert.*;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.altBase64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Edge case: Empty input
        expected = new byte[0];
        actual = Base64.altBase64ToByteArray("");
        assertArrayEquals(expected, actual);
        
        // Exception handling: Invalid input
        try {
            actual = Base64.altBase64ToByteArray("invalid_input");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical use case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.base64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Edge case: Empty input
        expected = new byte[0];
        actual = Base64.base64ToByteArray("");
        assertArrayEquals(expected, actual);
        
        // Exception handling: Invalid input
        try {
            actual = Base64.base64ToByteArray("invalid_input");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
        
        // Edge case: Empty input
        input = new byte[0];
        expected = "";
        actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
        
        // Exception handling: Null input
        try {
            actual = Base64.byteArrayToAltBase64(null);
            fail("Expected a NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Test passed
        }
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical use case
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
        
        // Edge case: Empty input
        input = new byte[0];
        expected = "";
        actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
        
        // Exception handling: Null input
        try {
            actual = Base64.byteArrayToBase64(null);
            fail("Expected a NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Test passed
        }
    }

    @Test
    public void testMain() {
        // Typical use case
        String[] args = {};
        Base64.main(args); // Just test that the method can be called without throwing an exception
    }
}