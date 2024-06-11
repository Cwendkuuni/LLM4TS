package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test method for {@link RandomStringUtils#random(int)}.
     */
    @Test
    public void testRandom() {
        // Test random string with default character set
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }

        // Test random string with specified character set (ASCII)
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomAsciiString.charAt(i)) || Character.isDigit(randomAsciiString.charAt(i)) || Character.isWhitespace(randomAsciiString.charAt(i)) || Character.isISOControl(randomAsciiString.charAt(i)));
        }

        // Test random string with specified character set (alphabetic)
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomAlphabeticString.charAt(i)));
        }

        // Test random string with specified character set (alphanumeric)
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomAlphanumericString.charAt(i)) || Character.isDigit(randomAlphanumericString.charAt(i)));
        }

        // Test random string with specified character set (numeric)
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            assertTrue(Character.isDigit(randomNumericString.charAt(i)));
        }

        // Test random string with specified character set and range
        String randomStringWithRange = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789", 10);
        assertNotNull(randomStringWithRange);
        assertEquals(10, randomStringWithRange.length());
        for (int i = 0; i < randomStringWithRange.length(); i++) {
            assertTrue(randomStringWithRange.charAt(i) >= 'A' && randomStringWithRange.charAt(i) <= 'Z' ||
                      randomStringWithRange.charAt(i) >= 'a' && randomStringWithRange.charAt(i) <= 'z' ||
                      randomStringWithRange.charAt(i) >= '0' && randomStringWithRange.charAt(i) <= '9');
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, boolean, boolean)}.
     */
    @Test
    public void testRandomWithFlags() {
        // Test random string with only letters
        String randomStringWithOnlyLetters = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomStringWithOnlyLetters);
        assertEquals(10, randomStringWithOnlyLetters.length());
        for (int i = 0; i < randomStringWithOnlyLetters.length(); i++) {
            assertTrue(Character.isAlphabetic(randomStringWithOnlyLetters.charAt(i)));
        }

        // Test random string with only numeric characters
        String randomStringWithOnlyNumbers = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomStringWithOnlyNumbers);
        assertEquals(10, randomStringWithOnlyNumbers.length());
        for (int i = 0; i < randomStringWithOnlyNumbers.length(); i++) {
            assertTrue(Character.isDigit(randomStringWithOnlyNumbers.charAt(i)));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithCustomCharacterSet() {
        // Test random string with custom character set
        char[] customCharacterSet = {'A', 'B', 'C', 'D', 'E', 'F'};
        String randomStringWithCustomCharacterSet = RandomStringUtils.random(5, customCharacterSet, new Random());
        assertNotNull(randomStringWithCustomCharacterSet);
        assertEquals(5, randomStringWithCustomCharacterSet.length());
        for (int i = 0; i < randomStringWithCustomCharacterSet.length(); i++) {
            assertTrue(Character.isLetter(randomStringWithCustomCharacterSet.charAt(i)) || Arrays.asList(customCharacterSet).contains(randomStringWithCustomCharacterSet.charAt(i)));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     * with negative count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() {
        char[] customCharacterSet = {'A', 'B', 'C', 'D', 'E', 'F'};
        RandomStringUtils.random(-1, 0, customCharacterSet.length, true, true, customCharacterSet, new Random());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     * with empty character array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharacterArray() {
        RandomStringUtils.random(1, 0, 0, true, true, null, new Random());
    }
}