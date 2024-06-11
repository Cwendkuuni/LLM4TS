package org.apache.commons.lang3;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Randomly generated string of length 10", count, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        int count = 5;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals("Randomly generated ASCII string of length 5", count, randomAsciiString.length());
    }

    @Test
    public void testRandomAlphabeticString() {
        int count = 8;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Randomly generated alphabetic string of length 8", count, randomAlphabeticString.length());
    }

    @Test
    public void testRandomAlphanumericString() {
        int count = 6;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Randomly generated alphanumeric string of length 6", count, randomAlphanumericString.length());
    }

    @Test
    public void testRandomNumericString() {
        int count = 15;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertEquals("Randomly generated numeric string of length 15", count, randomNumericString.length());
    }

    @Test
    public void testRandomStringWithCustomCharacterSet() {
        int count = 12;
        String customCharacterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomStringWithCustomCharacterSet = RandomStringUtils.random(count, customCharacterSet.toCharArray());
        assertEquals("Randomly generated string with custom character set of length 12", count, randomStringWithCustomCharacterSet.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNegativeCount() {
        int count = -5;
        RandomStringUtils.random(count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptyCharacterSet() {
        char[] emptyCharacterSet = new char[0];
        RandomStringUtils.random(5, emptyCharacterSet);
    }

    @Test
    public void testRandomStringWithHighSurrogates() {
        int count = 2;
        String randomStringWithHighSurrogates = RandomStringUtils.random(count, 0, 0, true, true);
        assertEquals("Randomly generated string with high surrogates of length 2", count, randomStringWithHighSurrogates.length());
    }

    @Test
    public void testRandomStringWithPrivateHighSurrogates() {
        int count = 1;
        String randomStringWithPrivateHighSurrogates = RandomStringUtils.random(count, 0, 0, true, true);
        assertNull("Randomly generated string should not contain private high surrogates", randomStringWithPrivateHighSurrogates);
    }
}