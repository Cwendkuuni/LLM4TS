package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Tests for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    private static final int COUNT = 10;

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(COUNT);
        assertNotNull(randomString);
        assertEquals(COUNT, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(COUNT);
        assertNotNull(randomAsciiString);
        assertEquals(COUNT, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(Character.isISOControl(c) || Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(COUNT);
        assertNotNull(randomAlphabeticString);
        assertEquals(COUNT, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(COUNT);
        assertNotNull(randomAlphanumericString);
        assertEquals(COUNT, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(COUNT);
        assertNotNull(randomNumericString);
        assertEquals(COUNT, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithStartEnd() {
        int start = 97;
        int end = 122;
        String randomString = RandomStringUtils.random(COUNT, start, end, false, false, null, RANDOM);
        assertNotNull(randomString);
        assertEquals(COUNT, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) && Character.toLowerCase(c) >= start && Character.toLowerCase(c) <= end);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyChars() {
        RandomStringUtils.random(0, (char[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNullChars() {
        RandomStringUtils.random(COUNT, null);
    }

    @Test
    public void testRandomWithChars() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(COUNT, chars.toCharArray());
        assertNotNull(randomString);
        assertEquals(COUNT, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(chars.indexOf(c) != -1);
        }
    }

    @Test
    public void testRandomWithStartEndAndChars() {
        String chars = "abc";
        int start = 97;
        int end = 101;
        String randomString = RandomStringUtils.random(COUNT, start, end, false, false, chars.toCharArray(), RANDOM);
        assertNotNull(randomString);
        assertEquals(COUNT, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) && Character.toLowerCase(c) >= start && Character.toLowerCase(c) <= end && chars.indexOf(c) != -1);
        }
    }

    @Test
    public void testRandomWithCustomRandom() {
        Random customRandom = new Random();
        String randomString = RandomStringUtils.random(COUNT, 0, 0, false, false, null, customRandom);
        assertNotNull(randomString);
        assertEquals(COUNT, randomString.length());
        Random originalRandom = RandomStringUtils.RANDOM;
        RandomStringUtils.RANDOM = customRandom;
        String randomString2 = RandomStringUtils.random(COUNT, 0, 0, false, false, null, null);
        RandomStringUtils.RANDOM = originalRandom;
        assertEquals(randomString, randomString2);
    }

}