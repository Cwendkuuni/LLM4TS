package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertEquals(5, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, randomAlphabeticString.length());
        assertTrue(randomAlphabeticString.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, randomAlphanumericString.length());
        assertTrue(randomAlphanumericString.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertEquals(5, randomNumericString.length());
        assertTrue(randomNumericString.matches("[0-9]+"));
    }

    @Test
    public void testRandom_CustomChars() {
        String customChars = "ABCD";
        String randomString = RandomStringUtils.random(5, customChars);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(customChars.contains(String.valueOf(c)));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandom_NegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandom_EmptyCharsArray() {
        RandomStringUtils.random(5, new char[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandom_NegativeCount_CustomChars() {
        RandomStringUtils.random(-1, "ABCD");
    }

}