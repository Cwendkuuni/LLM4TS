import static org.junit.Assert.*;

import org.junit.Test;

public class ChatGPT_Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.altBase64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);

        // Test empty input
        byte[] expectedEmpty = {};
        byte[] actualEmpty = Base64.altBase64ToByteArray("");
        assertArrayEquals(expectedEmpty, actualEmpty);

        // Test invalid input
        try {
            Base64.altBase64ToByteArray("12345");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.base64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);

        // Test empty input
        byte[] expectedEmpty = {};
        byte[] actualEmpty = Base64.base64ToByteArray("");
        assertArrayEquals(expectedEmpty, actualEmpty);

        // Test invalid input
        try {
            Base64.base64ToByteArray("12345");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);

        // Test edge case
        byte[] inputEdge = {};
        String expectedEdge = "";
        String actualEdge = Base64.byteArrayToAltBase64(inputEdge);
        assertEquals(expectedEdge, actualEdge);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);

        // Test edge case
        byte[] inputEdge = {};
        String expectedEdge = "";
        String actualEdge = Base64.byteArrayToBase64(inputEdge);
        assertEquals(expectedEdge, actualEdge);
    }

    @Test
    public void testMain() {
        String[] args = {};
        Base64.main(args); // Just test that the method can be called without throwing an exception
    }
}