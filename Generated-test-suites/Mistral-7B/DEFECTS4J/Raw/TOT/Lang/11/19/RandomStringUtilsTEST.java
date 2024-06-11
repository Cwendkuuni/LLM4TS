package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test for {@link RandomStringUtils#random(int)} method.
     */
    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for {@link RandomStringUtils#randomAscii(int)} method.
     */
    @Test
    public void testRandomAsciiString() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assert((int) c >= 32 && (int) c <= 126);
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphabetic(int)} method.
     */
    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assert(Character.isLetter(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphanumeric(int)} method.
     */
    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assert(Character.isLetterOrDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomNumeric(int)} method.
     */
    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean)} method with all characters.
     */
    @Test
    public void testRandomStringAllCharacters() {
        String randomString = RandomStringUtils.random(10, 0, 0, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean)} method with ASCII characters.
     */
    @Test
    public void testRandomStringASCII() {
        String randomString = RandomStringUtils.random(10, 32, 126, true, false);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert((int) c >= 32 && (int) c <= 126);
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean)} method with alphabetic characters.
     */
    @Test
    public void testRandomStringAlphabetic() {
        String randomString = RandomStringUtils.random(10, 0, 0, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isLetter(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean)} method with alphanumeric characters.
     */
    @Test
    public void testRandomStringAlphanumeric() {
        String randomString = RandomStringUtils.random(10, 0, 0, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isLetterOrDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method with all characters.
     */
    @Test
    public void testRandomStringAllCharactersWithCustomRandom() {
        Random random = new Random();
        String randomString = RandomStringUtils.random(10, 0, 0, true, true, null, random);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method with custom characters.
     */
    @Test
    public void testRandomStringCustomCharactersWithCustomRandom() {
        char[] customChars = {'a', 'b', 'c'};
        Random random = new Random();
        String randomString = RandomStringUtils.random(10, 0, customChars.length, true, false, customChars, random);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(contains(customChars, c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method with error conditions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringErrorConditionsWithCustomRandom() {
        char[] customChars = {'a', 'b'};
        Random random = new Random();
        String randomString = RandomStringUtils.random(10, 0, customChars.length + 1, true, false, customChars, random);
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method with empty character array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringEmptyCharacterArrayWithCustomRandom() {
        char[] emptyChars = {};
        Random random = new Random();
        String randomString = RandomStringUtils.random(10, 0, 0, true, false, emptyChars, random);
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method with negative count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringNegativeCountWithCustomRandom() {
        char[] customChars = {'a', 'b'};
        Random random = new Random();
        String randomString = RandomStringUtils.random(-5, 0, customChars.length, true, false, customChars, random);
    }

    /**
     * Helper method for testing character arrays.
     *
     * @param charArray the character array to search
     * @param character the character to search for
     * @return true if the character is present in the character array, false otherwise
     */
    private boolean contains(char[] charArray, char character) {
        for (char c : charArray) {
            if (c == character) {
                return true;
            }
        }
        return false;
    }
}