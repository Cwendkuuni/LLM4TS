package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(15);
        assertNotNull(randomAlphabeticString);
        assertEquals(15, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(20);
        assertNotNull(randomAlphanumericString);
        assertEquals(20, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(8);
        assertNotNull(randomNumericString);
        assertEquals(8, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }
    
    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomWithCustomChars() {
        String chars = "abc";
        String randomString = RandomStringUtils.random(5, chars);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(chars.contains(String.valueOf(c)));
        }
    }

    @Test
    public void testRandomWithEmptyChars() {
        try {
            RandomStringUtils.random(5, "");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The chars array must not be empty", e.getMessage());
        }
    }

    @Test
    public void testRandomWithNegativeCount() {
        try {
            RandomStringUtils.random(-5);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Requested random string length -5 is less than 0.", e.getMessage());
        }
    }
    
}
