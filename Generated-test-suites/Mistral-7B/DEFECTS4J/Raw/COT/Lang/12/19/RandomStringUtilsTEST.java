package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test suite for the {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    /** Test random string creation with default settings. */
    @Test
    public void testRandomDefault() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
    }

    /** Test random string creation with specified length and character set. */
    @Test
    public void testRandomWithCharacterSet() {
        String randomString = RandomStringUtils.random("abcdef", 10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '_');
            assertTrue(Character.isAlphabetic(c) || c == '_' || Character.isDigit(c)
                    || 'a' <= c && c <= 'f');
        }
    }

    /** Test random string creation with specified length and character set boundaries. */
    @Test
    public void testRandomWithCharacterSetBoundaries() {
        String randomString = RandomStringUtils.random("abc", 10, 3, 5);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue('a' <= c && c <= 'z' || 'A' <= c && c <= 'Z' || '0' <= c && c <= '9');
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /** Test random string creation with specified length and character set excluding certain characters. */
    @Test
    public void testRandomExclude() {
        String randomString = RandomStringUtils.random("abcdefgijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_", 10, 3, 5, false, '_');
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertFalse(c == '_');
            assertTrue('a' <= c && c <= 'z' || 'A' <= c && c <= 'Z' || '0' <= c && c <= '9');
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /** Test random string creation with empty character set. */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharacterSet() {
        RandomStringUtils.random("", 10);
    }

    /** Test random string creation with negative length. */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(10, "", 0, 0, false, false, new Random());
    }

    /** Test random string creation with null character set. */
    @Test
    public void testRandomNullCharacterSet() {
        String randomString = RandomStringUtils.random(10, null);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '_');
        }
    }

    /** Test random string creation with null random instance. */
    @Test
    public void testRandomNullRandom() {
        String randomString = RandomStringUtils.random(10, 0, 0, false, false, null);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
    }

    /** Test random string creation with length zero. */
    @Test
    public void testRandomLengthZero() {
        String randomString = RandomStringUtils.random(0);
        assertEquals("", randomString);
    }

    /** Test random string creation with length one. */
    @Test
    public void testRandomLengthOne() {
        String randomString = RandomStringUtils.random(1);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 1);
        char randomChar = randomString.charAt(0);
        assertTrue(Character.isAlphabetic(randomChar) || Character.isDigit(randomChar)
                || randomChar == '_');
        if (randomChar == '_') {
            assertTrue('a' <= RandomStringUtils.random('a').charAt(0) && RandomStringUtils.random('a').charAt(0) <= 'z'
                    || 'A' <= RandomStringUtils.random('A').charAt(0) && RandomStringUtils.random('A').charAt(0) <= 'Z'
                    || '0' <= RandomStringUtils.random('0').charAt(0) && RandomStringUtils.random('0').charAt(0) <= '9');
        }
    }

    /** Test random string creation with length two. */
    @Test
    public void testRandomLengthTwo() {
        String randomString = RandomStringUtils.random(2);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 2);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '_');
        }
    }

    /** Test random string creation with random range including start and end. */
    @Test
    public void testRandomRangeInclusive() {
        String randomString = RandomStringUtils.random("abcd", 10, 0, 3);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue('a' <= c && c <= 'd');
        }
    }

    /** Test random string creation with random range excluding start and end. */
    @Test
    public void testRandomRangeExclusive() {
        String randomString = RandomStringUtils.random("abcd", 10, 0, 4);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertFalse('a' <= c && c <= 'c');
            assertTrue('d' <= c || c == '_');
        }
    }

    /** Test random string creation with random range and empty character set. */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomRangeEmptyCharacterSet() {
        RandomStringUtils.random("", 10, 0, 0);
    }

    /** Test random string creation with random range and negative length. */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomRangeNegativeLength() {
        RandomStringUtils.random("abcd", 10, 0, 5, new Random());
        RandomStringUtils.random("abcd", -1, 0, 5, new Random());
    }

    /** Test random string creation with random range and null character set. */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomRangeNullCharacterSet() {
        RandomStringUtils.random("", 10, 0, 5, new Random());
    }

    /** Test random string creation with random range and null random instance. */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomRangeNullRandom() {
        RandomStringUtils.random("abcd", 10, 0, 5, null);
    }

    /** Test random string creation with random range and length zero. */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomRangeLengthZero() {
        RandomStringUtils.random("abcd", 0, 0, 5, new Random());
    }

    /** Test random string creation with random range and length one. */
    @Test
    public void testRandomRangeLengthOne() {
        String randomString1 = RandomStringUtils.random("abcd", 1, 0, 3, new Random());
        String randomString2 = RandomStringUtils.random("abcd", 1, 0, 3, new Random());
        assertNotNull(randomString1);
        assertNotNull(randomString2);
        assertEquals(randomString1, randomString2);
        assertEquals(randomString1.length(), 1);
        char randomChar = randomString1.charAt(0);
        assertTrue('a' <= randomChar && randomChar <= 'c');
    }

    /** Test random string creation with random range and length two. */
    @Test
    public void testRandomRangeLengthTwo() {
        String randomString1 = RandomStringUtils.random("abcd", 2, 0, 3, new Random());
        String randomString2 = RandomStringUtils.random("abcd", 2, 0, 3, new Random());
        assertNotNull(randomString1);
        assertNotNull(randomString2);
        assertEquals(randomString1, randomString2);
        assertEquals(randomString1.length(), 2);
        for (int i = 0; i < 2; i++) {
            char randomChar = randomString1.charAt(i);
            assertTrue('a' <= randomChar && randomChar <= 'd');
        }
    }

    /** Test random string creation with random range and length three. */
    @Test
    public void testRandomRangeLengthThree() {
        String randomString1 = RandomStringUtils.random("abcd", 3, 0, 3, new Random());
        String randomString2 = RandomStringUtils.random("abcd", 3, 0, 3, new Random());
        assertNotNull(randomString1);
        assertNotNull(randomString2);
        assertEquals(randomString1, randomString2);
        assertEquals(randomString1.length(), 3);
        for (int i = 0; i < 3; i++) {
            char randomChar = randomString1.charAt(i);
            assertTrue('a' <= randomChar && randomChar <= 'd');
        }
    }

    /** Test random string creation with random range and length four. */
    @Test
    public void testRandomRangeLengthFour() {
        String randomString1 = RandomStringUtils.random("abcd", 4, 0, 3, new Random());
        String randomString2 = RandomStringUtils.random("abcd", 4, 0, 3, new Random());
        assertNotNull(randomString1);
        assertNotNull(randomString2);
        assertEquals(randomString1, randomString2);
        assertEquals(randomString1.length(), 4);
        for (int i = 0; i < 4; i++) {
            char randomChar = randomString1.charAt(i);
            assertTrue('a' <= randomChar && randomChar <= 'd');
        }
    }

    /** Test random string creation with random range and length five. */
    @Test
    public void testRandomRangeLengthFive() {
        String randomString1 = RandomStringUtils.random("abcd", 5, 0, 3, new Random());
        String randomString2 = RandomStringUtils.random("abcd", 5, 0, 3, new Random());
        assertNotNull(randomString1);
        assertNotNull(randomString2);
        assertEquals(randomString1, randomString2);
        assertEquals(randomString1.length(), 5);
        for (int i = 0; i < 5; i++) {
            char randomChar = randomString1.charAt(i);
            assertTrue('a' <= randomChar && randomChar <= 'd');
        }
    }

    /** Test random string creation with random range and length greater than character set size. */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomRangeLengthGreaterThanCharacterSetSize() {
        RandomStringUtils.random("abcd", 6, 0, 3, new Random());
    }

    /** Test random string creation with random range and length equal to character set size. */
    @Test
    public void testRandomRangeLengthEqualToCharacterSetSize() {
        String randomString = RandomStringUtils.random("abcd", 4, 0, 4, new Random());
        assertNotNull(randomString);
        assertEquals(randomString.length(), 4);
        for (int i = 0; i < 4; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue('a' <= randomChar && randomChar <= 'd');
        }
    }
}