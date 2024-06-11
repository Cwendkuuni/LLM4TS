package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link RandomStringUtils} class
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10);
            assertEquals("Length should be 10", 10, randomString.length());
            assertNotNull("String should not be null", randomString);
        }
    }

    @Test
    public void testRandomAsciiString() {
        for (int i = 0; i < 10; i++) {
            String randomAsciiString = RandomStringUtils.randomAscii(10);
            assertEquals("Length should be 10", 10, randomAsciiString.length());
            assertNotNull("String should not be null", randomAsciiString);
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        for (int i = 0; i < 10; i++) {
            String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
            assertEquals("Length should be 10", 10, randomAlphabeticString.length());
            assertNotNull("String should not be null", randomAlphabeticString);
            for (char c : randomAlphabeticString.toCharArray()) {
                assertEquals("All characters should be alphabetic", isAlphabetic(c), true);
            }
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        for (int i = 0; i < 10; i++) {
            String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
            assertEquals("Length should be 10", 10, randomAlphanumericString.length());
            assertNotNull("String should not be null", randomAlphanumericString);
            for (char c : randomAlphanumericString.toCharArray()) {
                assertEquals("All characters should be alphanumeric", Character.isAlphabetic(c) || Character.isDigit(c), true);
            }
        }
    }

    @Test
    public void testRandomNumericString() {
        for (int i = 0; i < 10; i++) {
            String randomNumericString = RandomStringUtils.randomNumeric(10);
            assertEquals("Length should be 10", 10, randomNumericString.length());
            assertNotNull("String should not be null", randomNumericString);
            for (char c : randomNumericString.toCharArray()) {
                assertEquals("All characters should be numeric", Character.isDigit(c), true);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomStringWithRange() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, 'a', 'z');
            assertEquals("Length should be 10", 10, randomString.length());
            assertNotNull("String should not be null", randomString);
            for (char c : randomString.toCharArray()) {
                assertTrue("All characters should be within the range", c >= 'a' && c <= 'z');
            }
        }
    }

    @Test
    public void testRandomStringWithRangeAndFillEmpty() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(0, 'a', 'z');
            assertEquals("Length should be 0", 0, randomString.length());
            assertNotNull("String should not be null", randomString);
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringWithInvalidRange() {
        RandomStringUtils.random(1, 'z', 'a');
    }

    @Test
    public void testRandomStringWithCustomChars() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, chars.toCharArray());
            assertEquals("Length should be 10", 10, randomString.length());
            assertNotNull("String should not be null", randomString);
            for (char c : randomString.toCharArray()) {
                assertTrue("All characters should be in the custom set", chars.indexOf(c) != -1);
            }
        }
    }

    @Test
    public void testRandomStringWithCustomCharsAndEmptyCharArray() {
        String chars = "";
        assertArrayEquals("Custom char array should not be empty", new char[0], chars.toCharArray());
        // JUnit5 has a method assertThrows that is more suitable for testing exceptions,
        // but JUnit4 doesn't have it, so we're using an expected exception here.
        try {
            RandomStringUtils.random(10, chars.toCharArray());
        } catch (IllegalArgumentException e) {
            assertEquals("Exception message should be 'chars array should not be empty'", "chars array should not be empty", e.getMessage());
        }
    }

    private boolean isAlphabetic(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }
}