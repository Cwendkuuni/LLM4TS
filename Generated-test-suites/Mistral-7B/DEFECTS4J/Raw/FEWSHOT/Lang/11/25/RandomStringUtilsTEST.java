package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Test empty string
        assertEquals("", RandomStringUtils.random(0));
        
        // Test null input
        assertNull(RandomStringUtils.random(null));
        
        // Test empty character array
        assertNull(RandomStringUtils.random(0, (char[]) null));
        
        // Test random string of a given length with all characters
        String randomString = RandomStringUtils.random(5);
        assertTrue(randomString.length() == 5);
        
        // Test random string of a given length with ascii characters
        String asciiRandomString = RandomStringUtils.randomAscii(5);
        assertTrue(asciiRandomString.length() == 5);
        
        // Test random string of a given length with alphabetic characters
        String alphabeticRandomString = RandomStringUtils.randomAlphabetic(5);
        assertTrue(alphabeticRandomString.length() == 5);
        
        // Test random string of a given length with alphanumeric characters
        String alphanumericRandomString = RandomStringUtils.randomAlphanumeric(5);
        assertTrue(alphanumericRandomString.length() == 5);
        
        // Test random string of a given length with numeric characters
        String numericRandomString = RandomStringUtils.randomNumeric(5);
        assertTrue(numericRandomString.length() == 5);
        
        // Test random string of a given length with specified character set
        String customRandomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 5);
        assertTrue(customRandomString.length() == 5);
        
        // Test random string of a given length with specified character set range
        String rangeRandomString = RandomStringUtils.random(5, 'A', 'Z');
        assertTrue(rangeRandomString.length() == 5);
        
        // Test random string of a given length with specified character set and custom random source
        String customRandomSourceRandomString = RandomStringUtils.random(5, 'A', 'Z', new Random(12345));
        assertTrue(customRandomSourceRandomString.length() == 5);
    }

    @Test
    public void testRandomSurrogates() {
        // Test random string with surrogates
        String randomStringWithSurrogates = RandomStringUtils.random(5, true, true);
        assertTrue(randomStringWithSurrogates.length() >= 3);
        int count = 0;
        for (char c : randomStringWithSurrogates.toCharArray()) {
            if (Character.isHighSurrogate(c)) {
                count++;
            }
            if (Character.isLowSurrogate(c)) {
                count++;
            }
            if (Character.isSupplementaryCharacter(c)) {
                assertFalse(count % 2 == 0);
                count = 0;
            }
        }
        assertTrue(count % 2 == 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharacterArray() {
        RandomStringUtils.random(1, (char[]) null);
    }
}