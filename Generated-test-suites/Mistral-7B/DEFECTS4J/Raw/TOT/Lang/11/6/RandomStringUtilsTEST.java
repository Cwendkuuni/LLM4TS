package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link RandomStringUtils}
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Test random string creation with default settings
        String randomString = RandomStringUtils.random(10);
        assertTrue(randomString.length() == 10);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)) || Character.isWhitespace(randomString.charAt(i)) || Character.isISOControl(randomString.charAt(i)) || Character.isHighSurrogate(randomString.charAt(i)));
        }

        // Test random string creation with length zero
        String randomStringZeroLength = RandomStringUtils.random(0);
        assertEquals("", randomStringZeroLength);

        // Test random string creation with negative length
        try {
            RandomStringUtils.random(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test random string creation with custom character sets
        String randomStringLetters = RandomStringUtils.randomAlphabetic(5);
        String randomStringAlphanumeric = RandomStringUtils.randomAlphanumeric(5);
        String randomStringNumbers = RandomStringUtils.randomNumeric(5);
        String randomStringAscii = RandomStringUtils.randomAscii(5);

        for (int i = 0; i < randomStringLetters.length(); i++) {
            assertTrue(Character.isAlphabetic(randomStringLetters.charAt(i)));
        }
        for (int i = 0; i < randomStringAlphanumeric.length(); i++) {
            assertTrue(Character.isAlphabetic(randomStringAlphanumeric.charAt(i)) || Character.isDigit(randomStringAlphanumeric.charAt(i)));
        }
        for (int i = 0; i < randomStringNumbers.length(); i++) {
            assertTrue(Character.isDigit(randomStringNumbers.charAt(i)));
        }
        for (int i = 0; i < randomStringAscii.length(); i++) {
            assertTrue(Character.isISOControl(randomStringAscii.charAt(i)) || Character.isWhitespace(randomStringAscii.charAt(i)) || Character.isAlphabetic(randomStringAscii.charAt(i)) || Character.isDigit(randomStringAscii.charAt(i)));
        }

        // Test random string creation with custom character sets and custom random seed
        Random randomSeed = new Random(12345);
        String randomStringCustomSeedLetters = RandomStringUtils.random(5, true, false, randomSeed);
        String randomStringCustomSeedAlphanumeric = RandomStringUtils.random(5, true, true, randomSeed);
        String randomStringCustomSeedNumbers = RandomStringUtils.random(5, false, true, randomSeed);
        String randomStringCustomSeedAscii = RandomStringUtils.random(5, 32, 126, randomSeed);

        for (int i = 0; i < randomStringCustomSeedLetters.length(); i++) {
            assertTrue(Character.isAlphabetic(randomStringCustomSeedLetters.charAt(i)));
        }
        for (int i = 0; i < randomStringCustomSeedAlphanumeric.length(); i++) {
            assertTrue(Character.isAlphabetic(randomStringCustomSeedAlphanumeric.charAt(i)) || Character.isDigit(randomStringCustomSeedAlphanumeric.charAt(i)));
        }
        for (int i = 0; i < randomStringCustomSeedNumbers.length(); i++) {
            assertTrue(Character.isDigit(randomStringCustomSeedNumbers.charAt(i)));
        }
        for (int i = 0; i < randomStringCustomSeedAscii.length(); i++) {
            assertTrue(Character.isISOControl(randomStringCustomSeedAscii.charAt(i)) || Character.isWhitespace(randomStringCustomSeedAscii.charAt(i)) || Character.isAlphabetic(randomStringCustomSeedAscii.charAt(i)) || Character.isDigit(randomStringCustomSeedAscii.charAt(i)));
        }
    }

    @Test
    public void testRandomSurrogate() {
        // Test random string creation with surrogate characters
        String randomStringSurrogate = RandomStringUtils.random(10, 0, 0x10FFFF, true, true);
        for (int i = 0; i < randomStringSurrogate.length(); i++) {
            if (Character.isHighSurrogate(randomStringSurrogate.charAt(i))) {
                assertTrue(i < randomStringSurrogate.length() - 1);
                assertTrue(Character.isLowSurrogate(randomStringSurrogate.charAt(i + 1)));
            } else if (Character.isLowSurrogate(randomStringSurrogate.charAt(i))) {
                assertTrue(i > 0);
                assertTrue(Character.isHighSurrogate(randomStringSurrogate.charAt(i - 1)));
            } else {
                assertTrue(Character.isAlphabetic(randomStringSurrogate.charAt(i)) || Character.isDigit(randomStringSurrogate.charAt(i)));
            }
        }
    }

    @Test
    public void testRandomNull() {
        // Test null input
        try {
            RandomStringUtils.random(0, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            RandomStringUtils.randomAlphabetic(0, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            RandomStringUtils.randomAlphanumeric(0, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            RandomStringUtils.randomNumeric(0, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            RandomStringUtils.random(0, 0, 1, true, true, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testRandomEmpty() {
        // Test empty character sets
        String randomStringEmptyLetters = RandomStringUtils.randomAlphabetic(0);
        String randomStringEmptyAlphanumeric = RandomStringUtils.randomAlphanumeric(0);
        String randomStringEmptyNumbers = RandomStringUtils.randomNumeric(0);
        String randomStringEmptyAscii = RandomStringUtils.randomAscii(0);

        assertEquals("", randomStringEmptyLetters);
        assertEquals("", randomStringEmptyAlphanumeric);
        assertEquals("", randomStringEmptyNumbers);
        assertEquals("", randomStringEmptyAscii);
    }

    @Test
    public void testRandomInvalid() {
        // Test invalid input
        try {
            RandomStringUtils.random(-1, 0, 0, true, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            RandomStringUtils.random(0, 1, 1, true, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            RandomStringUtils.random(0, 0, -1, true, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            RandomStringUtils.random(0, 0, 0, false, false, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            RandomStringUtils.random(0, 0, 0, true, true, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            RandomStringUtils.random(0, 0, 0, true, true, new char[]{}, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            RandomStringUtils.random(0, 0, 0, true, true, null, new Random());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            RandomStringUtils.random(0, 0, 0, true, true, new char[]{'a'}, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}