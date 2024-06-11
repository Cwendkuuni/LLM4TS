package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String result = RandomStringUtils.random(5);
        assertEquals(5, result.length());
    }

    @Test
    public void testRandomAscii() {
        String result = RandomStringUtils.randomAscii(5);
        assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String result = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String result = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String result = RandomStringUtils.randomNumeric(5);
        assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomLettersAndNumbers() {
        String result = RandomStringUtils.random(5, true, true);
        assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomLetters() {
        String result = RandomStringUtils.random(5, true, false);
        assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomNumbers() {
        String result = RandomStringUtils.random(5, false, true);
        assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithCharSet() {
        char[] charSet = {'a', 'b', 'c'};
        String result = RandomStringUtils.random(5, charSet);
        assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(c == 'a' || c == 'b' || c == 'c');
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharSet() {
        char[] charSet = {};
        RandomStringUtils.random(5, charSet);
    }
}