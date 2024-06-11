package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test for RandomStringUtils.random(int count) method.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for RandomStringUtils.randomAscii(int count) method.
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            char currentChar = randomAsciiString.charAt(i);
            assertTrue(Character.isASCII(currentChar));
        }
    }

    /**
     * Test for RandomStringUtils.randomAlphabetic(int count) method.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            char currentChar = randomAlphabeticString.charAt(i);
            assertTrue(Character.isAlphabetic(currentChar));
        }
    }

    /**
     * Test for RandomStringUtils.randomAlphanumeric(int count) method.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char currentChar = randomAlphanumericString.charAt(i);
            assertTrue(Character.isAlphabetic(currentChar) || Character.isDigit(currentChar));
        }
    }

    /**
     * Test for RandomStringUtils.randomNumeric(int count) method.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            char currentChar = randomNumericString.charAt(i);
            assertTrue(Character.isDigit(currentChar));
        }
    }

    /**
     * Test for RandomStringUtils.random(int count, int start, int end, boolean letters, boolean numbers) method.
     */
    @Test
    public void testRandomWithRange() {
        String randomString = RandomStringUtils.random(10, 65, 90, true, false);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char currentChar = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(currentChar));
            assertTrue(currentChar >= 'A' && currentChar <= 'Z');
        }
    }

    /**
     * Test for RandomStringUtils.random(int count, char[] chars) method.
     */
    @Test
    public void testRandomWithCustomChars() {
        String randomString = RandomStringUtils.random(10, new char[]{'a', 'b', 'c'});
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char currentChar = randomString.charAt(i);
            assertTrue(new String(new char[]{currentChar}).contains("ab"));
        }
    }

    /**
     * Test for RandomStringUtils.random(int count, String chars) method.
     */
    @Test
    public void testRandomWithCustomCharsString() {
        String randomString = RandomStringUtils.random(10, "abc");
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char currentChar = randomString.charAt(i);
            assertTrue(new String(new char[]{currentChar}).contains("ab"));
        }
    }

    /**
     * Test for edge case: generating an empty string with RandomStringUtils.random(int count).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyString() {
        RandomStringUtils.random(-1);
    }

    /**
     * Test for edge case: generating a negative length string with RandomStringUtils.random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLengthString() {
        RandomStringUtils.random(-1, 0, 0, false, false, null, null);
    }

    /**
     * Test for edge case: generating a string with range out of bounds with RandomStringUtils.random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random).
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomOutOfBounds() {
        RandomStringUtils.random(1, 1, 0, false, false, new char[0], null);
    }
}