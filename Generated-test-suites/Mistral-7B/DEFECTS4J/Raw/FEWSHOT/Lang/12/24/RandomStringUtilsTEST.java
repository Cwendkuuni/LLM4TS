package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomStr = RandomStringUtils.random(10);
        assertNotNull(randomStr);
        assertEquals(10, randomStr.length());

        String randomAsciiStr = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiStr);
        assertEquals(10, randomAsciiStr.length());

        String randomAlphabeticStr = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticStr);
        assertEquals(10, randomAlphabeticStr.length());

        String randomAlphanumericStr = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericStr);
        assertEquals(10, randomAlphanumericStr.length());

        String randomNumericStr = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericStr);
        assertEquals(10, randomNumericStr.length());

        String randomStringWithCustomLengthAndLettersAndNumbers = RandomStringUtils.random(5, true, true);
        assertNotNull(randomStringWithCustomLengthAndLettersAndNumbers);
        assertEquals(5, randomStringWithCustomLengthAndLettersAndNumbers.length());
        for (int i = 0; i < randomStringWithCustomLengthAndLettersAndNumbers.length(); i++) {
            char randomChar = randomStringWithCustomLengthAndLettersAndNumbers.charAt(i);
            if (Character.isLetter(randomChar) || Character.isDigit(randomChar)) {
                continue;
            } else {
                fail("Randomly generated character is not a letter or a digit: " + randomChar);
            }
        }

        String randomStringWithCustomLengthAndLetters = RandomStringUtils.random(5, true, false);
        assertNotNull(randomStringWithCustomLengthAndLetters);
        assertEquals(5, randomStringWithCustomLengthAndLetters.length());
        for (int i = 0; i < randomStringWithCustomLengthAndLetters.length(); i++) {
            char randomChar = randomStringWithCustomLengthAndLetters.charAt(i);
            assertTrue(Character.isLetter(randomChar));
        }

        String randomStringWithCustomLengthAndNumbers = RandomStringUtils.random(5, false, true);
        assertNotNull(randomStringWithCustomLengthAndNumbers);
        assertEquals(5, randomStringWithCustomLengthAndNumbers.length());
        for (int i = 0; i < randomStringWithCustomLengthAndNumbers.length(); i++) {
            char randomChar = randomStringWithCustomLengthAndNumbers.charAt(i);
            assertTrue(Character.isDigit(randomChar));
        }

        String randomStringWithCustomLengthAndCharSet = RandomStringUtils.random(5, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        assertNotNull(randomStringWithCustomLengthAndCharSet);
        assertEquals(5, randomStringWithCustomLengthAndCharSet.length());
        String expected = "abcde";
        assertEquals(expected, randomStringWithCustomLengthAndCharSet);

        String randomStringWithCustomLengthAndStartEndAndLettersAndNumbers = RandomStringUtils.random(5, 5, 10, true, true, null, new Random());
        assertNotNull(randomStringWithCustomLengthAndStartEndAndLettersAndNumbers);
        assertEquals(5, randomStringWithCustomLengthAndStartEndAndLettersAndNumbers.length());
        for (int i = 0; i < randomStringWithCustomLengthAndStartEndAndLettersAndNumbers.length(); i++) {
            char randomChar = randomStringWithCustomLengthAndStartEndAndLettersAndNumbers.charAt(i);
            if (Character.isLetter(randomChar) || Character.isDigit(randomChar)) {
                continue;
            } else {
                fail("Randomly generated character is not a letter or a digit: " + randomChar);
            }
        }

        String randomStringWithCustomLengthAndStartEndAndLetters = RandomStringUtils.random(5, 5, 10, true, false, null, new Random());
        assertNotNull(randomStringWithCustomLengthAndStartEndAndLetters);
        assertEquals(5, randomStringWithCustomLengthAndStartEndAndLetters.length());
        for (int i = 0; i < randomStringWithCustomLengthAndStartEndAndLetters.length(); i++) {
            char randomChar = randomStringWithCustomLengthAndStartEndAndLetters.charAt(i);
            assertTrue(Character.isLetter(randomChar));
        }

        String randomStringWithCustomLengthAndStartEndAndNumbers = RandomStringUtils.random(5, 5, 10, false, true, null, new Random());
        assertNotNull(randomStringWithCustomLengthAndStartEndAndNumbers);
        assertEquals(5, randomStringWithCustomLengthAndStartEndAndNumbers.length());
        for (int i = 0; i < randomStringWithCustomLengthAndStartEndAndNumbers.length(); i++) {
            char randomChar = randomStringWithCustomLengthAndStartEndAndNumbers.charAt(i);
            assertTrue(Character.isDigit(randomChar));
        }

        String randomStringWithCustomLengthAndStartEndAndCharSet = RandomStringUtils.random(5, 5, 10, true, false, "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray(), new Random());
        assertNotNull(randomStringWithCustomLengthAndStartEndAndCharSet);
        assertEquals(5, randomStringWithCustomLengthAndStartEndAndCharSet.length());
        String expectedChars = "abcde";
        assertEquals(expectedChars, randomStringWithCustomLengthAndStartEndAndCharSet);
    }

    @Test
    public void testRandomWithEmptyCharSet() {
        try {
            RandomStringUtils.random(1, (String) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testRandomWithNegativeCount() {
        try {
            RandomStringUtils.random(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}