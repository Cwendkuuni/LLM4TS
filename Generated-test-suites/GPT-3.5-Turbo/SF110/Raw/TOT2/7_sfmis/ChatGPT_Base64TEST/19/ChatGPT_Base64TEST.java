import static org.junit.Assert.*;

import org.junit.Test;

public class Base64Test {

    // Test case for altBase64ToByteArray method
    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.altBase64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
    }

    // Test case for base64ToByteArray method
    @Test
    public void testBase64ToByteArray() {
        byte[] expected = { 1, 2, 3, 4, 5, 6 };
        byte[] actual = Base64.base64ToByteArray("AQIDBAUG");
        assertArrayEquals(expected, actual);
    }

    // Test case for byteArrayToAltBase64 method
    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
    }

    // Test case for byteArrayToBase64 method
    @Test
    public void testByteArrayToBase64() {
        byte[] input = { 1, 2, 3, 4, 5, 6 };
        String expected = "AQIDBAUG";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
    }

    // Test case for main method
    @Test
    public void testMain() {
        String[] args = {};
        Base64.main(args); // Just test that the method can be called without throwing an exception
    }

    // Additional edge case test for base64ToByteArray method with invalid input
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayWithInvalidInput() {
        Base64.base64ToByteArray("InvalidInput"); // Expecting IllegalArgumentException
    }

    // Additional edge case test for byteArrayToBase64 method with empty input
    @Test
    public void testByteArrayToBase64WithEmptyInput() {
        byte[] input = {};
        String expected = "";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
    }

    // Additional exception handling test for altBase64ToByteArray method with null input
    @Test(expected = NullPointerException.class)
    public void testAltBase64ToByteArrayWithNullInput() {
        Base64.altBase64ToByteArray(null); // Expecting NullPointerException
    }

}