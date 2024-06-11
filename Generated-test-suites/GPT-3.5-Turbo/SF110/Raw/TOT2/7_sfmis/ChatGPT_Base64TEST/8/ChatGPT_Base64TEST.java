import static org.junit.Assert.*;

import org.junit.Test;

public class ChatGPT_Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical Use Case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.altBase64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);

        // Edge case - empty string
        byte[] expectedEmpty = new byte[0];
        byte[] actualEmpty = Base64.altBase64ToByteArray("");
        assertArrayEquals(expectedEmpty, actualEmpty);

        // Error scenario - invalid input
        try {
            Base64.altBase64ToByteArray("invalidInput");
            fail("Should have thrown an exception for invalid input");
        } catch (IllegalArgumentException e) {
            // Expected exception thrown
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical Use Case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.base64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);

        // Edge case - null input
        byte[] actualNull = Base64.base64ToByteArray(null);
        assertNull(actualNull);

        // Error scenario - invalid input
        try {
            Base64.base64ToByteArray("invalidInput");
            fail("Should have thrown an exception for invalid input");
        } catch (IllegalArgumentException e) {
            // Expected exception thrown
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical Use Case
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);

        // Edge case - empty byte array
        byte[] inputEmpty = new byte[0];
        String expectedEmpty = "";
        String actualEmpty = Base64.byteArrayToAltBase64(inputEmpty);
        assertEquals(expectedEmpty, actualEmpty);

        // Error scenario - null input
        try {
            Base64.byteArrayToAltBase64(null);
            fail("Should have thrown an exception for null input");
        } catch (IllegalArgumentException e) {
            // Expected exception thrown
        }
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical Use Case
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);

        // Edge case - empty byte array
        byte[] inputEmpty = new byte[0];
        String expectedEmpty = "";
        String actualEmpty = Base64.byteArrayToBase64(inputEmpty);
        assertEquals(expectedEmpty, actualEmpty);

        // Error scenario - null input
        try {
            Base64.byteArrayToBase64(null);
            fail("Should have thrown an exception for null input");
        } catch (IllegalArgumentException e) {
            // Expected exception thrown
        }
    }

    @Test
    public void testMain() {
        // Test that the method can be called without throwing an exception
        String[] args = {};
        Base64.main(args);
    }
}