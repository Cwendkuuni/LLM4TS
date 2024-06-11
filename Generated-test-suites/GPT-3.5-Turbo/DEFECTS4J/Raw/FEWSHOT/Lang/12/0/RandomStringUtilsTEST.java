package org.apache.commons.lang3;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithChars() {
        char[] chars = {'a', 'b', 'c'};
        String randomString = RandomStringUtils.random(10, chars);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c == 'a' || c == 'b' || c == 'c');
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidLength() {
        RandomStringUtils.random(-10);
    }

    @Test
    public void testRandomWithEmptyChars() {
        char[] chars = {};
        String randomString = RandomStringUtils.random(10, chars);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertFalse(Character.isLetterOrDigit(c));
        }
    }
}