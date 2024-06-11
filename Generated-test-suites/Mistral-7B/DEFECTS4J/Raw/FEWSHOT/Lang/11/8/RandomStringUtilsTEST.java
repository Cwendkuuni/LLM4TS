package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertTrue(randomString.length() == 10);
        assertFalse(randomString.isEmpty());
        assertNotNull(randomString);
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertTrue(randomAsciiString.length() == 10);
        for (int i = 0; i < randomAsciiString.length(); i++) {
            char c = randomAsciiString.charAt(i);
            assertTrue(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertTrue(randomAlphabeticString.length() == 10);
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            char c = randomAlphabeticString.charAt(i);
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertTrue(randomAlphanumericString.length() == 10);
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char c = randomAlphanumericString.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertTrue(randomNumericString.length() == 10);
        for (int i = 0; i < randomNumericString.length(); i++) {
            char c = randomNumericString.charAt(i);
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomCustom() {
        String randomCustomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10);
        assertTrue(randomCustomString.length() == 10);
        for (int i = 0; i < randomCustomString.length(); i++) {
            char c = randomCustomString.charAt(i);
            assertTrue(randomCustomString.indexOf(c) != -1);
        }
    }

    @Test
    public void testRandomWithSeed() {
        String randomString1 = RandomStringUtils.random(10, new Random(1));
        String randomString2 = RandomStringUtils.random(10, new Random(1));
        assertEquals(randomString1, randomString2);
    }

    @Test
    public void testRandomWithCustomCharsetAndSeed() {
        String randomString1 = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10, new Random(1));
        String randomString2 = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10, new Random(1));
        assertEquals(randomString1, randomString2);
    }
}