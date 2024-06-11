package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Test empty string
        assertEquals("", RandomStringUtils.random(0));
        
        // Test null input
        try {
            RandomStringUtils.random(null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        // Test random alphanumeric string
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char c = randomAlphanumericString.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
        
        // Test random alphabetic string
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            char c = randomAlphabeticString.charAt(i);
            assertTrue(Character.isAlphabetic(c));
        }
        
        // Test random numeric string
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            char c = randomNumericString.charAt(i);
            assertTrue(Character.isDigit(c));
        }
        
        // Test random string with custom length and character set (ASCII printable)
        String randomString = RandomStringUtils.random(10, 0, Integer.MAX_VALUE, true, true, null, new Random());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || Character.isWhitespace(c) || c == '\'');
        }
        
        // Test random string with custom length and character set (custom characters)
        String customCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomStringWithCustomCharacters = RandomStringUtils.random(10, customCharacters.toCharArray(), new Random());
        assertNotNull(randomStringWithCustomCharacters);
        assertEquals(10, randomStringWithCustomCharacters.length());
        for (int i = 0; i < randomStringWithCustomCharacters.length(); i++) {
            char c = randomStringWithCustomCharacters.charAt(i);
            assertTrue(customCharacters.indexOf(c) != -1);
        }
    }

    @Test
    public void testRandomWithHighAndLowSurrogates() {
        // Test random string with high and low surrogates
        String randomStringWithSurrogates = RandomStringUtils.random(10, new Random());
        assertNotNull(randomStringWithSurrogates);
        assertEquals(10, randomStringWithSurrogates.length());
        boolean hasSurrogatePair = false;
        for (int i = 0; i < randomStringWithSurrogates.length() - 1; i++) {
            char c1 = randomStringWithSurrogates.charAt(i);
            char c2 = randomStringWithSurrogates.charAt(i + 1);
            if (Character.isHighSurrogate(c1) && Character.isLowSurrogate(c2)) {
                hasSurrogatePair = true;
                break;
            }
        }
        assertTrue(hasSurrogatePair);
    }

    @Test
    public void testRandomWithInvalidInput() {
        // Test invalid input for length
        try {
            RandomStringUtils.random(-1, null, null, false, false, null, new Random());
            fail("expected IllegalArgumentException with message 'Requested random string length is less than 0'");
        } catch (IllegalArgumentException e) {
            assertEquals("Requested random string length is less than 0", e.getMessage());
        }
        
        // Test invalid input for character set
        String emptyString = "";
        try {
            RandomStringUtils.random(1, emptyString.toCharArray(), new Random());
            fail("expected IllegalArgumentException with message 'The provided String is empty'");
        } catch (IllegalArgumentException e) {
            assertEquals("The provided String is empty", e.getMessage());
        }
    }
}