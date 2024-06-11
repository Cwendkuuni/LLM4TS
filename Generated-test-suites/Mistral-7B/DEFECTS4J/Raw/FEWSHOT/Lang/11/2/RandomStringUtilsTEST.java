package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomCustom() {
        String randomString = RandomStringUtils.random(5, 'a', 'z');
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) && (c >= 'a' && c <= 'z'));
        }
    }

    @Test
    public void testRandomCustomWithNumbers() {
        String randomString = RandomStringUtils.random(5, 'a', 'z', true, false);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c) || c == '\u0000');
        }
    }

    @Test
    public void testRandomCustomWithChars() {
        String randomString = RandomStringUtils.random(5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) && ("abcdefghijklmnopqrstuvwxyz".indexOf(c) != -1));
        }
    }

    @Test
    public void testRandomCustomWithStartEnd() {
        String randomString = RandomStringUtils.random(5, 'A', 'Z', true, true, null, null);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c) || c == '\u0000');
            if (Character.isAlphabetic(c)) {
                assert('A' <= c && c <= 'Z');
            }
        }

        String randomString2 = RandomStringUtils.random(5, 'A', 'E', true, true, null, null);
        assertNotNull(randomString2);
        assertEquals(5, randomString2.length());
        for (char c : randomString2.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c) || c == '\u0000');
            if (Character.isAlphabetic(c)) {
                assert('A' <= c && c <= 'E');
            }
        }
    }

    @Test
    public void testRandomCustomWithStartEndChars() {
        String randomString = RandomStringUtils.random(5, 'a', 'z', true, true, "!@#$%^&*()_+-=[]{}|;:,.<>? ".toCharArray(), null);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c) || c == '!' || c == '@' || c == '#' || c == '$' || c == '%' || c == '^' || c == '&' || c == '(' || c == ')' || c == '+' || c == '-' || c == '=' || c == '[' || c == ']' || c == '{' || c == '}' || c == '|' || c == ';' || c == ':' || c == ',' || c == '.' || c == '<' || c == '>' || c == '?' || c == '\u0000');
            if (Character.isAlphabetic(c)) {
                assert('a' <= c && c <= 'z');
            }
        }
    }
}