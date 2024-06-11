package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test case for RandomStringUtils class.
 */
public class RandomStringUtilsTest {

    // Test for random method
    @Test
    public void testRandom() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Should contain random character", isAsciiPrintable(randomString.charAt(i)));
        }
    }

    // Test for randomAscii method
    @Test
    public void testRandomAscii() {
        int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals("Length should match", count, randomAsciiString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Should contain ASCII printable character", isAsciiPrintable(randomAsciiString.charAt(i)));
            assertTrue("Should be between 32 and 126", randomAsciiString.charAt(i) >= 32 && randomAsciiString.charAt(i) <= 126);
        }
    }

    // Test for randomAlphabetic method
    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Length should match", count, randomAlphabeticString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Should be alphabetic character", isAlphabetic(randomAlphabeticString.charAt(i)));
        }
    }

    // Test for randomAlphanumeric method
    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Length should match", count, randomAlphanumericString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Should be alphanumeric character", isAlphanumeric(randomAlphanumericString.charAt(i)));
        }
    }

    // Test for randomNumeric method
    @Test
    public void testRandomNumeric() {
        int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertEquals("Length should match", count, randomNumericString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Should be numeric character", isDigit(randomNumericString.charAt(i)));
        }
    }

    // Test for random method with custom start and end characters
    @Test
    public void testRandomWithCustomStartAndEnd() {
        int count = 10;
        String start = "a";
        String end = "z";
        String randomString = RandomStringUtils.random(count, start.charAt(0), end.charAt(end.length() - 1), false, false, null, RANDOM);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Should be between start and end characters", randomString.charAt(i) >= start.charAt(0) && randomString.charAt(i) <= end.charAt(end.length() - 1));
        }
    }

    // Test for random method with custom set of characters
    @Test
    public void testRandomWithCustomSet() {
        int count = 10;
        String set = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(count, set.toCharArray(), RANDOM);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertTrue("Should be in the custom set of characters", set.indexOf(randomString.charAt(i)) > -1);
        }
    }

    // Test for random method with a null set of characters
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNullSet() {
        int count = 10;
        String randomString = RandomStringUtils.random(count, null, RANDOM);
    }

    // Test for random method with a count less than 0
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        int count = -10;
        RandomStringUtils.random(count);
    }

    // Test for randomAscii method with a count less than 0
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAsciiWithNegativeCount() {
        int count = -10;
        RandomStringUtils.randomAscii(count);
    }

    // Test for randomAlphabetic method with a count less than 0
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphabeticWithNegativeCount() {
        int count = -10;
        RandomStringUtils.randomAlphabetic(count);
    }

    // Test for randomAlphanumeric method with a count less than 0
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphanumericWithNegativeCount() {
        int count = -10;
        RandomStringUtils.randomAlphanumeric(count);
    }

    // Test for randomNumeric method with a count less than 0
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNumericWithNegativeCount() {
        int count = -10;
        RandomStringUtils.randomNumeric(count);
    }

    private boolean isAsciiPrintable(char c) {
        return (c >= 32 && c <= 126);
    }

    private boolean isAlphabetic(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }
}