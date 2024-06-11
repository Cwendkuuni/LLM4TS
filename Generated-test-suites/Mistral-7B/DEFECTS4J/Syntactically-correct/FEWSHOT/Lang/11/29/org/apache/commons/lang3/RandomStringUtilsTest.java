package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10);
            assertNotNull(randomString);
            assertEquals(10, randomString.length());
        }

        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());

        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());

        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());

        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, true, true);
            assertNotNull(randomString);
            assertEquals(10, randomString.length());

            for (char c : randomString.toCharArray()) {
                if (Character.isLetter(c) || Character.isDigit(c)) {
                    continue;
                } else {
                    throw new AssertionError("Randomly generated string contains non-alphanumeric character: " + c);
                }
            }
        }
    }

    @Test
    public void testRandomWithStartEndAndLetters() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, 0, chars.length, true, false, chars, new Random());
            assertNotNull(randomString);
            assertEquals(10, randomString.length());

            String substring = randomString.substring(0, 5);
            for (char c : substring.toCharArray()) {
                if (!Character.isLetter(c)) {
                    throw new AssertionError("Randomly generated substring contains non-letter character: " + c);
                }
            }

            String restOfString = randomString.substring(5);
            for (char c : restOfString.toCharArray()) {
                if (!Character.isLetterOrDigit(c)) {
                    throw new AssertionError("Randomly generated string contains non-alphanumeric character: " + c);
                }
            }
        }
    }

    @Test
    public void testRandomWithStartEndAndNumbers() {
        char[] numbers = "0123456789".toCharArray();

        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, 0, numbers.length, false, true, numbers, new Random());
            assertNotNull(randomString);
            assertEquals(10, randomString.length());

            for (int j = 0; j < randomString.length(); j++) {
                if (!Character.isDigit(randomString.charAt(j))) {
                    throw new AssertionError("Randomly generated string contains non-numeric character: " + randomString.charAt(j));
                }
            }
        }
    }

    @Test
    public void testRandomWithEmptySet() {
        String emptyString = RandomStringUtils.random(1, "", new Random());
        assertNotNull(emptyString);
        assertEquals(1, emptyString.length());
        assertEquals("", emptyString);

        char[] emptyCharArray = new char[0];
        String randomString = RandomStringUtils.random(1, emptyCharArray, new Random());
        assertNotNull(randomString);
        assertEquals(1, randomString.length());
        assertEquals("", randomString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1, "", new Random());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptySetAndLetters() {
        char[] emptyCharArray = new char[0];
        RandomStringUtils.random(1, emptyCharArray, true, false, new Random());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptySetAndNumbers() {
        char[] emptyCharArray = new char[0];
        RandomStringUtils.random(1, emptyCharArray, false, true, new Random());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeStart() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        RandomStringUtils.random(1, -1, chars.length, true, false, chars, new Random());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeEnd() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        RandomStringUtils.random(1, 0, -1, true, false, chars, new Random());
    }
}