package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Tests for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    /**
     * Test for {@link RandomStringUtils#random(int)} method.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for {@link RandomStringUtils#randomAscii(int)} method.
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            int randomChar = randomAsciiString.charAt(i);
            assertTrue(Character.isISOControl(randomChar) || Character.isDigit(randomChar) || Character.isLetter(randomChar) || Character.isWhitespace(randomChar) || Character.isHighSurrogate(randomChar) || Character.isLowSurrogate(randomChar));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphabetic(int)} method.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            assertEquals(Character.isLetter(randomAlphabeticString.charAt(i)), true);
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphanumeric(int)} method.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            int randomChar = randomAlphanumericString.charAt(i);
            assertTrue(Character.isAlphabetic(randomChar) || Character.isDigit(randomChar));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomNumeric(int)} method.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            assertEquals(Character.isDigit(randomNumericString.charAt(i)), true);
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean)} method.
     */
    @Test
    public void testRandomWithOptions() {
        String randomString = RandomStringUtils.random(5, 1, 12, true, true);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            int randomChar = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(randomChar) || Character.isDigit(randomChar));
        }

        String randomStringWithOnlyLetters = RandomStringUtils.random(5, 1, 12, true, false);
        assertNotNull(randomStringWithOnlyLetters);
        assertEquals(5, randomStringWithOnlyLetters.length());
        for (int i = 0; i < randomStringWithOnlyLetters.length(); i++) {
            assertEquals(Character.isLetter(randomStringWithOnlyLetters.charAt(i)), true);
        }

        String randomStringWithOnlyNumbers = RandomStringUtils.random(5, 1, 12, false, false);
        assertNotNull(randomStringWithOnlyNumbers);
        assertEquals(5, randomStringWithOnlyNumbers.length());
        for (int i = 0; i < randomStringWithOnlyNumbers.length(); i++) {
            assertEquals(Character.isDigit(randomStringWithOnlyNumbers.charAt(i)), true);
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method.
     */
    @Test
    public void testRandomWithCustomCharacterSet() {
        String customCharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomStringWithCustomCharacterSet = RandomStringUtils.random(5, 0, customCharacterSet.length(), true, true, customCharacterSet.toCharArray(), new Random());
        assertNotNull(randomStringWithCustomCharacterSet);
        assertEquals(5, randomStringWithCustomCharacterSet.length());
        for (int i = 0; i < randomStringWithCustomCharacterSet.length(); i++) {
            char randomChar = randomStringWithCustomCharacterSet.charAt(i);
            assertTrue(customCharacterSet.indexOf(randomChar) != -1);
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method
     * with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterSet() {
        String emptyCharacterSet = "";
        RandomStringUtils.random(5, 0, emptyCharacterSet.length(), true, true, emptyCharacterSet.toCharArray(), new Random());
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)} method
     * with negative count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1, 0, true, true, null, new Random());
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)} method
     * with invalid start and end indices.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithInvalidCharacterSetIndices() {
        String customCharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        RandomStringUtils.random(0, customCharacterSet.length(), true, true, customCharacterSet.toCharArray(), new Random());
    }

    /**
     * Test for {@link RandomStringUtils#random(int)} method with private high surrogates.
     */
    @Test
    public void testRandomWithPrivateHighSurrogates() {
        String randomString = RandomStringUtils.random(10, new Random());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        int numberOfPrivateHighSurrogates = 0;
        for (int i = 0; i < randomString.length(); i++) {
            int randomChar = randomString.charAt(i);
            if (Character.isHighSurrogate(randomChar)) {
                numberOfPrivateHighSurrogates++;
            }
        }
        assertTrue(numberOfPrivateHighSurrogates > 0);
    }
}