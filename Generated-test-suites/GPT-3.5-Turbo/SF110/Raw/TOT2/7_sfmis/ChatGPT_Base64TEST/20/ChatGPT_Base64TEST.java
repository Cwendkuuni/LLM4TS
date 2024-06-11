import static org.junit.Assert.*;
import org.junit.Test;

public class ChatGPT_Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.altBase64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Edge case: Empty input
        byte[] emptyExpected = {};
        byte[] emptyActual = Base64.altBase64ToByteArray("");
        assertArrayEquals(emptyExpected, emptyActual);
        
        // Exception handling: Invalid input
        try {
            Base64.altBase64ToByteArray("INVALID");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical use case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.base64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Edge case: Empty input
        byte[] emptyExpected = {};
        byte[] emptyActual = Base64.base64ToByteArray("");
        assertArrayEquals(emptyExpected, emptyActual);
        
        // Exception handling: Invalid input
        try {
            Base64.base64ToByteArray("INVALID");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
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
        byte[] emptyInput = {};
        String emptyExpected = "";
        String emptyActual = Base64.byteArrayToAltBase64(emptyInput);
        assertEquals(emptyExpected, emptyActual);
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical use case
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
        
        // Edge case: Empty input
        byte[] emptyInput = {};
        String emptyExpected = "";
        String emptyActual = Base64.byteArrayToBase64(emptyInput);
        assertEquals(emptyExpected, emptyActual);
    }

    @Test
    public void testMain() {
        // Typical use case
        String[] args = {};
        Base64.main(args); // Just test that the method can be called without throwing an exception
        
        // Edge case: Null input
        String[] nullArgs = null;
        Base64.main(nullArgs); // Just test that the method can be called without throwing an exception
    }
}