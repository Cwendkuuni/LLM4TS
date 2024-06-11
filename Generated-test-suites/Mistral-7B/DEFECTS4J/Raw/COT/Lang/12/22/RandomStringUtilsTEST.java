package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

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
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomAsciiString);
        assertEquals(5, randomAsciiString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomAlphabeticString);
        assertEquals(5, randomAlphabeticString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomAlphanumericString);
        assertEquals(5, randomAlphanumericString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomNumericString);
        assertEquals(5, randomNumericString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean)}
     */
    @Test
    public void testRandomWithRangeAndFlags() {
        String randomString = RandomStringUtils.random(10, 1, 10, false, false);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        String randomStringWithLetters = RandomStringUtils.random(10, 1, 10, true, false);
        assertNotNull(randomStringWithLetters);
        assertEquals(10, randomStringWithLetters.length());

        String randomStringWithNumbers = RandomStringUtils.random(10, 1, 10, false, true);
        assertNotNull(randomStringWithNumbers);
        assertEquals(10, randomStringWithNumbers.length());

        String randomStringWithLettersAndNumbers = RandomStringUtils.random(10, 1, 10, true, true);
        assertNotNull(randomStringWithLettersAndNumbers);
        assertEquals(10, randomStringWithLettersAndNumbers.length());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     */
    @Test
    public void testRandomWithCustomCharacterSet() {
        char[] customCharacterSet = new char[]{'a', 'b', 'c'};
        String randomString = RandomStringUtils.random(5, 0, customCharacterSet.length, true, true, customCharacterSet, new Random());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} with empty character set
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterSet() {
        char[] emptyCharacterSet = new char[0];
        RandomStringUtils.random(5, 0, 0, false, false, emptyCharacterSet, new Random());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} with negative length of character set
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCharacterSetLength() {
        char[] characterSet = new char[10];
        RandomStringUtils.random(5, characterSet.length - 1, 0, false, false, characterSet, new Random());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean)} with negative count
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1, 1, false, false);
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean)} with start and end outside the range of the character set
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidStartEnd() {
        char[] characterSet = new char[]{'a', 'b', 'c', 'd', 'e', 'f'};
        RandomStringUtils.random(5, 7, false, false, characterSet, new Random());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean)} with empty character set
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterSetInBooleanFlags() {
        RandomStringUtils.random(5, 0, true, false);
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean)} with invalid character set length (0)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidCharacterSetLength() {
        RandomStringUtils.random(5, 7, false, false, null, new Random());
    }
}