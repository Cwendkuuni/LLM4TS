import static org.junit.Assert.*;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.altBase64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Edge case: empty string
        expected = new byte[0];
        actual = Base64.altBase64ToByteArray("");
        assertArrayEquals(expected, actual);
        
        // Exception handling: invalid base64 string
        try {
            actual = Base64.altBase64ToByteArray("invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.base64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Edge case: empty string
        expected = new byte[0];
        actual = Base64.base64ToByteArray("");
        assertArrayEquals(expected, actual);
        
        // Exception handling: invalid base64 string
        try {
            actual = Base64.base64ToByteArray("invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
        
        // Edge case: empty byte array
        input = new byte[0];
        expected = "";
        actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
        
        // Edge case: empty byte array
        input = new byte[0];
        expected = "";
        actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testMain() {
        String[] args = {};
        Base64.main(args); // Just test that the method can be called without throwing an exception
    }
}