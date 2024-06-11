package org.apache.commons.lang3;
import org.junit.Test;

import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String result = RandomStringUtils.random(10);
        assertNotNull(result);
        assertEquals(10, result.length());
    }

    @Test
    public void testRandomAscii() {
        String result = RandomStringUtils.randomAscii(10);
        assertNotNull(result);
        assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue((int) c >= 32 && (int) c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String result = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(result);
        assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String result = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(result);
        assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String result = RandomStringUtils.randomNumeric(10);
        assertNotNull(result);
        assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithLetters() {
        String result = RandomStringUtils.random(10, true, false);
        assertNotNull(result);
        assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomWithNumbers() {
        String result = RandomStringUtils.random(10, false, true);
        assertNotNull(result);
        assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithCustomChars() {
        char[] customChars = {'a', 'b', 'c'};
        String result = RandomStringUtils.random(10, customChars);
        assertNotNull(result);
        assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            for (char customChar : customChars) {
                assertEquals(customChar, c);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyChars() {
        char[] emptyChars = {};
        RandomStringUtils.random(10, emptyChars);
    }

}