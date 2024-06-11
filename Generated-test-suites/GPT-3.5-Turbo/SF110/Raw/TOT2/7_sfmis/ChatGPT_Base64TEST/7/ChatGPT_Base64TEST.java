import static org.junit.Assert.*;

import org.junit.Test;

public class ChatGPT_Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical Use Case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.altBase64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);

        // Edge Case: Empty input
        byte[] emptyExpected = {};
        byte[] emptyActual = Base64.altBase64ToByteArray("");
        assertArrayEquals(emptyExpected, emptyActual);

        // Exception Handling: Invalid base64 input
        try {
            Base64.altBase64ToByteArray("invalid_base64");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Typical Use Case
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.base64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);

        // Edge Case: Empty input
        byte[] emptyExpected = {};
        byte[] emptyActual = Base64.base64ToByteArray("");
        assertArrayEquals(emptyExpected, emptyActual);

        // Exception Handling: Invalid base64 input
        try {
            Base64.base64ToByteArray("invalid_base64");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Typical Use Case
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);

        // Edge Case: Empty input
        byte[] emptyInput = {};
        String emptyExpected = "";
        String emptyActual = Base64.byteArrayToAltBase64(emptyInput);
        assertEquals(emptyExpected, emptyActual);

        // Exception Handling: Null input
        try {
            Base64.byteArrayToAltBase64(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testByteArrayToBase64() {
        // Typical Use Case
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);

        // Edge Case: Empty input
        byte[] emptyInput = {};
        String emptyExpected = "";
        String emptyActual = Base64.byteArrayToBase64(emptyInput);
        assertEquals(emptyExpected, emptyActual);

        // Exception Handling: Null input
        try {
            Base64.byteArrayToBase64(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testMain() {
        // Typical Use Case
        String[] args = {};
        Base64.main(args); // Just test that the method can be called without throwing an exception

        // Edge Case: Null argument
        String[] nullArgs = null;
        Base64.main(nullArgs); // Test that the method can handle null arguments without throwing an exception
    }
}