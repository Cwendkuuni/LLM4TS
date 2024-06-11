package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 5;
        String result = RandomStringUtils.random(count);
        assertEquals(count, result.length());
    }

    @Test
    public void testRandomAscii() {
        int count = 5;
        String result = RandomStringUtils.randomAscii(count);
        assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            assertTrue((int) c >= 32 && (int) c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 5;
        String result = RandomStringUtils.randomAlphabetic(count);
        assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 5;
        String result = RandomStringUtils.randomAlphanumeric(count);
        assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        int count = 5;
        String result = RandomStringUtils.randomNumeric(count);
        assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithStartEnd() {
        int count = 5;
        int start = 65; // 'A'
        int end = 90; // 'Z'
        String result = RandomStringUtils.random(count, start, end, true, false);
        assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            assertTrue((int) c >= start && (int) c <= end);
        }
    }

    @Test
    public void testRandomWithCustomCharSet() {
        int count = 5;
        char[] customChars = {'A', 'B', 'C', 'D', 'E'};
        String result = RandomStringUtils.random(count, customChars);
        assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(new String(customChars).contains(String.valueOf(c)));
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharset() {
        RandomStringUtils.random(5, new char[]{});
    }

}