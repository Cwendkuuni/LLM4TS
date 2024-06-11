package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

import org.junit.Test;

/**
 * Test class for RandomStringUtils.
 *
 */
public class RandomStringUtilsTest {

    private static final int MIN_LENGTH = 0;
    private static final int MAX_LENGTH = 10;
    private static final Random TEST_RANDOM = new Random();

    /**
     * Test method for {@link RandomStringUtils#random(int)}.
     */
    @Test
    public void testRandom() {
        for (int i = MIN_LENGTH; i <= MAX_LENGTH; i++) {
            String randomString = RandomStringUtils.random(i);
            assertTrue(randomString.length() == i);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        for (int i = MIN_LENGTH; i <= MAX_LENGTH; i++) {
            String randomAsciiString = RandomStringUtils.randomAscii(i);
            assertTrue(randomAsciiString.length() == i);
            for (int j = 0; j < randomAsciiString.length(); j++) {
                int asc = (int) randomAsciiString.charAt(j);
                assertTrue(asc >= 32 && asc <= 126);
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}.
     */
    @Test
    public void testRandomAlphabetic() {
        for (int i = MIN_LENGTH; i <= MAX_LENGTH; i++) {
            String randomAlphabeticString = RandomStringUtils.randomAlphabetic(i);
            assertTrue(randomAlphabeticString.length() == i);
            for (int j = 0; j < randomAlphabeticString.length(); j++) {
                char ch = randomAlphabeticString.charAt(j);
                assertTrue(Character.isAlphabetic(ch));
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        for (int i = MIN_LENGTH; i <= MAX_LENGTH; i++) {
            String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(i);
            assertTrue(randomAlphanumericString.length() == i);
            for (int j = 0; j < randomAlphanumericString.length(); j++) {
                char ch = randomAlphanumericString.charAt(j);
                assertTrue((Character.isAlphabetic(ch) || Character.isDigit(ch)));
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}.
     */
    @Test
    public void testRandomNumeric() {
        for (int i = MIN_LENGTH; i <= MAX_LENGTH; i++) {
            String randomNumericString = RandomStringUtils.randomNumeric(i);
            assertTrue(randomNumericString.length() == i);
            for (int j = 0; j < randomNumericString.length(); j++) {
                char ch = randomNumericString.charAt(j);
                assertTrue(Character.isDigit(ch));
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean)}.
     */
    @Test
    public void testRandomWithRange() {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = MIN_LENGTH; i <= MAX_LENGTH; i++) {
            String randomString = RandomStringUtils.random(i, 0, chars.length(), true, false, null, TEST_RANDOM);
            assertTrue(randomString.length() == i);
            for (int j = 0; j < randomString.length(); j++) {
                char ch = randomString.charAt(j);
                assertTrue(chars.indexOf(ch) != -1);
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithCustomChars() {
        char[] chars = {'A', 'B', 'C'};
        for (int i = MIN_LENGTH; i <= MAX_LENGTH; i++) {
            String randomString = RandomStringUtils.random(i, 0, chars.length, true, false, chars, TEST_RANDOM);
            assertTrue(randomString.length() == i);
            for (int j = 0; j < randomString.length(); j++) {
                char ch = randomString.charAt(j);
                assertTrue(inArray(ch, chars));
            }
        }
    }

    /**
     * Helper method for testing if an array contains a given element.
     *
     * @param element the element to search for
     * @param array the array to search in
     * @return true if the array contains the element
     */
    private boolean inArray(char element, char[] array) {
        for (char c : array) {
            if (c == element) {
                return true;
            }
        }
        return false;
    }

}