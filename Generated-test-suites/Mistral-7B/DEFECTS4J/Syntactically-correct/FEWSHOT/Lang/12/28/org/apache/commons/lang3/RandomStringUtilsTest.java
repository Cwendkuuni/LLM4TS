package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            int asc = (int) randomAsciiString.charAt(i);
            assert(asc >= 32 && asc <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            int asc = (int) randomAlphabeticString.charAt(i);
            assert(asc >= 97 && asc <= 122 || asc >= 65 && asc <= 90);
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            int asc = (int) randomAlphanumericString.charAt(i);
            assert(asc >= 32 && asc <= 126 || asc >= 48 && asc <= 57 || asc >= 97 && asc <= 122 || asc >= 65 && asc <= 90);
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            int asc = (int) randomNumericString.charAt(i);
            assert(asc >= 48 && asc <= 57);
        }
    }

    @Test
    public void testRandomCustom() {
        String customChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String randomCustomString = RandomStringUtils.random(5, customChars.toCharArray());
        assertNotNull(randomCustomString);
        assertEquals(5, randomCustomString.length());
        for (int i = 0; i < randomCustomString.length(); i++) {
            int index = customChars.indexOf(randomCustomString.charAt(i));
            assert(index != -1);
        }
    }

    @Test
    public void testRandomStartEnd() {
        String randomStartEndString = RandomStringUtils.random(5, 65, 90);
        assertNotNull(randomStartEndString);
        assertEquals(5, randomStartEndString.length());
        for (int i = 0; i < randomStartEndString.length(); i++) {
            int asc = (int) randomStartEndString.charAt(i);
            assert(asc >= 65 && asc <= 90);
        }
    }

    @Test
    public void testRandomLettersNumbers() {
        String randomLettersNumbersString = RandomStringUtils.random(5, true, true);
        assertNotNull(randomLettersNumbersString);
        assertEquals(5, randomLettersNumbersString.length());
        for (int i = 0; i < randomLettersNumbersString.length(); i++) {
            int asc = (int) randomLettersNumbersString.charAt(i);
            if (asc >= 48 && asc <= 57) {
                continue;
            }
            if (asc >= 97 && asc <= 122 || asc >= 65 && asc <= 90) {
                continue;
            }
            throw new AssertionError("Unexpected character encountered: " + asc);
        }
    }

    @Test
    public void testRandomWithCharsArray() {
        char[] customChars = new char[]{'A', 'B', 'C', 'D', 'E'};
        String randomWithCharsArrayString = RandomStringUtils.random(5, customChars);
        assertNotNull(randomWithCharsArrayString);
        assertEquals(5, randomWithCharsArrayString.length());
        for (int i = 0; i < randomWithCharsArrayString.length(); i++) {
            char expectedChar = customChars[new Random().nextInt(customChars.length)];
            char actualChar = randomWithCharsArrayString.charAt(i);
            assertEquals(expectedChar, actualChar);
        }
    }
}