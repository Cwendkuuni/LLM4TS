package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Typical use case
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());

        // Edge case: count is 0
        randomString = RandomStringUtils.random(0);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomAscii() {
        // Typical use case
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());

        // Edge case: count is 0
        randomString = RandomStringUtils.randomAscii(0);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomAlphabetic() {
        // Typical use case
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[a-zA-Z]+"));

        // Edge case: count is 0
        randomString = RandomStringUtils.randomAlphabetic(0);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomAlphanumeric() {
        // Typical use case
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[a-zA-Z0-9]+"));

        // Edge case: count is 0
        randomString = RandomStringUtils.randomAlphanumeric(0);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomNumeric() {
        // Typical use case
        String randomString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[0-9]+"));

        // Edge case: count is 0
        randomString = RandomStringUtils.randomNumeric(0);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomWithLettersOnly() {
        // Typical use case
        String randomString = RandomStringUtils.random(10, true, false);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[a-zA-Z]+"));

        // Error scenario: invalid input, both letters and numbers are false
        try {
            randomString = RandomStringUtils.random(10, false, false);
        } catch (IllegalArgumentException e) {
            assertEquals("Both letters and numbers cannot be false", e.getMessage());
        }
    }

    @Test
    public void testRandomWithNumbersOnly() {
        // Typical use case
        String randomString = RandomStringUtils.random(10, false, true);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[0-9]+"));

        // Error scenario: invalid input, both letters and numbers are false
        try {
            randomString = RandomStringUtils.random(10, false, false);
        } catch (IllegalArgumentException e) {
            assertEquals("Both letters and numbers cannot be false", e.getMessage());
        }
    }

    @Test
    public void testRandomWithCustomChars() {
        // Typical use case
        String randomString = RandomStringUtils.random(10, 65, 90, true, false);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[A-Z]+"));

        // Error scenario: invalid input, empty chars array
        try {
            randomString = RandomStringUtils.random(10, new char[] {});
        } catch (IllegalArgumentException e) {
            assertEquals("The provided chars array is empty", e.getMessage());
        }
    }

    @Test
    public void testRandomWithInvalidCount() {
        // Error scenario: negative count
        try {
            String randomString = RandomStringUtils.random(-1);
        } catch (IllegalArgumentException e) {
            assertEquals("Requested random string length -1 is less than 0.", e.getMessage());
        }
    }

    @Test
    public void testRandomWithStringChars() {
        // Typical use case
        String randomString = RandomStringUtils.random(10, "ABCabc123");
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[A-Za-z0-9]+"));

        // Error scenario: invalid input, empty string
        try {
            randomString = RandomStringUtils.random(10, "");
        } catch (IllegalArgumentException e) {
            assertEquals("The provided string is empty", e.getMessage());
        }
    }

}