import static org.junit.Assert.*;

import org.junit.Test;

public class ChatGPT_Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.altBase64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Edge case: Empty input string
        expected = new byte[0];
        actual = Base64.altBase64ToByteArray("");
        assertArrayEquals(expected, actual);
        
        // Edge case: Invalid input string
        try {
            actual = Base64.altBase64ToByteArray("12345");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical use case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.base64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Edge case: Empty input string
        expected = new byte[0];
        actual = Base64.base64ToByteArray("");
        assertArrayEquals(expected, actual);
        
        // Edge case: Invalid input string
        try {
            actual = Base64.base64ToByteArray("12345");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
        
        // Edge case: Empty input array
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
        
        // Edge case: Empty input array
        input = new byte[0];
        expected = "";
        actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testMain() {
        // Typical use case
        String[] args = {};
        Base64.main(args);
    }

    @Test
    public void testMainInvalidArg() {
        // Error scenario: Invalid arguments
        String[] args = {"arg1", "arg2"};
        try {
            Base64.main(args);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}