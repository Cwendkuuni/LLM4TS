package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

import org.junit.Test;

/**
 * Test class for {@link RandomStringUtils}
 */
public class RandomStringUtilsTest {

    /**
     * Test for {@link RandomStringUtils#random(int)} method
     */
    @Test
    public void testRandom() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Random string of length " + count, count, randomString.length());
    }

    /**
     * Test for {@link RandomStringUtils#randomAscii(int)} method
     */
    @Test
    public void testRandomAscii() {
        int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        for (int i = 0; i < randomAsciiString.length(); i++) {
            int randomChar = randomAsciiString.charAt(i);
            assertTrue("Random character should be in ascii range", (randomChar >= 32 && randomChar <= 126));
        }
        assertEquals("Random string of length " + count, count, randomAsciiString.length());
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphabetic(int)} method
     */
    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            int randomChar = randomAlphabeticString.charAt(i);
            assertTrue("Random character should be alphabetic", Character.isAlphabetic((char) randomChar));
        }
        assertEquals("Random string of length " + count, count, randomAlphabeticString.length());
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphanumeric(int)} method
     */
    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            int randomChar = randomAlphanumericString.charAt(i);
            assertTrue("Random character should be alphanumeric", Character.isAlphabetic((char) randomChar) || Character.isDigit((char) randomChar));
        }
        assertEquals("Random string of length " + count, count, randomAlphanumericString.length());
    }

    /**
     * Test for {@link RandomStringUtils#randomNumeric(int)} method
     */
    @Test
    public void testRandomNumeric() {
        int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        for (int i = 0; i < randomNumericString.length(); i++) {
            int randomChar = randomNumericString.charAt(i);
            assertTrue("Random character should be numeric", Character.isDigit((char) randomChar));
        }
        assertEquals("Random string of length " + count, count, randomNumericString.length());
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean)} method
     */
    @Test
    public void testRandomWithOptions() {
        int count = 10;
        boolean letters = true;
        boolean numbers = true;
        String randomString = RandomStringUtils.random(count, letters, numbers);
        for (int i = 0; i < randomString.length(); i++) {
            int randomChar = randomString.charAt(i);
            if (letters) {
                assertTrue("Random character should be alphabetic", Character.isAlphabetic((char) randomChar));
            }
            if (numbers) {
                assertTrue("Random character should be numeric", Character.isDigit((char) randomChar));
            }
        }
        assertEquals("Random string of length " + count, count, randomString.length());
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method with default random source
     */
    @Test
    public void testRandomWithDefaultRandomSource() {
        int count = 10;
        boolean letters = true;
        boolean numbers = true;
        String randomString = RandomStringUtils.random(count, 0, 0, letters, numbers, null, null);
        for (int i = 0; i < randomString.length(); i++) {
            int randomChar = randomString.charAt(i);
            if (letters) {
                assertTrue("Random character should be alphabetic", Character.isAlphabetic((char) randomChar));
            }
            if (numbers) {
                assertTrue("Random character should be numeric", Character.isDigit((char) randomChar));
            }
        }
        assertEquals("Random string of length " + count, count, randomString.length());
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method with custom random source
     */
    @Test
    public void testRandomWithCustomRandomSource() {
        int count = 10;
        boolean letters = true;
        boolean numbers = true;
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
        Random customRandom = new Random();
        String randomString = RandomStringUtils.random(count, 0, chars.length, letters, numbers, chars, customRandom);
        for (int i = 0; i < randomString.length(); i++) {
            int randomChar = randomString.charAt(i);
            if (letters) {
                assertTrue("Random character should be alphabetic", Character.isAlphabetic((char) randomChar));
            }
            if (numbers) {
                assertTrue("Random character should be numeric", Character.isDigit((char) randomChar));
            }
            if (customRandom.nextInt(chars.length) < chars.length) {
                assertTrue("Random character should be in custom char array", CharSequenceUtils.containsChar(randomString, chars[customRandom.nextInt(chars.length)]));
            }
        }
        assertEquals("Random string of length " + count, count, randomString.length());
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method with empty char array
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharArray() {
        int count = 10;
        boolean letters = true;
        boolean numbers = true;
        char[] emptyChars = {};
        Random customRandom = new Random();
        RandomStringUtils.random(count, 0, emptyChars.length, letters, numbers, emptyChars, customRandom);
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method with negative count
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        int count = -10;
        boolean letters = true;
        boolean numbers = true;
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
        Random customRandom = new Random();
        RandomStringUtils.random(count, 0, chars.length, letters, numbers, chars, customRandom);
    }

}