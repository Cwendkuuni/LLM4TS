package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10);
            assertEquals(10, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                Character c = randomString.charAt(j);
                assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || Character.isWhitespace(c) || c == '\u0000');
            }
        }
    }

    @Test
    public void testRandomAscii() {
        for (int i = 0; i < 10; i++) {
            String randomAsciiString = RandomStringUtils.randomAscii(10);
            assertEquals(10, randomAsciiString.length());
            for (int j = 0; j < randomAsciiString.length(); j++) {
                Character c = randomAsciiString.charAt(j);
                assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '!' || c == '@' || c == '#' || c == '$' || c == '%' || c == '^' || c == '&' || c == '*' || c == '(' || c == ')' || c == '_' || c == '+' || c == '-' || c == '=' || c == '[' || c == ']' || c == '{' || c == '}' || c == '|' || c == ';' || c == ':' || c == ',' || c == '.' || c == '/' || c == '?' || c == '`' || c == '~');
            }
        }
    }

    @Test
    public void testRandomAlphabetic() {
        for (int i = 0; i < 10; i++) {
            String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
            assertEquals(10, randomAlphabeticString.length());
            for (int j = 0; j < randomAlphabeticString.length(); j++) {
                Character c = randomAlphabeticString.charAt(j);
                assertTrue(Character.isAlphabetic(c));
            }
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        for (int i = 0; i < 10; i++) {
            String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
            assertEquals(10, randomAlphanumericString.length());
            for (int j = 0; j < randomAlphanumericString.length(); j++) {
                Character c = randomAlphanumericString.charAt(j);
                assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '_');
            }
        }
    }

    @Test
    public void testRandomNumeric() {
        for (int i = 0; i < 10; i++) {
            String randomNumericString = RandomStringUtils.randomNumeric(10);
            assertEquals(10, randomNumericString.length());
            for (int j = 0; j < randomNumericString.length(); j++) {
                Character c = randomNumericString.charAt(j);
                assertTrue(Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomWithStartAndEnd() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random("a", "z", 10);
            assertEquals(10, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                Character c = randomString.charAt(j);
                assertTrue(Character.isAlphabetic(c) && (c >= 'a' && c <= 'z'));
            }
        }
    }

    @Test
    public void testRandomWithCustomChars() {
        String customChars = "!@#$%^&*()_+=-:;{}|\\[\\]~";
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, customChars.toCharArray());
            assertEquals(10, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                Character c = randomString.charAt(j);
                assertTrue(customChars.indexOf(c) != -1);
            }
        }
    }
}