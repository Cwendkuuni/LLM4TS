package org.apache.commons.lang3;
import static org.junit.Assert.*;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomAsciiString);
        assertEquals(5, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue((int)c >= 32 && (int)c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomAlphabeticString);
        assertEquals(5, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomAlphanumericString);
        assertEquals(5, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomNumericString);
        assertEquals(5, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithChars() {
        String randomStringWithChars = RandomStringUtils.random(5, "abcd");
        assertNotNull(randomStringWithChars);
        assertEquals(5, randomStringWithChars.length());
        for (char c : randomStringWithChars.toCharArray()) {
            assertTrue(c == 'a' || c == 'b' || c == 'c' || c == 'd');
        }
    }

    @Test
    public void testRandomWithCharRange() {
        String randomStringWithCharRange = RandomStringUtils.random(5, 'a', 'z', false, false);
        assertNotNull(randomStringWithCharRange);
        assertEquals(5, randomStringWithCharRange.length());
        for (char c : randomStringWithCharRange.toCharArray()) {
            assertTrue((int)c >= 97 && (int)c <= 122);
        }
    }

    @Test
    public void testRandomInvalidCount() {
        try {
            RandomStringUtils.random(-5);
            fail("Expected IllegalArgumentException for negative count");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}