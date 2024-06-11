package org.apache.commons.lang3;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomAsciiString.length());
        assertTrue(randomAsciiString.chars().allMatch(c -> (c >= 32 && c <= 127)));
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomAlphabeticString.length());
        assertTrue(randomAlphabeticString.chars().allMatch(Character::isLetter));
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomAlphanumericString.length());
        assertTrue(randomAlphanumericString.chars().allMatch(c -> (Character.isLetterOrDigit(c))));
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomNumericString.length());
        assertTrue(randomNumericString.chars().allMatch(Character::isDigit));
    }

    @Test
    public void testRandomWithChars() {
        String chars = "ABC";
        String randomString = RandomStringUtils.random(10, chars);
        assertEquals(10, randomString.length());
        assertTrue(randomString.chars().allMatch(c -> (chars.indexOf(c) >= 0)));
    }

    @Test
    public void testRandomWithStartAndEnd() {
        int start = 'A';
        int end = 'Z';
        String randomString = RandomStringUtils.random(10, start, end, true, false, null, null);
        assertEquals(10, randomString.length());
        assertTrue(randomString.chars().allMatch(c -> (c >= start && c <= end)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        char[] chars = new char[0];
        RandomStringUtils.random(10, chars);
    }
}