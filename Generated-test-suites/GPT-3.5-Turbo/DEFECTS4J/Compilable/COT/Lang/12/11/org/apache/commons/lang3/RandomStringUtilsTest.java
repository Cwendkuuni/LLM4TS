package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {
    
    @Test
    public void testRandom() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals(count, randomString.length());
    }
    
    @Test
    public void testRandomAscii() {
        int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals(count, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue((int) c >= 32 && (int) c <= 127);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertEquals(count, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals(count, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertEquals(count, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }
    
    @Test
    public void testRandomWithLettersAndNumbers() {
        int count = 10;
        boolean letters = true;
        boolean numbers = true;
        String randomString = RandomStringUtils.random(count, letters, numbers);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }
    
    @Test
    public void testRandomWithStartAndEnd() {
        int count = 10;
        int start = 65; // 'A'
        int end = 90; // 'Z'
        String randomString = RandomStringUtils.random(count, start, end, true, true);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue((int) c >= start && (int) c <= end);
        }
    }
    
    @Test
    public void testRandomWithCharArray() {
        int count = 10;
        char[] chars = {'A', 'B', 'C'};
        String randomString = RandomStringUtils.random(count, chars);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(isInCharArray(c, chars));
        }
    }
    
    private boolean isInCharArray(char c, char[] chars) {
        for (char ch : chars) {
            if (c == ch) {
                return true;
            }
        }
        return false;
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        int count = -10;
        RandomStringUtils.random(count);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharArray() {
        int count = 10;
        char[] chars = {};
        RandomStringUtils.random(count, chars);
    }
    
    @Test
    public void testRandomWithString() {
        int count = 10;
        String chars = "ABC";
        String randomString = RandomStringUtils.random(count, chars);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(chars.contains(String.valueOf(c)));
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyString() {
        int count = 10;
        String chars = "";
        RandomStringUtils.random(count, chars);
    }
    
}
