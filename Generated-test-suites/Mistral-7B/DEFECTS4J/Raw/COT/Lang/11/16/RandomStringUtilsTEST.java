package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length should match", count, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        int count = 10;
        String randomString = RandomStringUtils.randomAscii(count);
        assertEquals("Length should match", count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue("Char should be ASCII", (c >= 32 && c <= 126));
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Length should match", count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue("Char should be alphabetic", Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Length should match", count, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (Character.isDigit(c)) {
                assertTrue("Digit should be in range 0-9", Character.isDigit(c) && (c >= '0' && c <= '9'));
            } else {
                assertTrue("Char should be alphabetic or digit", Character.isAlphabetic(c) || Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomNumeric() {
        int count = 10;
        String randomString = RandomStringUtils.randomNumeric(count);
        assertEquals("Length should match", count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue("Char should be digit", Character.isDigit(c));
            assertTrue("Digit should be in range 0-9", Character.isDigit(c) && (c >= '0' && c <= '9'));
        }
    }

    @Test
    public void testRandomWithRange() {
        int count = 10;
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyz", count);
        assertEquals("Length should match", count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue("Char should be in provided range", Character.isAlphabetic(c) && (c >= 'a' && c <= 'z'));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharsArray() {
        RandomStringUtils.random(new char[0]);
    }

    @Test
    public void testRandomWithCustomCharArray() {
        char[] chars = {'a', 'b', 'c'};
        int count = 10;
        String randomString = RandomStringUtils.random(count, chars);
        assertEquals("Length should match", count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue("Char should be in provided array", contains(chars, c));
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomOutOfBounds() {
        char[] chars = {'a', 'b', 'c'};
        int start = chars.length;
        int end = chars.length - 1;
        String randomString = RandomStringUtils.random(1, chars, start, end, true, true);
    }

    @Test
    public void testRandomWithStartEndAndCustomCharArray() {
        char[] chars = {'a', 'b', 'c'};
        int start = 1;
        int end = 2;
        int count = 10;
        String randomString = RandomStringUtils.random(count, chars, start, end, true, true);
        assertEquals("Length should match", count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue("Char should be in provided array and range", contains(chars, c) && (c >= chars[start] && c <= chars[end]));
        }
    }

    @Test
    public void testRandomSurrogateChars() {
        int count = 10;
        String randomString = RandomStringUtils.random(count, false, false);
        for (char c : randomString.toCharArray()) {
            if (Character.isHighSurrogate(c)) {
                int nextCharIndex = randomString.indexOf(c, randomString.indexOf(c) + 1);
                assertTrue("Should have a following low surrogate", nextCharIndex != -1);
                char nextChar = randomString.charAt(nextCharIndex);
                assertTrue("Next character should be low surrogate", Character.isLowSurrogate(nextChar));
            }
        }
    }

    private static boolean contains(char[] array, char c) {
        for (char element : array) {
            if (element == c) {
                return true;
            }
        }
        return false;
    }

}