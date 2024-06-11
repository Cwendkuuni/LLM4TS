package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

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
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Expected a random string of length " + count, count, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals("Expected a random string of length " + count, count, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            char c = randomAsciiString.charAt(i);
            assertTrue("Expected an ASCII character", (c >= 32 && c <= 126));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}.
     */
    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Expected a random string of length " + count, count, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            char c = randomAlphabeticString.charAt(i);
            assertTrue("Expected an alphabetic character", isAlpha(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Expected a random string of length " + count, count, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char c = randomAlphanumericString.charAt(i);
            assertTrue("Expected an alphanumeric character", isAlphabetic(c) || isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}.
     */
    @Test
    public void testRandomNumeric() {
        int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertEquals("Expected a random string of length " + count, count, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            char c = randomNumericString.charAt(i);
            assertTrue("Expected a numeric character", isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, boolean, boolean)}.
     */
    @Test
    public void testRandomWithOptions() {
        int count = 10;
        boolean includeLetters = true;
        boolean includeNumbers = true;
        String randomString = RandomStringUtils.random(count, includeLetters, includeNumbers);
        assertEquals("Expected a random string of length " + count, count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            if (includeLetters) {
                assertTrue("Expected an alphabetic character", isAlpha(c));
            }
            if (includeNumbers) {
                assertTrue("Expected a numeric character", isDigit(c));
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithCustomCharacterSet() {
        int count = 10;
        char[] characterSet = new char[]{'A', 'B', 'C'};
        Random random = new Random();
        String randomString = RandomStringUtils.random(count, 0, characterSet.length, true, true, characterSet, random);
        assertEquals("Expected a random string of length " + count, count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Expected a character from the character set", isOneOf(c, characterSet));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, String)}.
     */
    @Test
    public void testRandomWithStringCharacterSet() {
        int count = 10;
        String characterSet = "ABC";
        String randomString = RandomStringUtils.random(count, characterSet);
        assertEquals("Expected a random string of length " + count, count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Expected a character from the character set", isOneOf(c, characterSet.toCharArray()));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     * Test for the case where the set of characters is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterSet() {
        int count = 10;
        char[] characterSet = new char[0];
        Random random = new Random();
        RandomStringUtils.random(count, 0, characterSet.length, true, true, characterSet, random);
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     * Test for the case where the provided random instance is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNullRandomInstance() {
        int count = 10;
        char[] characterSet = new char[]{'A', 'B', 'C'};
        RandomStringUtils.random(count, 0, characterSet.length, true, true, characterSet, null);
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean)}.
     * Test for the case where the provided random instance is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNullRandomInstance2() {
        int count = 10;
        boolean includeLetters = true;
        boolean includeNumbers = true;
        RandomStringUtils.random(count, includeLetters, includeNumbers, null);
    }

    /**
     * Helper method for checking if a character is alphabetic.
     *
     * @param c the character to check
     * @return {@code true} if alphabetic, {@code false} otherwise
     */
    private static boolean isAlpha(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    /**
     * Helper method for checking if a character is numeric.
     *
     * @param c the character to check
     * @return {@code true} if numeric, {@code false} otherwise
     */
    private static boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    /**
     * Helper method for checking if a character is one of a set of characters.
     *
     * @param c the character to check
     * @param characters the set of characters to check against
     * @return {@code true} if the character is in the set, {@code false} otherwise
     */
    private static boolean isOneOf(char c, char[] characters) {
        for (char character : characters) {
            if (character == c) {
                return true;
            }
        }
        return false;
    }
}