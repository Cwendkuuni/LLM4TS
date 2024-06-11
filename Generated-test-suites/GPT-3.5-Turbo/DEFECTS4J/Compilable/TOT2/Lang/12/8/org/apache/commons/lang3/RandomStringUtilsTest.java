package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Typical Use Case
        String randomString = RandomStringUtils.random(5);
        assertEquals(5, randomString.length());

        // Edge Cases
        randomString = RandomStringUtils.random(0);
        assertEquals("", randomString);

        randomString = RandomStringUtils.random(10, true, false);
        assertEquals(10, randomString.length());

        randomString = RandomStringUtils.random(20, false, true);
        assertEquals(20, randomString.length());

        // Error Scenarios
        try {
            RandomStringUtils.random(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRandomAscii() {
        // Typical Use Case
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertEquals(5, randomAsciiString.length());

        // Edge Cases
        randomAsciiString = RandomStringUtils.randomAscii(0);
        assertEquals("", randomAsciiString);

        randomAsciiString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomAsciiString.length());

        // Error Scenarios
        try {
            RandomStringUtils.randomAscii(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRandomAlphabetic() {
        // Typical Use Case
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, randomAlphabeticString.length());

        // Edge Cases
        randomAlphabeticString = RandomStringUtils.randomAlphabetic(0);
        assertEquals("", randomAlphabeticString);

        randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomAlphabeticString.length());

        // Error Scenarios
        try {
            RandomStringUtils.randomAlphabetic(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        // Typical Use Case
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, randomAlphanumericString.length());

        // Edge Cases
        randomAlphanumericString = RandomStringUtils.randomAlphanumeric(0);
        assertEquals("", randomAlphanumericString);

        randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomAlphanumericString.length());

        // Error Scenarios
        try {
            RandomStringUtils.randomAlphanumeric(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRandomNumeric() {
        // Typical Use Case
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertEquals(5, randomNumericString.length());

        // Edge Cases
        randomNumericString = RandomStringUtils.randomNumeric(0);
        assertEquals("", randomNumericString);

        randomNumericString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomNumericString.length());

        // Error Scenarios
        try {
            RandomStringUtils.randomNumeric(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Additional Tests for edge cases and exception handling can be added here

}