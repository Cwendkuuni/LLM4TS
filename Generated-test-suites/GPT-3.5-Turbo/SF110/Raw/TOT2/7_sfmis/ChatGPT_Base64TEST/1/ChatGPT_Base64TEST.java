import static org.junit.Assert.*;

import org.junit.Test;

public class ChatGPT_Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = ChatGPT_Base64.altBase64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Edge case
        expected = new byte[0];
        actual = ChatGPT_Base64.altBase64ToByteArray("");
        assertArrayEquals(expected, actual);
        
        // Error scenario
        try {
            ChatGPT_Base64.altBase64ToByteArray("InvalidInput");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical use case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = ChatGPT_Base64.base64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
        
        // Edge case
        expected = new byte[0];
        actual = ChatGPT_Base64.base64ToByteArray("");
        assertArrayEquals(expected, actual);
        
        // Error scenario
        try {
            ChatGPT_Base64.base64ToByteArray("InvalidInput");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = ChatGPT_Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
        
        // Edge case
        input = new byte[0];
        expected = "";
        actual = ChatGPT_Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical use case
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = ChatGPT_Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
        
        // Edge case
        input = new byte[0];
        expected = "";
        actual = ChatGPT_Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testMain() {
        // Just test that the method can be called without throwing an exception
        String[] args = {};
        ChatGPT_Base64.main(args);
    }
}