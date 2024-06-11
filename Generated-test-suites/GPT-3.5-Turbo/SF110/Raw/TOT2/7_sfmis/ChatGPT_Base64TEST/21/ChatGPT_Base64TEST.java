import static org.junit.Assert.*;

import org.junit.Test;

public class ChatGPT_Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
        byte[] actual = Base64.altBase64ToByteArray("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/");
        assertArrayEquals(expected, actual);
        
        byte[] expectedEmpty = {};
        byte[] actualEmpty = Base64.altBase64ToByteArray("");
        assertArrayEquals(expectedEmpty, actualEmpty);
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
        byte[] actual = Base64.base64ToByteArray("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/");
        assertArrayEquals(expected, actual);
        
        byte[] expectedEmpty = {};
        byte[] actualEmpty = Base64.base64ToByteArray("");
        assertArrayEquals(expectedEmpty, actualEmpty);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
        
        byte[] emptyInput = {};
        String expectedEmpty = "";
        String actualEmpty = Base64.byteArrayToAltBase64(emptyInput);
        assertEquals(expectedEmpty, actualEmpty);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
        
        byte[] emptyInput = {};
        String expectedEmpty = "";
        String actualEmpty = Base64.byteArrayToBase64(emptyInput);
        assertEquals(expectedEmpty, actualEmpty);
    }

    @Test
    public void testMain() {
        String[] args = {};
        Base64.main(args); // Just test that the method can be called without throwing an exception
    }
}