package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test cases for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test random string creation.
     */
    @Test
    public void testRandomStringCreation() {
        // Test creating a random string with default options
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        // Test creating a random string with specified options
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
    }

    /**
     * Test random string creation with custom character set.
     */
    @Test
    public void testRandomStringCreationWithCustomCharacterSet() {
        String customCharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomString = RandomStringUtils.random(10, customCharacterSet);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue(customCharacterSet.indexOf(randomChar) != -1);
        }
    }

    /**
     * Test random string creation with specified length and character set boundaries.
     */
    @Test
    public void testRandomStringCreationWithSpecifiedLengthAndCharacterSetBoundaries() {
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomString = RandomStringUtils.random(10, 0, characterSet.length(), false, false, null, RANDOM);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue(characterSet.indexOf(randomChar) != -1);
        }
    }

    /**
     * Test creating a random string with specified options and range of characters.
     */
    @Test
    public void testRandomStringCreationWithSpecifiedOptionsAndCharacterRange() {
        String customCharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomString = RandomStringUtils.random(10, 0, customCharacterSet.length(), true, true, customCharacterSet.toCharArray(), RANDOM);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < 10; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue(customCharacterSet.indexOf(randomChar) != -1);
            if (Character.isLetter(randomChar)) {
                assertTrue(randomChar >= 'A' && randomChar <= 'Z' || randomChar >= 'a' && randomChar <= 'z');
            } else {
                assertTrue(Character.isDigit(randomChar) && (randomChar >= '0' && randomChar <= '9'));
            }
        }
    }

    /**
     * Test creating a random string with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringCreationWithEmptyCharacterSet() {
        RandomStringUtils.random(10, new char[0]);
    }

    /**
     * Test creating a random string with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringCreationWithNegativeLength() {
        RandomStringUtils.random(-1);
    }
}