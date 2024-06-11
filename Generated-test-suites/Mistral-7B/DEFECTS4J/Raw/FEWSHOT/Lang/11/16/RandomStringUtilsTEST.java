package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10);
            assertEquals("Length should be 10", 10, randomString.length());
            assertNotNull("String should not be null", randomString);
        }
    }

    @Test
    public void testRandomAscii() {
        for (int i = 0; i < 10; i++) {
            String randomAsciiString = RandomStringUtils.randomAscii(10);
            assertEquals("Length should be 10", 10, randomAsciiString.length());
            assertNotNull("String should not be null", randomAsciiString);
            for (char c : randomAsciiString.toCharArray()) {
                assertTrue("Character should be ASCII", (c >= 32 && c <= 126));
            }
        }
    }

    @Test
    public void testRandomAlphabetic() {
        for (int i = 0; i < 10; i++) {
            String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
            assertEquals("Length should be 10", 10, randomAlphabeticString.length());
            assertNotNull("String should not be null", randomAlphabeticString);
            for (char c : randomAlphabeticString.toCharArray()) {
                assertTrue("Character should be alphabetic", Character.isAlphabetic(c));
            }
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        for (int i = 0; i < 10; i++) {
            String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
            assertEquals("Length should be 10", 10, randomAlphanumericString.length());
            assertNotNull("String should not be null", randomAlphanumericString);
            for (char c : randomAlphanumericString.toCharArray()) {
                assertTrue("Character should be alphanumeric", Character.isAlphabetic(c) || Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomNumeric() {
        for (int i = 0; i < 10; i++) {
            String randomNumericString = RandomStringUtils.randomNumeric(10);
            assertEquals("Length should be 10", 10, randomNumericString.length());
            assertNotNull("String should not be null", randomNumericString);
            for (char c : randomNumericString.toCharArray()) {
                assertTrue("Character should be numeric", Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomWithRange() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random("a", "z", 10);
            assertEquals("Length should be 10", 10, randomString.length());
            assertNotNull("String should not be null", randomString);
            for (char c : randomString.toCharArray()) {
                assertTrue("Character should be alphabetic", Character.isAlphabetic(c));
            }
        }
    }

    @Test
    public void testRandomWithCharsArray() {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*()_+={}[]|\\:;\"'<>,.?/";
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, chars.toCharArray());
            assertEquals("Length should be 10", 10, randomString.length());
            assertNotNull("String should not be null", randomString);
            for (char c : randomString.toCharArray()) {
                assertTrue("Character should be in the chars array", chars.indexOf(c) != -1);
            }
        }
    }

    @Test
    public void testRandomWithNullCharsArray() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, (char[]) null);
            assertEquals("Length should be 10", 10, randomString.length());
            assertNotNull("String should not be null", randomString);
            // All characters should be printable ASCII
            for (char c : randomString.toCharArray()) {
                assertTrue("Character should be printable ASCII", Character.isISOControl(c) == false);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharsArray() {
        RandomStringUtils.random(10, (char[]) null);
    }
}