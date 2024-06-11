package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    // Test random method
    //-----------------------------------------------------------------------

    /**
     * Test random string creation with length 0.
     */
    @Test
    public void testRandomStringLengthZero() {
        String result = RandomStringUtils.random(0);
        assertNull(result);
    }

    /**
     * Test random string creation with length less than 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringLengthNegative() {
        RandomStringUtils.random(-1);
    }

    /**
     * Test random string creation with length equal to 1.
     */
    @Test
    public void testRandomStringLengthOne() {
        String result = RandomStringUtils.random(1);
        assertEquals(1, result.length());
    }

    /**
     * Test random string creation with length greater than 0.
     */
    @Test
    public void testRandomStringLengthGreaterThanOne() {
        String result = RandomStringUtils.random(5);
        assertEquals(5, result.length());
    }

    /**
     * Test random string creation with length equals to Integer.MAX_VALUE.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringLengthMaxValue() {
        RandomStringUtils.random(Integer.MAX_VALUE);
    }

    /**
     * Test random string creation with all characters.
     */
    @Test
    public void testRandomStringAllCharacters() {
        String result = RandomStringUtils.random(10);
        assertEquals(10, result.length());
    }

    /**
     * Test random string creation with ASCII characters only.
     */
    @Test
    public void testRandomStringAscii() {
        String result = RandomStringUtils.randomAscii(10);
        assertEquals(10, result.length());
    }

    /**
     * Test random string creation with alphabetic characters only.
     */
    @Test
    public void testRandomStringAlphabetic() {
        String result = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, result.length());
    }

    /**
     * Test random string creation with alphanumeric characters only.
     */
    @Test
    public void testRandomStringAlphanumeric() {
        String result = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, result.length());
    }

    /**
     * Test random string creation with numeric characters only.
     */
    @Test
    public void testRandomStringNumeric() {
        String result = RandomStringUtils.randomNumeric(10);
        assertEquals(10, result.length());
    }

    /**
     * Test random string creation with a custom character set.
     */
    @Test
    public void testRandomStringCustomCharacterSet() {
        String expected = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String result = RandomStringUtils.random(expected.length(), expected.toCharArray());
        assertEquals(expected, result);
    }

    // Test random method with range
    //-----------------------------------------------------------------------

    /**
     * Test random string creation within a range with letters and numbers.
     */
    @Test
    public void testRandomStringRangeLettersAndNumbers() {
        String result = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 5);
        assertEquals(5, result.length());
    }

    /**
     * Test random string creation within a range with only letters.
     */
    @Test
    public void testRandomStringRangeOnlyLetters() {
        String result = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 5);
        assertEquals(5, result.length());
    }

    /**
     * Test random string creation within a range with only numbers.
     */
    @Test
    public void testRandomStringRangeOnlyNumbers() {
        String result = RandomStringUtils.random("0123456789", 5);
        assertEquals(5, result.length());
    }

    /**
     * Test random string creation within a range with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringRangeEmptyCharacterSet() {
        RandomStringUtils.random("", 5);
    }

    /**
     * Test random string creation within a range with a negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringRangeNegativeLength() {
        RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", -1);
    }

    // Test random method with custom character set and custom random
    //-----------------------------------------------------------------------

    /**
     * Test random string creation with a custom character set and custom random.
     */
    @Test
    public void testRandomStringCustomCharacterSetCustomRandom() {
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        char[] chars = expected.toCharArray();
        String result = RandomStringUtils.random(chars, 5, new Random(1));
        assertEquals(expected, result);
    }

    // Edge cases and exception handling tests
    //-----------------------------------------------------------------------

    /**
     * Test random string creation with a null character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringNullCharacterSet() {
        RandomStringUtils.random(null, 5);
    }

    /**
     * Test random string creation with a character set containing only one
     * character.
     */
    @Test
    public void testRandomStringCharacterSetSingleCharacter() {
        String result = RandomStringUtils.random("a", 5);
        assertEquals(5, result.length());
    }

    /**
     * Test random string creation with a character set containing high surrogates.
     */
    @Test
    public void testRandomStringCharacterSetHighSurrogates() {
        String result = RandomStringUtils.random("👋🏻", 5);
        assertEquals(5, result.length());
    }

    /**
     * Test random string creation with a character set containing low surrogates.
     */
    @Test
    public void testRandomStringCharacterSetLowSurrogates() {
        String result = RandomStringUtils.random("👋", 5);
        assertEquals(5, result.length());
    }

    /**
     * Test random string creation with a character set containing private high surrogates.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testRandomStringCharacterSetPrivateHighSurrogates() {
        String result = RandomStringUtils.random("😐", 5);
    }

    /**
     * Test random string creation with a character set containing invalid characters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringCharacterSetInvalidCharacters() {
        String result = RandomStringUtils.random("\u0000", 5);
    }
}