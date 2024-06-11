package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test cases for RandomStringUtils class
 */
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
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
    }

    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
    }

    @Test
    public void testRandomStringWithRangeAndLetters() {
        String randomString = RandomStringUtils.random("abcd", 10, false, false);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char ch = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(ch) || Character.isWhitespace(ch) || Character.isDigit(ch) || ch == '\u0000');
        }
    }

    @Test
    public void testRandomStringWithRangeAndNumbers() {
        String randomString = RandomStringUtils.random("0123456789", 10, false, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char ch = randomString.charAt(i);
            assertTrue(Character.isDigit(ch) || ch == '\u0000');
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringEmptyCharsArray() {
        RandomStringUtils.random(new char[0]);
    }

    @Test
    public void testRandomStringWithCustomRandom() {
        Random random = new Random(123456789);
        String randomString = RandomStringUtils.random(5, new char[0], random);
        assertEquals("5z\u0001", randomString);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringOutOfRange() {
        String randomString = RandomStringUtils.random(11, "abcdefghijklmnopqrstuvwxyz", false, true, 'z', 'z');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringEmptyString() {
        RandomStringUtils.random(1, "");
    }
}