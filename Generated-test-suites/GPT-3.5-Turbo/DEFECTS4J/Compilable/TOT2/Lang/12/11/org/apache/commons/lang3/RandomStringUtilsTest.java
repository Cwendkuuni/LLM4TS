package org.apache.commons.lang3;

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
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    // Additional edge cases and exception handling scenarios

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomEmptyCharacters() {
        String randomString = RandomStringUtils.random(10, "");
        assertEquals("", randomString);
    }

    @Test
    public void testRandomWithSpecificCharacters() {
        String randomString = RandomStringUtils.random(10, "abc123");
        for (char c : randomString.toCharArray()) {
            assertTrue(c == 'a' || c == 'b' || c == 'c' || c == '1' || c == '2' || c == '3');
        }
    }

    @Test
    public void testRandomBoundaryCharacters() {
        String randomString = RandomStringUtils.random(10, 33, 65, false, false);
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 33 && c < 65);
        }
    }

}