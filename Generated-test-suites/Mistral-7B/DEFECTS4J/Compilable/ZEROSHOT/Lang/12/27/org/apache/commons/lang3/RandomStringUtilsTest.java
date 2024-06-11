package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;

import org.junit.Test;

/**
 * JUnit test file for RandomStringUtils class
 */
public class RandomStringUtilsTest {

    /**
     * Test for random method with default character set
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for randomAscii method with default character set
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            int asciiValue = (int) randomAsciiString.charAt(i);
            assertEquals(asciiValue >= 32 && asciiValue <= 126, true);
        }
    }

    /**
     * Test for randomAlphabetic method with default character set
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            char character = randomAlphabeticString.charAt(i);
            assertEquals(Character.isAlphabetic(character), true);
        }
    }

    /**
     * Test for randomAlphanumeric method with default character set
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char character = randomAlphanumericString.charAt(i);
            assertEquals(Character.isAlphabetic(character) || Character.isDigit(character), true);
        }
    }

    /**
     * Test for randomNumeric method with default character set
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            char character = randomNumericString.charAt(i);
            assertEquals(Character.isDigit(character), true);
        }
    }

    /**
     * Test for random method with specified character set
     */
    @Test
    public void testRandomCustomCharacterSet() {
        String customCharacterSet = "abcd1234ABCD5678";
        String randomString = RandomStringUtils.random(10, customCharacterSet.toCharArray());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char character = randomString.charAt(i);
            assertEquals(customCharacterSet.indexOf(character) != -1, true);
        }
    }
}