package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandomStringCreation() {
        // Test random string creation with default options
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);

        // Test random string creation with specific options
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomAsciiString);
        assertTrue(randomAsciiString.length() == 5);
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }

        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(7);
        assertNotNull(randomAlphabeticString);
        assertTrue(randomAlphabeticString.length() == 7);
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }

        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(15);
        assertNotNull(randomAlphanumericString);
        assertTrue(randomAlphanumericString.length() == 15);
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue((Character.isAlphabetic(c) || Character.isDigit(c)));
        }

        String randomNumericString = RandomStringUtils.randomNumeric(12);
        assertNotNull(randomNumericString);
        assertTrue(randomNumericString.length() == 12);
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }

        // Test random string creation with custom character set
        String customSetString = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 15);
        assertNotNull(customSetString);
        assertTrue(customSetString.length() == 15);
        for (char c : customSetString.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                assertTrue(Character.isUpperCase(c) || Character.isLowerCase(c));
            } else {
                assertTrue(Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomStringCreationWithLimitedCharacterSet() {
        // Test random string creation with limited character set
        String limitedSetString = RandomStringUtils.random(10, 'A', 'Z');
        assertNotNull(limitedSetString);
        assertTrue(limitedSetString.length() == 10);
        for (char c : limitedSetString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) && Character.isUpperCase(c));
        }

        limitedSetString = RandomStringUtils.random(5, '0', '9');
        assertNotNull(limitedSetString);
        assertTrue(limitedSetString.length() == 5);
        for (char c : limitedSetString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomStringCreationWithEmptyCharacterSet() {
        // Test random string creation with empty character set
        try {
            RandomStringUtils.random(5, (String)null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            RandomStringUtils.random(10, (char[])null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRandomStringCreationWithNegativeCount() {
        // Test random string creation with negative count
        try {
            RandomStringUtils.random(-5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRandomStringCreationWithStartAndEndValuesOutOfRange() {
        // Test random string creation with start and end values out of range
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        try {
            RandomStringUtils.random(5, 53, 52, false, false, chars.toCharArray(), RANDOM);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected
        }

        try {
            RandomStringUtils.random(10, 53, 1, false, false, chars.toCharArray(), RANDOM);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRandomStringCreationWithLettersAndNumbers() {
        // Test random string creation with letters and numbers
        String randomString = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 12);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 12);
        for (char c : randomString.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
            } else {
                assertTrue(Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomStringCreationWithOnlyLetters() {
        // Test random string creation with only letters
        String randomString = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 8);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 8);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomStringCreationWithOnlyNumbers() {
        // Test random string creation with only numbers
        String randomString = RandomStringUtils.random("0123456789", 7);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 7);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomStringCreationWithHighSurrogates() {
        // Test random string creation with high surrogates
        String randomString = RandomStringUtils.random(2, false, false, null, RANDOM);
        assertNull(randomString);

        randomString = RandomStringUtils.random(2, true, false, null, RANDOM);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 2);
        for (char c : randomString.toCharArray()) {
            if (c >= 55296 && c <= 56191) {
                assertTrue(randomString.length() > 1);
                char nextChar = randomString.charAt(1);
                assertTrue(nextChar >= 56320 && nextChar <= 57343);
            } else {
                assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '\u0000');
            }
        }
    }

    @Test
    public void testRandomStringCreationWithPrivateHighSurrogates() {
        // Test random string creation with private high surrogates
        String randomString = RandomStringUtils.random(2, true, true, null, RANDOM);
        assertNull(randomString);
    }
}