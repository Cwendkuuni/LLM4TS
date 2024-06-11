package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test method for {@link RandomStringUtils#random(int)}.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean)}.
     */
    @Test
    public void testRandomWithOptions() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (!Character.isAlphabetic(c) && !Character.isDigit(c)) {
                throw new AssertionError("Unexpected character: " + c);
            }
        }

        String randomStringWithNoLetters = RandomStringUtils.random(10, false, true);
        assertNotNull(randomStringWithNoLetters);
        assertEquals(10, randomStringWithNoLetters.length());
        for (char c : randomStringWithNoLetters.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new AssertionError("Unexpected character: " + c);
            }
        }

        String randomStringWithNoNumbers = RandomStringUtils.random(10, true, false);
        assertNotNull(randomStringWithNoNumbers);
        assertEquals(10, randomStringWithNoNumbers.length());
        for (char c : randomStringWithNoNumbers.toCharArray()) {
            if (!Character.isLetter(c)) {
                throw new AssertionError("Unexpected character: " + c);
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithCustomSet() {
        String customSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomString = RandomStringUtils.random(10, 0, customSet.length(), true, true, customSet.toCharArray(), new Random());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (customSet.indexOf(c) == -1) {
                throw new AssertionError("Unexpected character: " + c);
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     * Tests the behavior when the set contains no characters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptySet() {
        String emptySet = "";
        RandomStringUtils.random(10, 0, emptySet.length(), true, true, emptySet.toCharArray(), new Random());
    }
}