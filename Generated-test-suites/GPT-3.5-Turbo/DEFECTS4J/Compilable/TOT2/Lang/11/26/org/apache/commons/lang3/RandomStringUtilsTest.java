package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
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
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        assertTrue(randomAlphabeticString.matches("^[a-zA-Z]+$"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        assertTrue(randomAlphanumericString.matches("^[a-zA-Z0-9]+$"));
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        assertTrue(randomNumericString.matches("^[0-9]+$"));
    }

    // Test edge case with special characters
    @Test
    public void testRandomWithSpecialChars() {
        String specialChars = "!@#";
        String randomString = RandomStringUtils.random(5, specialChars);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(specialChars.contains(String.valueOf(c)));
        }
    }

    // Test error scenario with negative count
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }
    
    // Test error scenario with empty chars array
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharsArray() {
        char[] emptyChars = {};
        RandomStringUtils.random(5, emptyChars);
    }

}