package org.apache.commons.lang3;

import org.junit.Assert;
import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RandomStringUtilsTest {

    // Test random(int count) method
    @Test
    public void testRandomCount() {
        String result = RandomStringUtils.random(5);
        assertEquals(5, result.length());
    }

    // Test randomAscii(int count) method
    @Test
    public void testRandomAsciiCount() {
        String result = RandomStringUtils.randomAscii(5);
        assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            assertTrue((int) c >= 32 && (int) c <= 127);
        }
    }

    // Test randomAlphabetic(int count) method
    @Test
    public void testRandomAlphabeticCount() {
        String result = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, result.length());
        assertTrue(Pattern.matches("[a-zA-Z]+", result));
    }

    // Test randomAlphanumeric(int count) method
    @Test
    public void testRandomAlphanumericCount() {
        String result = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, result.length());
        assertTrue(Pattern.matches("[a-zA-Z0-9]+", result));
    }

    // Test randomNumeric(int count) method
    @Test
    public void testRandomNumericCount() {
        String result = RandomStringUtils.randomNumeric(5);
        assertEquals(5, result.length());
        assertTrue(Pattern.matches("[0-9]+", result));
    }

    // Test random(int count, boolean letters, boolean numbers) method
    @Test
    public void testRandomCountLettersAndNumbers() {
        String result = RandomStringUtils.random(5, true, true);
        assertEquals(5, result.length());
        assertTrue(Pattern.matches("[a-zA-Z0-9]+", result));
    }

    // Test random(int count, int start, int end, boolean letters, boolean numbers, char... chars) method
    @Test
    public void testRandomCountStartEndLettersAndNumbers() {
        String result = RandomStringUtils.random(5, 65, 70, true, true, 'A', 'B', 'C', 'D', 'E', 'F');
        assertEquals(5, result.length());
        assertTrue(Pattern.matches("[A-Fa-f0-9]+", result));
    }

    // Test random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) method
    @Test
    public void testRandomCountStartEndLettersNumbersCharsRandom() {
        String result = RandomStringUtils.random(5, 65, 70, true, true, new char[]{'A', 'B', 'C', 'D', 'E', 'F'}, new java.util.Random());
        assertEquals(5, result.length());
        assertTrue(Pattern.matches("[A-Fa-f0-9]+", result));
    }

    // Test random(int count, String chars) method
    @Test
    public void testRandomCountChars() {
        String result = RandomStringUtils.random(5, "ABCDEF");
        assertEquals(5, result.length());
        assertTrue(Pattern.matches("[A-F]+", result));
    }

    // Test random(int count, char... chars) method
    @Test
    public void testRandomCountCharsArray() {
        String result = RandomStringUtils.random(5, 'A', 'B', 'C', 'D', 'E', 'F');
        assertEquals(5, result.length());
        assertTrue(Pattern.matches("[A-F]+", result));
    }

    // Test exception for negative count
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    // Test exception for empty chars array
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharsArray() {
        RandomStringUtils.random(5, new char[0]);
    }
}

