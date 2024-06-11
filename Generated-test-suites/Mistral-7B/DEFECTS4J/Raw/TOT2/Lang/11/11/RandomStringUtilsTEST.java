package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test {@link RandomStringUtils#random(int)}.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    /**
     * Test {@link RandomStringUtils#randomAlphabetic(int)}.
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
     * Test {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test {@link RandomStringUtils#randomNumeric(int)}.
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
     * Test {@link RandomStringUtils#random(int, boolean, boolean)}.
     */
    @Test
    public void testRandomWithOptions() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithCustomCharsAndRandom() {
        Random random = new Random();
        char[] chars = {'a', 'b', 'c'};
        String randomString = RandomStringUtils.random(10, 0, 3, true, false, chars, random);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(randomString.charAt(i) == chars[random.nextInt(3)] || i > 0 && randomString.charAt(i - 1) == (char) 0xdb70);
        }
    }

    /**
     * Test {@link RandomStringUtils#random(int, char[], Random)}.
     */
    @Test
    public void testRandomWithCustomChars() {
        char[] chars = {'a', 'b', 'c'};
        String randomString = RandomStringUtils.random(10, chars, new Random());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(randomString.charAt(i) == chars[new Random().nextInt(chars.length)]);
        }
    }

    /**
     * Test {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     * with empty character array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCustomChars() {
        Random random = new Random();
        RandomStringUtils.random(10, new char[0], random);
    }

    /**
     * Test {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     * with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeLength() {
        Random random = new Random();
        RandomStringUtils.random(-1, 0, 100, true, false, new char[0], random);
    }

    /**
     * Test {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     * with length greater than random character set size.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithLengthGreaterThanSetSize() {
        Random random = new Random();
        char[] chars = {'a', 'b'};
        RandomStringUtils.random(chars.length + 1, 0, 0, true, false, chars, random);
    }

    /**
     * Test {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     * with invalid character set size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidCharacterSetSize() {
        Random random = new Random();
        char[] chars = {'a', 'b'};
        RandomStringUtils.random(0, 0, 1, true, false, chars, random);
    }

    /**
     * Test {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     * with invalid character set boundaries.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidCharacterSetBoundaries() {
        Random random = new Random();
        char[] chars = {'a', 'b'};
        RandomStringUtils.random(1, 2, 0, true, false, chars, random);
        RandomStringUtils.random(1, 0, 1, true, false, chars, random);
    }

    /**
     * Test {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     * with letters and numbers but no valid character in the set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidCharacterSet() {
        Random random = new Random();
        char[] chars = {'a', 'b', (char) 0};
        RandomStringUtils.random(10, 0, chars.length, true, true, chars, random);
    }

    /**
     * Test {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     * with high surrogate followed by low surrogate.
     */
    @Test
    public void testRandomWithHighAndLowSurrogates() {
        Random random = new Random();
        String randomString = RandomStringUtils.random(10, 55296, 57343, true, false, null, random);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            if (i % 2 == 0) {
                assertTrue(randomString.charAt(i) >= 55296 && randomString.charAt(i) <= 56191);
            } else {
                assertTrue(randomString.charAt(i) >= 56320 && randomString.charAt(i) <= 57343);
            }
        }
    }

    /**
     * Test {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     * with high surrogate followed by high surrogate.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithTwoHighSurrogates() {
        Random random = new Random();
        String randomString = RandomStringUtils.random(10, 55296, 55296, true, false, null, random);
    }

    /**
     * Test {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     * with private high surrogates.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithPrivateHighSurrogates() {
        Random random = new Random();
        String randomString = RandomStringUtils.random(10, 56192, 56319, true, false, null, random);
    }
}