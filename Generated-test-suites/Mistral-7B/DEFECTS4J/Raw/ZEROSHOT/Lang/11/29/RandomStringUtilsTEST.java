package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test file for RandomStringUtils class
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
        for (int i = 0; i < randomAsciiString.length(); i++) {
            char c = randomAsciiString.charAt(i);
            assertTrue(Character.isISOControl(c) || Character.isAlphabetic(c) || Character.isDigit(c) || Character.isWhitespace(c) || Character.isSpaceChar(c) || Character.isGraphic(c));
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            char c = randomAlphabeticString.charAt(i);
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char c = randomAlphanumericString.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            char c = randomNumericString.charAt(i);
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomStringWithCustomChars() {
        String customChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomStringWithCustomChars = RandomStringUtils.random(10, customChars.toCharArray());
        assertNotNull(randomStringWithCustomChars);
        assertEquals(10, randomStringWithCustomChars.length());
        for (int i = 0; i < randomStringWithCustomChars.length(); i++) {
            char c = randomStringWithCustomChars.charAt(i);
            assertTrue(customChars.indexOf(c) != -1);
        }
    }

    @Test
    public void testRandomStringWithStartAndEndCharacters() {
        String randomStringWithStartAndEndChars = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 10, 'X', 'Z');
        assertNotNull(randomStringWithStartAndEndChars);
        assertEquals(10, randomStringWithStartAndEndChars.length());
        for (int i = 0; i < randomStringWithStartAndEndChars.length(); i++) {
            char c = randomStringWithStartAndEndChars.charAt(i);
            assertTrue(c >= 'X' && c <= 'Z' || c >= 'A' && c <= 'Z' || c >= 'A' && c <= 'Z');
        }
    }

    @Test
    public void testRandomStringWithStartAndEndCharactersOutOfRange() {
        String randomStringWithInvalidRange = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 10, 'Z', 'A');
        assertEquals("", randomStringWithInvalidRange);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptyCustomChars() {
        RandomStringUtils.random(10, "");
    }
}