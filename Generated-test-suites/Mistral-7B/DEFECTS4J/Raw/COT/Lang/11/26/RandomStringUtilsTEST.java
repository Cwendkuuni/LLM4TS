package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assert(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCountRandomString() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomStringWithRange() {
        String randomStringWithRange = RandomStringUtils.random(10, 33, 124);
        assertNotNull(randomStringWithRange);
        assertEquals(10, randomStringWithRange.length());
        for (char c : randomStringWithRange.toCharArray()) {
            assert(c >= 33 && c <= 124);
        }
    }

    @Test
    public void testRandomStringWithEmptyList() {
        String randomStringWithEmptyList = RandomStringUtils.random(10, new char[0]);
        assertNotNull(randomStringWithEmptyList);
        assertEquals(10, randomStringWithEmptyList.length());
        for (char c : randomStringWithEmptyList.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c) || c == 0);
        }
    }

    @Test
    public void testRandomStringWithNullChars() {
        String randomStringWithNullChars = RandomStringUtils.random(10, null);
        assertNotNull(randomStringWithNullChars);
        assertEquals(10, randomStringWithNullChars.length());
        for (char c : randomStringWithNullChars.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c) || c == 0);
        }
    }

    @Test
    public void testRandomStringWithCustomCharSet() {
        String randomStringWithCustomCharSet = RandomStringUtils.random(10, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        assertNotNull(randomStringWithCustomCharSet);
        assertEquals(10, randomStringWithCustomCharSet.length());
        for (char c : randomStringWithCustomCharSet.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }
}