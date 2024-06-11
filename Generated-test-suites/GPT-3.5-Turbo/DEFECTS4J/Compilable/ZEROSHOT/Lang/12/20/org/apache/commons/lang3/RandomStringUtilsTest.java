package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Random;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 10;
        String result = RandomStringUtils.random(count);
        assertEquals(count, result.length());
    }

    @Test
    public void testRandomAscii() {
        int count = 10;
        String result = RandomStringUtils.randomAscii(count);
        assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String result = RandomStringUtils.randomAlphabetic(count);
        assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String result = RandomStringUtils.randomAlphanumeric(count);
        assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        int count = 10;
        String result = RandomStringUtils.randomNumeric(count);
        assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        int count = 10;
        boolean letters = true;
        boolean numbers = true;
        String result = RandomStringUtils.random(count, letters, numbers);
        assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomWithCustomRange() {
        int count = 10;
        int start = 'A';
        int end = 'Z';
        boolean letters = true;
        boolean numbers = false;
        String result = RandomStringUtils.random(count, start, end, letters, numbers);
        assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetter(c) && c >= start && c <= end);
        }
    }

    @Test
    public void testRandomWithCustomChars() {
        int count = 10;
        char[] chars = {'A', 'B', 'C', 'D', 'E', 'F'};
        String result = RandomStringUtils.random(count, chars);
        assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(c == 'A' || c == 'B' || c == 'C' || c == 'D' || c == 'E' || c == 'F');
        }
    }

    @Test
    public void testRandomWithRandomInstance() {
        int count = 10;
        int start = 'A';
        int end = 'Z';
        boolean letters = true;
        boolean numbers = false;
        char[] chars = null;
        Random random = new Random();
        String result = RandomStringUtils.random(count, start, end, letters, numbers, chars, random);
        assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetter(c) && c >= start && c <= end);
        }
    }

    @Test
    public void testRandomWithEmptyCharsArray() {
        int count = 10;
        char[] chars = {};
        try {
            String result = RandomStringUtils.random(count, chars);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testRandomWithEmptyString() {
        int count = 10;
        String chars = "";
        try {
            String result = RandomStringUtils.random(count, chars);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
}