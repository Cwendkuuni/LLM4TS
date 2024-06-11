package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertFalse(randomString.isEmpty());
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertFalse(randomAsciiString.isEmpty());
        assertEquals(10, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            int asciiValue = (int) randomAsciiString.charAt(i);
            assertTrue(asciiValue >= 32 && asciiValue <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertFalse(randomAlphabeticString.isEmpty());
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            char randomChar = randomAlphabeticString.charAt(i);
            assertTrue(Character.isAlphabetic(randomChar));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertFalse(randomAlphanumericString.isEmpty());
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char randomChar = randomAlphanumericString.charAt(i);
            assertTrue(Character.isAlphabetic(randomChar) || Character.isDigit(randomChar));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertFalse(randomNumericString.isEmpty());
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            char randomChar = randomNumericString.charAt(i);
            assertTrue(Character.isDigit(randomChar));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharsArray() {
        RandomStringUtils.random(5, (char[]) null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomOutOfRange() {
        String randomString = RandomStringUtils.random(5, 57344, 57345, true, false, null, RANDOM);
    }

    @Test
    public void testRandomHighSurrogate() {
        String randomString = RandomStringUtils.random(2, true, false, null, RANDOM);
        assertEquals(2, randomString.length());
        if (randomString.length() > 1) {
            char highSurrogate = randomString.charAt(0);
            char lowSurrogate = randomString.charAt(1);
            assertTrue(Character.isHighSurrogate(highSurrogate));
            assertTrue(Character.isLowSurrogate(lowSurrogate));
            int combinedChar = Character.toCodePoint(highSurrogate, lowSurrogate);
            assertTrue(combinedChar >= 0x10000 && combinedChar <= 0x10FFFF);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNullRandom() {
        RandomStringUtils.random(10, 0, 0, false, false, null, null);
    }
}