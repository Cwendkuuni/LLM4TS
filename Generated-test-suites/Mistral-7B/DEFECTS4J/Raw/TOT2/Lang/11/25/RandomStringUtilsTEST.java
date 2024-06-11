package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test the random method with default parameters.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test the randomAscii method with default parameters.
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isASCII(c));
        }
    }

    /**
     * Test the randomAlphabetic method with default parameters.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test the randomAlphanumeric method with default parameters.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    /**
     * Test the randomNumeric method with default parameters.
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test the random method with custom parameters.
     */
    @Test
    public void testRandomCustom() {
        String randomString = RandomStringUtils.random(5, 'a', 'z');
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
            assertTrue(c >= 'a' && c <= 'z');
        }
    }

    /**
     * Test the random method with negative count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    /**
     * Test the randomAlphanumeric method with empty char array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphanumericEmptyChars() {
        RandomStringUtils.randomAlphanumeric(0, (char[]) null);
    }

    /**
     * Test the random method with custom char array and invalid range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomInvalidRange() {
        char[] chars = {'a', 'z', 'A', 'Z'};
        RandomStringUtils.random(5, chars, 0, 0, true);
    }

    /**
     * Test the random method with custom char array and valid range.
     */
    @Test
    public void testRandomCustomChars() {
        char[] chars = {'a', 'z', 'A', 'Z'};
        String randomString = RandomStringUtils.random(5, chars, 0, chars.length, true);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    /**
     * Test the random method with custom char array and empty range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyRange() {
        char[] chars = {'a', 'z'};
        RandomStringUtils.random(5, chars, 0, 0, false);
    }

    /**
     * Test the random method with custom char array and both letters and numbers.
     */
    @Test
    public void testRandomCustomCharsLetterAndNumber() {
        char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
                'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        String randomString = RandomStringUtils.random(5, chars, 0, chars.length, true, true);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
}