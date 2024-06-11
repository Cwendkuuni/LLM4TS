package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test method for {@link RandomStringUtils#random(int)}.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            int randomChar = randomAsciiString.charAt(i);
            assertTrue(randomChar >= 32 && randomChar <= 126);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            int randomChar = randomAlphabeticString.charAt(i);
            assertTrue(Character.isAlphabetic((char) randomChar));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            int randomChar = randomAlphanumericString.charAt(i);
            assertTrue(Character.isAlphabetic((char) randomChar) || Character.isDigit((char) randomChar));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            int randomChar = randomNumericString.charAt(i);
            assertTrue(Character.isDigit((char) randomChar));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean)}.
     */
    @Test
    public void testRandomCustom() {
        String randomCustomString = RandomStringUtils.random(10, 33, 58, true, true);
        assertNotNull(randomCustomString);
        assertEquals(10, randomCustomString.length());
        for (int i = 0; i < randomCustomString.length(); i++) {
            int randomChar = randomCustomString.charAt(i);
            assertTrue(Character.isAlphabetic((char) randomChar) || Character.isDigit((char) randomChar) || randomChar == 33 || randomChar == 58);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomCustomWithCharArray() {
        String randomCustomString = RandomStringUtils.random(10, 33, 58, true, true, new char[]{'a', 'b', 'c', 'd'}, new Random());
        assertNotNull(randomCustomString);
        assertEquals(10, randomCustomString.length());
        for (int i = 0; i < randomCustomString.length(); i++) {
            int randomChar = randomCustomString.charAt(i);
            assertTrue(Character.isAlphabetic((char) randomChar) || Character.isDigit((char) randomChar) || randomChar == 33 || randomChar == 58);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean, String)}.
     */
    @Test
    public void testRandomCustomWithString() {
        String randomCustomString = RandomStringUtils.random(10, "0123456789ABCDEF", true, true);
        assertNotNull(randomCustomString);
        assertEquals(10, randomCustomString.length());
        for (int i = 0; i < randomCustomString.length(); i++) {
            int randomChar = randomCustomString.charAt(i);
            assertTrue(Character.isDigit((char) randomChar) || Character.isAlphabetic((char) randomChar));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)} with an empty character array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharacterArray() {
        RandomStringUtils.random(10, new char[0], new Random());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int)} with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLength() {
        RandomStringUtils.random(-1);
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)} with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLengthWithCharArray() {
        RandomStringUtils.random(-1, new char[0], new Random());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean)} with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLengthWithBoolean() {
        RandomStringUtils.random(-1, 33, false, true);
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int)} with negative start.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStart() {
        RandomStringUtils.random(0, -1);
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)} with negative start.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStartWithCharArray() {
        RandomStringUtils.random(-1, 10, true, true, new char[0], new Random());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean)} with negative end.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeEnd() {
        RandomStringUtils.random(10, 0);
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)} with negative end.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeEndWithCharArray() {
        RandomStringUtils.random(10, -1, true, true, new char[0], new Random());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)} with null character array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullCharacterArray() {
        RandomStringUtils.random(10, 10, true, true, null, new Random());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)} with null Random instance.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullRandomInstance() {
        RandomStringUtils.random(10, 10, true, true, new char[0], null);
    }
}