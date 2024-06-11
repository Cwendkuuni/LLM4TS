package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test creating a random string.
     */
    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test creating an ASCII random string.
     */
    @Test
    public void testRandomAsciiString() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    /**
     * Test creating a random alphabetic string.
     */
    @Test
    public void testRandomAlphabeticString() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test creating a random alphanumeric string.
     */
    @Test
    public void testRandomAlphanumericString() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test creating a random numeric string.
     */
    @Test
    public void testRandomNumericString() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test creating a random string with specified length, start, end, letters, and numbers.
     */
    @Test
    public void testRandomStringWithOptions() {
        String randomString = RandomStringUtils.random(10, true, true, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test creating a random string with specified length, start, end, letters, and numbers using a custom char array.
     */
    @Test
    public void testRandomStringWithCustomChars() {
        String randomString = RandomStringUtils.random(10, false, false, new char[]{'@', '#', '$', '%', '^', '&', '*', '(', ')', '_'});
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Arrays.asList("@#$%^&*()_".indexOf(c) != -1));
        }
    }

    /**
     * Test creating a random string with specified length and an empty char array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptyChars() {
        RandomStringUtils.random(10, (char[]) null);
    }

    /**
     * Test creating a random string with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNegativeLength() {
        RandomStringUtils.random(-10);
    }
}