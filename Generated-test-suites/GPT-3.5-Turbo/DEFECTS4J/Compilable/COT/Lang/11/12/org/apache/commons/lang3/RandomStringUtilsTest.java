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
    }
    
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomAlphabeticString.length());
        assertTrue(randomAlphabeticString.matches("[a-zA-Z]+")); // Check if only alphabetic characters
    }
    
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomAlphanumericString.length());
        assertTrue(randomAlphanumericString.matches("[a-zA-Z0-9]+")); // Check if only alphanumeric characters
    }
    
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomNumericString.length());
        assertTrue(randomNumericString.matches("[0-9]+")); // Check if only numeric characters
    }
    
    @Test
    public void testRandomWithParams() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[a-zA-Z0-9]+")); // Check if only alphanumeric characters
    }
    
    @Test
    public void testRandomWithChars() {
        char[] chars = {'a', 'b', 'c'};
        String randomString = RandomStringUtils.random(10, chars);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[abc]+")); // Check if only characters from chars array
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomCharsArrayEmpty() {
        char[] chars = {};
        RandomStringUtils.random(10, chars);
    }
    
    @Test
    public void testRandomWithValueOutOfRange() {
        String randomString = RandomStringUtils.random(10, 128, 255, false, false);
        assertEquals(10, randomString.length());
        assertFalse(randomString.matches("[\\x00-\\x7F]+")); // Check if characters are outside ASCII range
    }
}