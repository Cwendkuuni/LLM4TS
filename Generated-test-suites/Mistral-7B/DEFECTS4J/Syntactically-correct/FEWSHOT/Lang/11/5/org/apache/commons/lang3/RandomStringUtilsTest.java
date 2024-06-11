package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char currentChar = randomString.charAt(i);
            assertTrue("Character should be ASCII", Character.isASCII(currentChar));
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char currentChar = randomString.charAt(i);
            assertTrue("Character should be alphabetic", Character.isAlphabetic(currentChar));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char currentChar = randomString.charAt(i);
            assertTrue("Character should be alphanumeric", Character.isAlphabetic(currentChar) || Character.isDigit(currentChar));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char currentChar = randomString.charAt(i);
            assertTrue("Character should be numeric", Character.isDigit(currentChar));
        }
    }

    @Test
    public void testRandomWithRange() {
        String randomString = RandomStringUtils.random("abcd", 5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char currentChar = randomString.charAt(i);
            assertEquals("Character should be from the given range", "abcd".charAt(i % 4), currentChar);
        }
    }

    @Test
    public void testRandomWithCustomChars() {
        String randomString = RandomStringUtils.random("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ", 10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char currentChar = randomString.charAt(i);
            assertTrue("Character should be from the given set", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(currentChar) != -1);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharsArray() {
        RandomStringUtils.random(null);
    }

}