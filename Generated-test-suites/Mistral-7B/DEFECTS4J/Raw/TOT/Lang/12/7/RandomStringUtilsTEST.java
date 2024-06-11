package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test random string creation with default options.
     */
    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test random string creation with specific character set and length.
     */
    @Test
    public void testRandomStringWithCustomCharacterSet() {
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(randomString.indexOf(c) >= 0);
            assertTrue("Expected a character in the set [0-9A-Za-z]", Character.isLetterOrDigit(c));
        }
    }

    /**
     * Test random string creation with specific character set, length, and bounds.
     */
    @Test
    public void testRandomStringWithCustomCharacterSetBounds() {
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10, 5, 7);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            int index = randomString.indexOf(c);
            assertTrue("Expected character at index " + (index + 5) + " to be in the set", index >= 5 && index <= 12);
            assertTrue("Expected a character in the set [0-9A-Za-z]", Character.isLetterOrDigit(c));
        }
    }

    /**
     * Test random string creation with specific character set, length, bounds, and alphanumeric flag.
     */
    @Test
    public void testRandomStringWithCustomCharacterSetBoundsAlphanumeric() {
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10, 5, 7, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            int index = randomString.indexOf(c);
            assertTrue("Expected character at index " + (index + 5) + " to be in the set", index >= 5 && index <= 12);
            assertTrue("Expected an alphanumeric character", Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test random string creation with specific character set, length, bounds, and alphanumeric flag (empty character set).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptyCharacterSet() {
        RandomStringUtils.random("", 10, 5, 7, true, true);
    }

    /**
     * Test random string creation with specific character set, length, bounds, and alphanumeric flag (null character set).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNullCharacterSet() {
        RandomStringUtils.random(null, 10, 5, 7, true, true);
    }

    /**
     * Test random string creation with specific character set, length, and alphanumeric flag (negative length).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNegativeLength() {
        RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", -5, 5, 7, true, true);
    }

    /**
     * Test random string creation with specific character set, length, and alphanumeric flag (negative start index).
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRandomStringWithNegativeStartIndex() {
        String characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        RandomStringUtils.random(characterSet, -5, 5, 7, true, true);
    }

    /**
     * Test random string creation with specific character set, length, and alphanumeric flag (end index out of bounds).
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRandomStringWithEndIndexOutOfBounds() {
        String characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        RandomStringUtils.random(characterSet, 10, characterSet.length(), 7, true, true);
    }
}