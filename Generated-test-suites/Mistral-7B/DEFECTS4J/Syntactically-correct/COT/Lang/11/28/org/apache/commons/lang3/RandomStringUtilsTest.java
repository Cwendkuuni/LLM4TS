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
     * Test method for {@link RandomStringUtils#random(int)} .
     */
    @Test
    public void testRandom() {
        int count = 5;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length should match", count, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)} .
     */
    @Test
    public void testRandomAscii() {
        int count = 5;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals("Length should match", count, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            char c = randomAsciiString.charAt(i);
            assertTrue("Character should be ASCII", (c >= 32 && c <= 126));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)} .
     */
    @Test
    public void testRandomAlphabetic() {
        int count = 5;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Length should match", count, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            char c = randomAlphabeticString.charAt(i);
            assertTrue("Character should be alphabetic", Character.isAlphabetic(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)} .
     */
    @Test
    public void testRandomAlphanumeric() {
        int count = 5;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Length should match", count, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char c = randomAlphanumericString.charAt(i);
            assertTrue("Character should be alphanumeric", Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)} .
     */
    @Test
    public void testRandomNumeric() {
        int count = 5;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertEquals("Length should match", count, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            char c = randomNumericString.charAt(i);
            assertTrue("Character should be numeric", Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean)} .
     */
    @Test
    public void testRandomWithCustomRange() {
        int count = 5;
        int start = 65;
        int end = 90;
        String randomString = RandomStringUtils.random(count, start, end, false, false);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be in the range", (c >= start && c <= end));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} .
     */
    @Test
    public void testRandomWithCustomChars() {
        int count = 5;
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f'};
        String randomString = RandomStringUtils.random(count, 0, chars.length, false, false, chars, new Random());
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be in the set", contains(chars, c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, String)} .
     */
    @Test
    public void testRandomWithCustomString() {
        int count = 5;
        String customChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(count, customChars);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be in the set", customChars.indexOf(c) != -1);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} with edge cases.
     */
    @Test
    public void testRandomWithCustomCharsEdgeCases() {
        int count = 1;
        char[] chars = {'a'};
        String randomString = RandomStringUtils.random(count, 0, chars.length, false, false, chars, new Random());
        assertEquals("Length should match", count, randomString.length());
        assertTrue("String should not be empty", !randomString.isEmpty());

        count = Integer.MAX_VALUE;
        String errorMessage = String.format("Requested random string length %d is greater than the maximum allowed value %d.", count, Integer.MAX_VALUE);
        try {
            RandomStringUtils.random(count, 0, chars.length, false, false, chars, new Random());
            fail(errorMessage);
        } catch (IllegalArgumentException e) {
            assertEquals(errorMessage, e.getMessage());
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} with empty char array.
     */
    @Test
    public void testRandomWithEmptyCustomChars() {
        int count = 1;
        char[] chars = {};
        String errorMessage = String.format("The chars array must not be empty");
        try {
            RandomStringUtils.random(count, 0, chars.length, false, false, chars, new Random());
            fail(errorMessage);
        } catch (IllegalArgumentException e) {
            assertEquals(errorMessage, e.getMessage());
        }
    }

    /**
     * Helper method to check if a character is in a char array.
     *
     * @param chars the char array to search in
     * @param c the character to search for
     * @return true if the character is in the array, false otherwise
     */
    private boolean contains(char[] chars, char c) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) {
                return true;
            }
        }
        return false;
    }
}