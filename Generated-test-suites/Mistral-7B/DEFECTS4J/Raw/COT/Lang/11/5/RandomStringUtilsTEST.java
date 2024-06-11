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
        int count = 5;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length should match", count, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        int count = 5;
        String randomString = RandomStringUtils.randomAscii(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            int asciiValue = (int) randomString.charAt(i);
            assertTrue("All characters should be in the range 32-126", asciiValue >= 32 && asciiValue <= 126);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}.
     */
    @Test
    public void testRandomAlphabetic() {
        int count = 5;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("All characters should be alphabetic", Character.isAlphabetic(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        int count = 5;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            if (Character.isAlphabetic(c)) {
                assertTrue("Alphabetic characters should only appear if alphabetic is true", Character.isAlphabetic(c));
            } else if (Character.isDigit(c)) {
                assertTrue("Numeric characters should only appear if numeric is true", Character.isDigit(c));
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}.
     */
    @Test
    public void testRandomNumeric() {
        int count = 5;
        String randomString = RandomStringUtils.randomNumeric(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("All characters should be numeric", Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean)}.
     */
    @Test
    public void testRandomWithCharRangeAndFlags() {
        int count = 5;
        int start = 33;
        int end = 126;
        boolean includeLetters = true;
        boolean includeNumbers = false;
        String randomString = RandomStringUtils.random(count, start, end, includeLetters, includeNumbers);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            if (includeLetters && Character.isAlphabetic(c)) {
                assertTrue("Alphabetic characters should only appear if alphabetic is true", Character.isAlphabetic(c));
            } else if (includeNumbers && Character.isDigit(c)) {
                assertTrue("Numeric characters should only appear if numeric is true", Character.isDigit(c));
            } else {
                int asciiValue = (int) c;
                assertTrue("All other characters should be in the range " + start + "-" + end, asciiValue >= start && asciiValue <= end);
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithCustomCharSetAndRandom() {
        int count = 5;
        int start = 65;
        int end = 90;
        boolean includeLetters = true;
        boolean includeNumbers = false;
        char[] characterSet = new char[] {'A', 'B', 'C'};
        Random random = new Random();
        String randomString = RandomStringUtils.random(count, start, end, includeLetters, includeNumbers, characterSet, random);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            if (includeLetters && Character.isAlphabetic(c)) {
                assertTrue("Alphabetic characters should only appear if alphabetic is true", Character.isAlphabetic(c));
                assertTrue("Alphabetic characters should only come from the character set", Arrays.asList(characterSet).contains(c));
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, String)}.
     */
    @Test
    public void testRandomWithStringCharSet() {
        int count = 5;
        String characterSet = "ABC";
        String randomString = RandomStringUtils.random(count, characterSet);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("All characters should be in the character set", characterSet.indexOf(c) != -1);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, char[])}.
     */
    @Test
    public void testRandomWithCharArrayCharSet() {
        int count = 5;
        char[] characterSet = new char[] {'A', 'B', 'C'};
        String randomString = RandomStringUtils.random(count, characterSet);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("All characters should be in the character set", Arrays.asList(characterSet).contains(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        int count = -5;
        RandomStringUtils.random(count);
    }

    /**
     * Test method for {@link RandomStringUtils#random(int)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterSet() {
        int count = 5;
        String characterSet = "";
        RandomStringUtils.random(count, characterSet);
    }
}