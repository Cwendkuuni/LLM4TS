package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test method for {@link RandomStringUtils#random(int)}.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(c >= 32 && c <= 126);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue((Character.isAlphabetic(c) || Character.isDigit(c)));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}.
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean)}.
     */
    @Test
    public void testRandomWithStartAndEnd() {
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyz", 5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(c));
            assertTrue(c >= 'a' && c <= 'z');
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithCustomCharacterSetAndRandom() {
        char[] customCharacterSet = {'a', 'b', 'c', 'd'};
        String randomString = RandomStringUtils.random(5, customCharacterSet, new Random(42));
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(containsChar(customCharacterSet, c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     * with an empty character set.
     *
     * @expected: java.lang.IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterSet() {
        String randomString = RandomStringUtils.random(5, new char[0], new Random(42));
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)}
     * with a negative length.
     *
     * @expected: java.lang.IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeLength() {
        String randomString = RandomStringUtils.random(-5, new char[0], new Random(42));
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)}
     * with a start position greater than the end position.
     *
     * @expected: java.lang.IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidStartAndEndPositions() {
        char[] customCharacterSet = {'a', 'b', 'c'};
        String randomString = RandomStringUtils.random(5, customCharacterSet[0], customCharacterSet[2], true, true, customCharacterSet, new Random(42));
    }

    /**
     * Helper method for checking if a character is contained in a character array.
     */
    private boolean containsChar(char[] charArray, char character) {
        for (char c : charArray) {
            if (c == character) {
                return true;
            }
        }
        return false;
    }
}