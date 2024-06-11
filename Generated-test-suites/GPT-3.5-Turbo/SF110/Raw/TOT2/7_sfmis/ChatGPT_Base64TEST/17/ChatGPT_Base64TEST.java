import static org.junit.Assert.*;

import org.junit.Test;

public class ChatGPT_Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.altBase64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Edge cases
        byte[] emptyArray = {};
        byte[] emptyResult = Base64.altBase64ToByteArray("");
        assertArrayEquals(emptyArray, emptyResult);
        
        // Exception handling
        try {
            Base64.altBase64ToByteArray("invalidBase64");
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
        
        // Edge cases
        byte[] emptyArray = {};
        byte[] emptyResult = Base64.base64ToByteArray("");
        assertArrayEquals(emptyArray, emptyResult);
        
        // Exception handling
        try {
            Base64.base64ToByteArray("invalidBase64");
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
        
        // Edge cases
        byte[] emptyArray = {};
        String emptyResult = Base64.byteArrayToAltBase64(emptyArray);
        assertEquals("", emptyResult);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
        
        // Edge cases
        byte[] emptyArray = {};
        String emptyResult = Base64.byteArrayToBase64(emptyArray);
        assertEquals("", emptyResult);
    }

    @Test
    public void testMain() {
        String[] args = {};
        Base64.main(args); // Just test that the method can be called without throwing an exception
    }
}