package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test cases for RandomStringUtils class
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length should match", count, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        int count = 10;
        String randomString = RandomStringUtils.randomAscii(count);
        assertEquals("Length should match", count, randomString.length());
    }

    @Test
    public void testRandomAlphabeticString() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Length should match", count, randomString.length());
    }

    @Test
    public void testRandomAlphanumericString() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Length should match", count, randomString.length());
    }

    @Test
    public void testRandomNumericString() {
        int count = 10;
        String randomString = RandomStringUtils.randomNumeric(count);
        assertEquals("Length should match", count, randomString.length());
    }

    @Test
    public void testRandomCustomString() {
        int count = 10;
        String customString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(count, customString);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue("Randomly generated character should be present in the custom set", customString.indexOf(randomChar) != -1);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCountForRandomString() {
        int count = -5;
        try {
            RandomStringUtils.random(count);
        } catch (IllegalArgumentException e) {
            fail("Expected exception of type IllegalArgumentException not thrown.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCustomStringForRandomString() {
        String emptyCustomString = "";
        try {
            RandomStringUtils.random(5, emptyCustomString);
        } catch (IllegalArgumentException e) {
            fail("Expected exception of type IllegalArgumentException not thrown.");
        }
    }

    @Test
    public void testRandomStringWithRange() {
        int count = 10;
        int start = 48;
        int end = 58;
        String randomString = RandomStringUtils.random(count, start, end);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue("Randomly generated character should be within the specified range", randomChar >= start && randomChar <= end);
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testInvalidRangeForRandomString() {
        int count = 10;
        int start = 48;
        int end = 47;
        try {
            RandomStringUtils.random(count, start, end);
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Expected exception of type ArrayIndexOutOfBoundsException not thrown.");
        }
    }

    @Test
    public void testRandomStringWithLettersAndNumbers() {
        int count = 10;
        boolean letters = true;
        boolean numbers = true;
        String randomString = RandomStringUtils.random(count, letters, numbers);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue("Randomly generated character should be alphanumeric", Character.isAlphabetic(randomChar) || Character.isDigit(randomChar));
        }
    }

    @Test
    public void testRandomStringWithOnlyLetters() {
        int count = 10;
        boolean letters = true;
        boolean numbers = false;
        String randomString = RandomStringUtils.random(count, letters, numbers);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue("Randomly generated character should be alphabetic", Character.isAlphabetic(randomChar));
        }
    }

    @Test
    public void testRandomStringWithOnlyNumbers() {
        int count = 10;
        boolean letters = false;
        boolean numbers = true;
        String randomString = RandomStringUtils.random(count, letters, numbers);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue("Randomly generated character should be numeric", Character.isDigit(randomChar));
        }
    }

    @Test
    public void testRandomStringWithSurrogates() {
        int count = 10;
        String result = RandomStringUtils.random(count);
        int surrogateCount = 0;
        for (int i = 0; i < count; i++) {
            char c = result.charAt(i);
            if (c >= 0x55296 && c <= 0x57343) {
                surrogateCount++;
            }
        }
        assertTrue("At least one surrogate pair should be present in the generated string", surrogateCount > 0);
    }
}