package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertTrue(randomAsciiString.matches("[ -~]+")); // Check if it contains only printable ASCII characters
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertTrue(randomAlphabeticString.matches("[a-zA-Z]+")); // Check if it contains only alphabetic characters
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertTrue(randomAlphanumericString.matches("[a-zA-Z0-9]+")); // Check if it contains only alphanumeric characters
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertTrue(randomNumericString.matches("[0-9]+")); // Check if it contains only numeric characters
    }

    @Test
    public void testRandomWithInvalidCount() {
        try {
            RandomStringUtils.random(-1); // Testing with negative count
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testRandomWithEmptyCharArray() {
        try {
            RandomStringUtils.random(10, new char[]{}); // Testing with empty char array
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testRandomWithInvalidRange() {
        try {
            RandomStringUtils.random(10, 'z', 'a', false, false); // Testing with invalid range
            fail("Expected an ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Success
        }
    }

    @Test
    public void testRandomWithEmptyString() {
        try {
            RandomStringUtils.random(10, ""); // Testing with empty string
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testRandomWithNullString() {
        String randomString = RandomStringUtils.random(10, (String) null); // Testing with null string
        assertEquals(10, randomString.length());
    }
}