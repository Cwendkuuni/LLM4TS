package org.apache.commons.lang3;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Random;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        Assert.assertEquals("Expected a random string of length 10", 10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        Assert.assertEquals("Expected a random string of length 10 consisting of ASCII characters", 10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue("Expected a random ASCII character", (int) c >= 32 && (int) c <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        Assert.assertEquals("Expected a random string of length 10 consisting of alphabetic characters", 10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue("Expected a random alphabetic character", Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        Assert.assertEquals("Expected a random string of length 10 consisting of alphanumeric characters", 10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue("Expected a random alphanumeric character", (Character.isAlphabetic(c) || Character.isDigit(c)));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        Assert.assertEquals("Expected a random string of length 10 consisting of numeric characters", 10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue("Expected a random numeric character", Character.isDigit(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharsArray() {
        RandomStringUtils.random(10, (char[]) null);
    }

    @Test
    public void testRandomCustomLengthAndRange() {
        char[] chars = {'a', 'b', 'c', 'd'};
        String randomString = RandomStringUtils.random(5, chars);
        Assert.assertEquals("Expected a random string of length 5 using the provided set of characters", 5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue("Expected a random character from the provided set", Arrays.binarySearch(chars, c) >= 0);
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomOutOfRange() {
        String randomString = RandomStringUtils.random(1, 'z'.codePointAt(0), 'a'.codePointAt(0), false, false, null, RANDOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeStart() {
        String randomString = RandomStringUtils.random(1, -1, 0, false, false, null, RANDOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeEnd() {
        String randomString = RandomStringUtils.random(1, 0, -1, false, false, null, RANDOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptySet() {
        String randomString = RandomStringUtils.random(1, '', '', false, false, null, RANDOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNullRandom() {
        String randomString = RandomStringUtils.random(1, 0, 0, false, false, null, null);
    }
}