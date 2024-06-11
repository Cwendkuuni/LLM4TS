package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        assertEquals("", RandomStringUtils.random(0));
        assertEquals("a", RandomStringUtils.random(1));
        assertEquals("abc", RandomStringUtils.random(3));
        String str = RandomStringUtils.random(10);
        assertNotNull(str);
        assertEquals(10, str.length());
    }

    @Test
    public void testRandomAscii() {
        assertEquals("", RandomStringUtils.randomAscii(0));
        assertEquals("a", RandomStringUtils.randomAscii(1));
        assertEquals("abc", RandomStringUtils.randomAscii(3));
        String str = RandomStringUtils.randomAscii(10);
        assertNotNull(str);
        assertEquals(10, str.length());
        for (char c : str.toCharArray()) {
            assertTrue(Character.isISOControl(c) || Character.isWhitespace(c) || Character.isDigit(c) || Character.isLetter(c) || Character.isUpperCase(c) || Character.isLowerCase(c));
        }
    }

    @Test
    public void testRandomAlphabetic() {
        assertEquals("", RandomStringUtils.randomAlphabetic(0));
        assertEquals("a", RandomStringUtils.randomAlphabetic(1));
        assertEquals("abc", RandomStringUtils.randomAlphabetic(3));
        String str = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(str);
        assertEquals(10, str.length());
        for (char c : str.toCharArray()) {
            assertTrue(Character.isLetter(c) || Character.isUpperCase(c) || Character.isLowerCase(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        assertEquals("", RandomStringUtils.randomAlphanumeric(0));
        assertEquals("a", RandomStringUtils.randomAlphanumeric(1));
        assertEquals("abc", RandomStringUtils.randomAlphanumeric(3));
        String str = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(str);
        assertEquals(10, str.length());
        for (char c : str.toCharArray()) {
            assertTrue(Character.isLetter(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        assertEquals("", RandomStringUtils.randomNumeric(0));
        assertEquals("0", RandomStringUtils.randomNumeric(1));
        assertEquals("012", RandomStringUtils.randomNumeric(3));
        String str = RandomStringUtils.randomNumeric(10);
        assertNotNull(str);
        assertEquals(10, str.length());
        for (char c : str.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithStartAndEnd() {
        assertEquals("", RandomStringUtils.random(0, 0, 0, false, false, null, RANDOM));
        assertEquals("a", RandomStringUtils.random(1, 0, 0, false, false, null, RANDOM));
        assertEquals("abc", RandomStringUtils.random(3, 0, 0, false, false, null, RANDOM));
        String str = RandomStringUtils.random(10, 0, 0, false, false, null, RANDOM);
        assertNotNull(str);
        assertEquals(10, str.length());
        for (char c : str.toCharArray()) {
            assertTrue(Character.isISOControl(c) || Character.isWhitespace(c) || Character.isDigit(c) || Character.isLetter(c) || Character.isUpperCase(c) || Character.isLowerCase(c));
        }

        // Test with specified set
        assertEquals("", RandomStringUtils.random(0, 0, 0, false, false, new char[]{}, RANDOM));
        assertEquals("a", RandomStringUtils.random(1, 0, 0, false, false, new char[]{'a'}, RANDOM));
        assertEquals("ab", RandomStringUtils.random(2, 0, 1, false, false, new char[]{'a', 'b'}, RANDOM));
        assertEquals("abc", RandomStringUtils.random(3, 0, 2, false, false, new char[]{'a', 'b', 'c'}, RANDOM));
        String str2 = RandomStringUtils.random(10, 0, 25, false, false, new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'}, RANDOM);
        assertNotNull(str2);
        assertEquals(10, str2.length());
        for (char c : str2.toCharArray()) {
            assertTrue(Character.isLetter(c) && (c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f' || c == 'g' || c == 'h' || c == 'i' || c == 'j'));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        assertEquals("", RandomStringUtils.random(0, false, false));
        assertEquals("0", RandomStringUtils.random(1, false, true));
        assertEquals("01", RandomStringUtils.random(2, false, true));
        assertEquals("abc", RandomStringUtils.random(3, true, false));
        assertEquals("ab0", RandomStringUtils.random(4, true, true));
        assertEquals("abc123", RandomStringUtils.random(7, true, true));
        String str = RandomStringUtils.random(10, true, true);
        assertNotNull(str);
        assertEquals(10, str.length());
        for (char c : str.toCharArray()) {
            assertTrue(Character.isLetter(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithCustomCharSet() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        assertEquals("", RandomStringUtils.random(0, chars.length(), chars.length(), true, true, chars.toCharArray(), RANDOM));
        assertEquals("A", RandomStringUtils.random(1, chars.length(), chars.length(), true, true, chars.toCharArray(), RANDOM));
        assertEquals("AB", RandomStringUtils.random(2, chars.length(), chars.length(), true, true, chars.toCharArray(), RANDOM));
        assertEquals("ABC", RandomStringUtils.random(3, chars.length(), chars.length(), true, true, chars.toCharArray(), RANDOM));
        String str = RandomStringUtils.random(10, chars.length(), chars.length(), true, true, chars.toCharArray(), RANDOM);
        assertNotNull(str);
        assertEquals(10, str.length());
        for (char c : str.toCharArray()) {
            assertTrue(Character.isLetter(c) || Character.isDigit(c) || chars.indexOf(c) != -1);
        }
    }
}