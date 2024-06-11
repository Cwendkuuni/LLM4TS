package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test creating random strings.
     */
    @Test
    public void testRandomStrings() {
        // Test empty string
        assertEquals("", RandomStringUtils.random(0));
        // Test creating a random string of a specific length
        assertEquals("abcdefghijk", RandomStringUtils.randomAlphanumeric(10));
        // Test creating a random string of a specific length with letters only
        assertEquals("abcdefghij", RandomStringUtils.randomAlphabetic(10));
        // Test creating a random string of a specific length with numbers only
        assertEquals("1234567890", RandomStringUtils.randomNumeric(10));
        // Test creating a random string of a specific length with custom character set
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        assertEquals(chars, RandomStringUtils.random(10, chars));
        // Test creating a random string of a specific length with custom character set and limit
        String limitChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        assertEquals(limitChars, RandomStringUtils.random(10, limitChars, 0, limitChars.length()));
    }

    /**
     * Test creating random strings with errors.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringsErrors() {
        // Test negative length
        RandomStringUtils.random(-1);
        // Test null character set
        RandomStringUtils.random(10, null);
        // Test empty character set
        RandomStringUtils.random(10, "");
    }

    /**
     * Test creating random strings with specific characters.
     */
    @Test
    public void testRandomStringsWithSpecificCharacters() {
        // Test creating a random string with a specific character set and no limit
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        assertEquals(chars, RandomStringUtils.random(100, chars, 0, chars.length()));
        // Test creating a random string with a specific character set and limit
        String limitChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        assertEquals(limitChars, RandomStringUtils.random(100, limitChars, 0, limitChars.length()));
        // Test creating a random string with a specified start and end position in the character set
        String subChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        assertEquals(subChars, RandomStringUtils.random(100, subChars.charAt(0), subChars.charAt(subChars.length())));
    }
}