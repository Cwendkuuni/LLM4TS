package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        String str = RandomStringUtils.random(10);
        assertNotNull(str);
        assertEquals(10, str.length());
    }

    @Test
    public void testRandomAsciiString() {
        String str = RandomStringUtils.randomAscii(10);
        assertNotNull(str);
        assertEquals(10, str.length());

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            assertTrue(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        String str = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(str);
        assertEquals(10, str.length());

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        String str = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(str);
        assertEquals(10, str.length());

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumericString() {
        String str = RandomStringUtils.randomNumeric(10);
        assertNotNull(str);
        assertEquals(10, str.length());

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomStringWithRange() {
        String str = RandomStringUtils.random(10, 65, 90);
        assertNotNull(str);
        assertEquals(10, str.length());

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            assertTrue(c >= 65 && c <= 90);
        }
    }

    @Test
    public void testRandomStringWithLetters() {
        String str = RandomStringUtils.random(10, true, false);
        assertNotNull(str);
        assertEquals(10, str.length());

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            assertTrue(Character.isAlphabetic(c) || c == '\u0000');
        }
    }

    @Test
    public void testRandomStringWithNumbers() {
        String str = RandomStringUtils.random(10, false, true);
        assertNotNull(str);
        assertEquals(10, str.length());

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            assertTrue(Character.isDigit(c) || c == '\u0000');
        }
    }

    @Test
    public void testRandomStringWithLettersAndNumbers() {
        String str = RandomStringUtils.random(10, true, true);
        assertNotNull(str);
        assertEquals(10, str.length());

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '\u0000');
        }
    }

    @Test
    public void testRandomStringWithCustomChars() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String str = RandomStringUtils.random(10, chars.toCharArray());
        assertNotNull(str);
        assertEquals(10, str.length());

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            assertTrue(chars.indexOf(c) != -1);
        }
    }

    @Test
    public void testRandomStringWithCustomRandom() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String str = RandomStringUtils.random(10, 0, chars.length(), true, true, chars.toCharArray(), new Random(1L));
        assertNotNull(str);
        assertEquals(10, str.length());

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            assertTrue(chars.indexOf(c) != -1);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStringLength() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCustomChars() {
        String str = RandomStringUtils.random(10, (String) null);
    }

}