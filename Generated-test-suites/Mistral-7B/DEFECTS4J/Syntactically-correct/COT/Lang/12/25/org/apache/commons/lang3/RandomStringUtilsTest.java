package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Random;

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
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assert((c >= 32 && c <= 126) || (c >= 9 && c <= 13)); // control characters
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assert((Character.isAlphabetic(c) || Character.isDigit(c)) || (c == '_' || c == '-')); // special characters
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean)}
     */
    @Test
    public void testRandomWithConstraints() {
        String randomStringWithConstraints = RandomStringUtils.random(10, 25, false, false);
        assertNotNull(randomStringWithConstraints);
        assertEquals(10, randomStringWithConstraints.length());
        for (char c : randomStringWithConstraints.toCharArray()) {
            assert((c >= 0 && c <= 25) || (c >= 33 && c <= 126)); // printable ASCII characters except control characters
        }
    }

    /**
     * Test method for
     * {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     */
    @Test
    public void testRandomWithCustomCharacterSetAndRandom() {
        String customCharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomStringWithCustomCharacterSetAndRandom = RandomStringUtils.random(10, new Random(), customCharacterSet.toCharArray());
        assertNotNull(randomStringWithCustomCharacterSetAndRandom);
        assertEquals(10, randomStringWithCustomCharacterSetAndRandom.length());
        for (char c : randomStringWithCustomCharacterSetAndRandom.toCharArray()) {
            assert(customCharacterSet.indexOf(c) != -1);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} with null character set
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNullCharacterSet() {
        RandomStringUtils.random(10, 25, false, false, null, null, new Random());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} with empty character set
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterSet() {
        RandomStringUtils.random(10, 25, false, false, "", new Random());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} with character set length < character count
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithCharacterSetLengthLessThanCharacterCount() {
        String customCharacterSet = "ABC";
        RandomStringUtils.random(10, 25, false, false, customCharacterSet.toCharArray(), new Random());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} with character set length equal to character count
     */
    @Test
    public void testRandomWithCharacterSetLengthEqualToCharacterCount() {
        String customCharacterSet = "ABC";
        String randomStringWithCustomCharacterSetAndRandom = RandomStringUtils.random(3, 25, false, false, customCharacterSet.toCharArray(), new Random());
        assertNotNull(randomStringWithCustomCharacterSetAndRandom);
        assertEquals(3, randomStringWithCustomCharacterSetAndRandom.length());
        for (char c : randomStringWithCustomCharacterSetAndRandom.toCharArray()) {
            assert(customCharacterSet.indexOf(c) != -1);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, String)}
     */
    @Test
    public void testRandomWithStringCharacterSet() {
        String customCharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomStringWithCustomCharacterSet = RandomStringUtils.random(10, customCharacterSet);
        assertNotNull(randomStringWithCustomCharacterSet);
        assertEquals(10, randomStringWithCustomCharacterSet.length());
        for (char c : randomStringWithCustomCharacterSet.toCharArray()) {
            assert(customCharacterSet.indexOf(c) != -1);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, String)} with empty string as character set
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyStringCharacterSet() {
        RandomStringUtils.random(10, "");
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)} with invalid character set
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidCharacterSet() {
        String invalidCharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789$%^";
        RandomStringUtils.random(10, 25, false, false, invalidCharacterSet.toCharArray(), new Random());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)} with character set containing high surrogates
     */
    @Test
    public void testRandomWithCharacterSetContainingHighSurrogates() {
        String customCharacterSet = "\uD800\uDBFF";
        String randomStringWithCustomCharacterSetAndRandom = RandomStringUtils.random(2, new Random(), customCharacterSet.toCharArray());
        assertNotNull(randomStringWithCustomCharacterSetAndRandom);
        assertEquals(2, randomStringWithCustomCharacterSetAndRandom.length());
        for (char c : randomStringWithCustomCharacterSetAndRandom.toCharArray()) {
            assert(Character.isHighSurrogate(c) || Character.isLowSurrogate(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)} with character set containing private high surrogates
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithCharacterSetContainingPrivateHighSurrogates() {
        String customCharacterSet = "\uD800\uDC00";
        RandomStringUtils.random(10, 25, false, false, customCharacterSet.toCharArray(), new Random());
    }
}