package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test cases for the RandomStringUtils class.
 */
public class RandomStringUtilsTest {

    /**
     * Test random string creation with default settings.
     */
    @Test
    public void testRandomStringCreation() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test random string creation with Ascii characters.
     */
    @Test
    public void testRandomAsciiStringCreation() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(c >= 32 && c <= 126);
        }
    }

    /**
     * Test random string creation with alphabetic characters.
     */
    @Test
    public void testRandomAlphabeticStringCreation() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    /**
     * Test random string creation with alphanumeric characters.
     */
    @Test
    public void testRandomAlphanumericStringCreation() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test random string creation with numeric characters.
     */
    @Test
    public void testRandomNumericStringCreation() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    /**
     * Test random string creation with custom character set and length.
     */
    @Test
    public void testRandomCustomCharacterSetStringCreation() {
        String customChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomString = RandomStringUtils.random(10, customChars.toCharArray());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(customChars.indexOf(c) != -1);
        }
    }

    /**
     * Test random string creation with custom character set, start and end positions, and length.
     */
    @Test
    public void testRandomCustomCharacterSetPositionStringCreation() {
        String customChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String randomString = RandomStringUtils.random(10, customChars.toCharArray(), 0, customChars.length());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(customChars.indexOf(c) != -1);
        }
    }

    /**
     * Test random string creation with custom character set, start and end positions, length, and letters and numbers flags.
     */
    @Test
    public void testRandomCustomCharacterSetPositionLettersNumbersStringCreation() {
        String customChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(10, customChars.toCharArray(), 0, customChars.length(), true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isLetter(c) || Character.isDigit(c) || customChars.indexOf(c) != -1);
        }
    }

    /**
     * Test exception when count is less than 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringCreationNegativeCount() {
        RandomStringUtils.random(-1);
    }

    /**
     * Test exception when chars array is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringCreationEmptyChars() {
        RandomStringUtils.random(1, new char[0]);
    }
}