package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang3.RandomStringUtils;
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
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)} .
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomAsciiString);
        assertEquals(5, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(Character.isISOControl(c) || Character.isAlphabetic(c) || Character.isDigit(c) || Character.isWhitespace(c) || Character.isGraphicsSymbol(c) || Character.isUpperCase(c) || Character.isLowerCase(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)} .
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomAlphabeticString);
        assertEquals(5, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)} .
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomAlphanumericString);
        assertEquals(5, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)} .
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomNumericString);
        assertEquals(5, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, boolean, boolean)} .
     */
    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomStringWithLettersAndNumbers = RandomStringUtils.random(5, true, true);
        assertNotNull(randomStringWithLettersAndNumbers);
        assertEquals(5, randomStringWithLettersAndNumbers.length());
        for (char c : randomStringWithLettersAndNumbers.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} .
     */
    @Test
    public void testRandomWithCustomCharSetAndRandom() {
        String customCharSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        String randomStringWithCustomCharSet = RandomStringUtils.random(5, customCharSet.toCharArray(), random);
        assertNotNull(randomStringWithCustomCharSet);
        assertEquals(5, randomStringWithCustomCharSet.length());
        for (char c : randomStringWithCustomCharSet.toCharArray()) {
            assertTrue(customCharSet.indexOf(c) >= 0);
        }
    }
}