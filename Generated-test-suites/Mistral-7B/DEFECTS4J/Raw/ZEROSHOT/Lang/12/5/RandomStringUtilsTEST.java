package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Random;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test for random method with default character set.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for randomAscii method with default character set.
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            int randomChar = randomString.charAt(i);
            assertEquals(32, (char) Math.min(randomChar, 126));
        }
    }

    /**
     * Test for randomAlphabetic method with default character set.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char randomChar = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(randomChar));
        }
    }

    /**
     * Test for randomAlphanumeric method with default character set.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char randomChar = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(randomChar) || Character.isDigit(randomChar));
        }
    }

    /**
     * Test for randomNumeric method with default character set.
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char randomChar = randomString.charAt(i);
            assertTrue(Character.isDigit(randomChar));
        }
    }

    /**
     * Test for random method with specific character set.
     */
    @Test
    public void testRandomWithSpecificCharacterSet() {
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char randomChar = randomString.charAt(i);
            assertTrue(randomString.chars().allMatch(c -> Character.isAlphabetic(c) || Character.isDigit(c)));
        }
    }

    /**
     * Test for random method with specific character set and length zero.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithSpecificCharacterSetAndLengthZero() {
        RandomStringUtils.random("abc", 0);
    }

    /**
     * Test for random method with specific character set and negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithSpecificCharacterSetAndNegativeLength() {
        RandomStringUtils.random("abc", -1);
    }

    /**
     * Test for randomAscii method with specific character set and length zero.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAsciiWithSpecificCharacterSetAndLengthZero() {
        RandomStringUtils.randomAscii(" ", 0);
    }

    /**
     * Test for randomAscii method with specific character set and negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAsciiWithSpecificCharacterSetAndNegativeLength() {
        RandomStringUtils.randomAscii(" ", -1);
    }

    /**
     * Test for randomAlphabetic method with specific character set and length zero.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphabeticWithSpecificCharacterSetAndLengthZero() {
        RandomStringUtils.randomAlphabetic("abc", 0);
    }

    /**
     * Test for randomAlphabetic method with specific character set and negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphabeticWithSpecificCharacterSetAndNegativeLength() {
        RandomStringUtils.randomAlphabetic("abc", -1);
    }

    /**
     * Test for randomAlphanumeric method with specific character set and length zero.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphanumericWithSpecificCharacterSetAndLengthZero() {
        RandomStringUtils.randomAlphanumeric("abc", 0);
    }

    /**
     * Test for randomAlphanumeric method with specific character set and negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphanumericWithSpecificCharacterSetAndNegativeLength() {
        RandomStringUtils.randomAlphanumeric("abc", -1);
    }

    /**
     * Test for randomNumeric method with specific character set and length zero.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNumericWithSpecificCharacterSetAndLengthZero() {
        RandomStringUtils.randomNumeric("0123456789", 0);
    }

    /**
     * Test for randomNumeric method with specific character set and negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNumericWithSpecificCharacterSetAndNegativeLength() {
        RandomStringUtils.randomNumeric("0123456789", -1);
    }
}