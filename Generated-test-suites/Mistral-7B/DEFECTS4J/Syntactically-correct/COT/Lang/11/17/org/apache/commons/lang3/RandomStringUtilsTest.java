package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;

import org.junit.Test;

/**
 * Test class for RandomStringUtils
 */
public class RandomStringUtilsTest {

    /**
     * Test method for {@link RandomStringUtils#random(int)}
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character is an ASCII character.", (c >= 32 && c <= 126));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character is an alphabetic character.", Character.isAlphabetic(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character is an alphanumeric character.", Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character is a numeric character.", Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean)}
     */
    @Test
    public void testRandomWithBoundsAndFlags() {
        String randomString = RandomStringUtils.random(5, 32, 126, false, false);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character is an ASCII character.", (c >= 32 && c <= 126));
        }

        String randomStringAlphabetic = RandomStringUtils.random(5, true, false);
        assertNotNull(randomStringAlphabetic);
        assertEquals(5, randomStringAlphabetic.length());
        for (int i = 0; i < randomStringAlphabetic.length(); i++) {
            char c = randomStringAlphabetic.charAt(i);
            assertTrue("Character is an alphabetic character.", Character.isAlphabetic(c));
        }

        String randomStringAlphanumeric = RandomStringUtils.random(5, true, true);
        assertNotNull(randomStringAlphanumeric);
        assertEquals(5, randomStringAlphanumeric.length());
        for (int i = 0; i < randomStringAlphanumeric.length(); i++) {
            char c = randomStringAlphanumeric.charAt(i);
            assertTrue("Character is an alphanumeric character.", Character.isAlphabetic(c) || Character.isDigit(c));
        }

        String randomStringNumeric = RandomStringUtils.random(5, false, true);
        assertNotNull(randomStringNumeric);
        assertEquals(5, randomStringNumeric.length());
        for (int i = 0; i < randomStringNumeric.length(); i++) {
            char c = randomStringNumeric.charAt(i);
            assertTrue("Character is a numeric character.", Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     */
    @Test
    public void testRandomWithCustomCharacterSet() {
        String randomString = RandomStringUtils.random(5, 32, 126, false, false, null, RandomStringUtils.RANDOM);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character is an ASCII character.", (c >= 32 && c <= 126));
        }

        String randomStringAlphabeticSet = "abc".toCharArray();
        String randomStringAlphabetic = RandomStringUtils.random(5, randomStringAlphabeticSet, 0, true, false, randomStringAlphabeticSet, RandomStringUtils.RANDOM);
        assertNotNull(randomStringAlphabetic);
        assertEquals(5, randomStringAlphabetic.length());
        for (int i = 0; i < randomStringAlphabetic.length(); i++) {
            char c = randomStringAlphabetic.charAt(i);
            assertTrue("Character is an alphabetic character.", Character.isAlphabetic(c));
        }

        String randomStringAlphanumericSet = "0123456789abcdef".toCharArray();
        String randomStringAlphanumeric = RandomStringUtils.random(5, randomStringAlphanumericSet, 0, true, true, randomStringAlphanumericSet, RandomStringUtils.RANDOM);
        assertNotNull(randomStringAlphanumeric);
        assertEquals(5, randomStringAlphanumeric.length());
        for (int i = 0; i < randomStringAlphanumeric.length(); i++) {
            char c = randomStringAlphanumeric.charAt(i);
            assertTrue("Character is an alphanumeric character.", Character.isAlphabetic(c) || Character.isDigit(c));
        }

        String randomStringNumericSet = "0123456789".toCharArray();
        String randomStringNumeric = RandomStringUtils.random(5, randomStringNumericSet, 0, false, true, randomStringNumericSet, RandomStringUtils.RANDOM);
        assertNotNull(randomStringNumeric);
        assertEquals(5, randomStringNumeric.length());
        for (int i = 0; i < randomStringNumeric.length(); i++) {
            char c = randomStringNumeric.charAt(i);
            assertTrue("Character is a numeric character.", Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     * with an empty character set
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterSet() {
        RandomStringUtils.random(5, new char[0], 0, false, false, null, RandomStringUtils.RANDOM);
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int)} with invalid bounds
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidBounds() {
        RandomStringUtils.random(5, -1, 0);
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)}
     * with invalid bounds and an empty character set
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidBoundsAndEmptyCharacterSet() {
        RandomStringUtils.random(5, -1, 0, false, false, new char[0], RandomStringUtils.RANDOM);
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)}
     * with invalid bounds and a character set that is not empty
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithInvalidBoundsAndNonEmptyCharacterSet() {
        char[] characterSet = "abc".toCharArray();
        RandomStringUtils.random(5, -1, 0, false, false, characterSet, RandomStringUtils.RANDOM);
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)}
     * with a negative count
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1, 0, false, false, null, RandomStringUtils.RANDOM);
    }
}