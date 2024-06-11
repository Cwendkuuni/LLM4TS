import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Base64Test {

    @Before
    public void setUp() {
        // Initialize any necessary variables or objects before each test
    }

    @Test
    public void testAltBase64ToByteArray_typicalCase() {
        String input = "SGVsbG8gd29ybGQ="; // "Hello world" in base64
        byte[] expectedOutput = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100}; // ASCII values of "Hello world"
        byte[] actualOutput = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testBase64ToByteArray_typicalCase() {
        String input = "SGVsbG8gd29ybGQ="; // "Hello world" in base64
        byte[] expectedOutput = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100}; // ASCII values of "Hello world"
        byte[] actualOutput = Base64.base64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAltBase64ToByteArray_edgeCase_EmptyString() {
        String input = "";
        byte[] expectedOutput = {};
        byte[] actualOutput = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testBase64ToByteArray_edgeCase_EmptyString() {
        String input = "";
        byte[] expectedOutput = {};
        byte[] actualOutput = Base64.base64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_errorCase_InvalidStringLength() {
        String input = "SGVsbG8gd29ybGQ"; // Invalid base64 string with length not being multiple of four
        Base64.altBase64ToByteArray(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_errorCase_InvalidStringLength() {
        String input = "SGVsbG8gd29ybGQ"; // Invalid base64 string with length not being multiple of four
        Base64.base64ToByteArray(input);
    }

    // Additional test cases for other methods can be added similarly

}