package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10);
            assertNotNull(randomString);
            assertEquals(10, randomString.length());
        }
    }

    @Test
    public void testRandomAsciiString() {
        for (int i = 0; i < 10; i++) {
            String randomAsciiString = RandomStringUtils.randomAscii(10);
            assertNotNull(randomAsciiString);
            assertEquals(10, randomAsciiString.length());
            for (int j = 0; j < randomAsciiString.length(); j++) {
                char c = randomAsciiString.charAt(j);
                assert(c >= 32 && c <= 126);
            }
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        for (int i = 0; i < 10; i++) {
            String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
            assertNotNull(randomAlphabeticString);
            assertEquals(10, randomAlphabeticString.length());
            for (int j = 0; j < randomAlphabeticString.length(); j++) {
                char c = randomAlphabeticString.charAt(j);
                assert(Character.isAlphabetic(c));
            }
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        for (int i = 0; i < 10; i++) {
            String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
            assertNotNull(randomAlphanumericString);
            assertEquals(10, randomAlphanumericString.length());
            for (int j = 0; j < randomAlphanumericString.length(); j++) {
                char c = randomAlphanumericString.charAt(j);
                assert(Character.isAlphabetic(c) || Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomNumericString() {
        for (int i = 0; i < 10; i++) {
            String randomNumericString = RandomStringUtils.randomNumeric(10);
            assertNotNull(randomNumericString);
            assertEquals(10, randomNumericString.length());
            for (int j = 0; j < randomNumericString.length(); j++) {
                char c = randomNumericString.charAt(j);
                assert(Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomStringWithRange() {
        String expected = "abcdefghij";
        String randomString = RandomStringUtils.random("abcdefghij", 10);
        assertEquals(expected, randomString);
    }

    @Test
    public void testRandomStringWithCustomChars() {
        String expected = "!@#$%^&*()_+";
        String randomString = RandomStringUtils.random(10, expected.toCharArray());
        assertEquals(expected, randomString);
    }

    @Test
    public void testRandomStringWithCustomCharsAndNumbers() {
        String expected = "1!@#$%^&*()_+";
        String randomString = RandomStringUtils.random(10, true, true, expected.toCharArray());
        assertEquals(expected, randomString);
    }

    @Test
    public void testRandomStringWithCustomCharsAndLetters() {
        String expected = "abcdefghij";
        String randomString = RandomStringUtils.random(10, true, false, expected.toCharArray());
        assertEquals(expected, randomString);
    }

    @Test
    public void testRandomStringWithCustomCharsAndNoNumbers() {
        String expected = "abcdefghij";
        String randomString = RandomStringUtils.random(10, true, false, null);
        assertEquals(expected, randomString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithInvalidCount() {
        RandomStringUtils.random(-5, 0, 0, false, false, null, RANDOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptyCharsArray() {
        RandomStringUtils.random(10, new char[0]);
    }
}