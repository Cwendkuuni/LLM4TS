package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Random length string generated correctly", count, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals("Random Ascii length string generated correctly", count, randomAsciiString.length());
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Random alphabetic length string generated correctly", count, randomAlphabeticString.length());
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Random alphanumeric length string generated correctly", count, randomAlphanumericString.length());
    }

    @Test
    public void testRandomNumeric() {
        int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertEquals("Random numeric length string generated correctly", count, randomNumericString.length());
    }

    @Test
    public void testRandomWithStartEnd() {
        int count = 10;
        int start = 33;
        int end = 126;
        String randomString = RandomStringUtils.random(count, start, end);
        assertEquals("Random length string generated correctly with start and end", count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            int randomChar = randomString.charAt(i);
            assertTrue("All characters are within specified range", randomChar >= start && randomChar <= end);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        int count = -5;
        RandomStringUtils.random(count);
    }

    @Test
    public void testRandomWithLettersNumbersAndChars() {
        int count = 10;
        char[] chars = {'a', 'b', '1', '2', '!', '@', '#', '$', '%'};
        String randomString = RandomStringUtils.random(count, chars);
        assertEquals("Random length string generated correctly with chars array", count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char randomChar = randomString.charAt(i);
            assertTrue("All characters are within specified chars array", containsChar(chars, randomChar));
        }
    }

    @Test
    public void testRandomWithString() {
        int count = 10;
        String chars = "abcdef123!@#$%";
        String randomString = RandomStringUtils.random(count, chars);
        assertEquals("Random length string generated correctly with string", count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char randomChar = randomString.charAt(i);
            assertTrue("All characters are within specified string", chars.indexOf(randomChar) != -1);
        }
    }

    private boolean containsChar(char[] arr, char target) {
        for (char c : arr) {
            if (c == target) {
                return true;
            }
        }
        return false;
    }
}