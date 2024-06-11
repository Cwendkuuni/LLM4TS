package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomAsciiString.length());
        assertTrue(randomAsciiString.matches("[ -~]+")); // Check if only ASCII printable characters are present
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomAlphabeticString.length());
        assertTrue(randomAlphabeticString.matches("[A-Za-z]+")); // Check if only alphabetic characters are present
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomAlphanumericString.length());
        assertTrue(randomAlphanumericString.matches("[A-Za-z0-9]+")); // Check if only alphanumeric characters are present
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomNumericString.length());
        assertTrue(randomNumericString.matches("[0-9]+")); // Check if only numeric characters are present
    }

    @Test
    public void testRandomWithCustomCharacters() {
        String customChars = "abcdef";
        String randomCustomString = RandomStringUtils.random(10, customChars.toCharArray());
        assertEquals(10, randomCustomString.length());
        assertTrue(randomCustomString.matches("[abcdef]+")); // Check if only custom characters are present
    }

    @Test
    public void testRandomWithInvalidInput() {
        try {
            String randomString = RandomStringUtils.random(-5);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }
}