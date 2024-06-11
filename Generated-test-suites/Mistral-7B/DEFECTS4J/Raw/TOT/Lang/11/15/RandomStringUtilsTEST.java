package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    /**
     * Test for {@link RandomStringUtils#random(int)} method.
     */
    @Test
    public void testRandom() {
        int length = 10;
        String randomString = RandomStringUtils.random(length);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue((randomChar >= 0 && randomChar <= 127) || (randomChar >= 56320 && randomChar <= 57343));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAscii(int)} method.
     */
    @Test
    public void testRandomAscii() {
        int length = 10;
        String randomString = RandomStringUtils.randomAscii(length);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue(randomChar >= 32 && randomChar <= 126);
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphabetic(int)} method.
     */
    @Test
    public void testRandomAlphabetic() {
        int length = 10;
        String randomString = RandomStringUtils.randomAlphabetic(length);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(randomChar));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphanumeric(int)} method.
     */
    @Test
    public void testRandomAlphanumeric() {
        int length = 10;
        String randomString = RandomStringUtils.randomAlphanumeric(length);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(randomChar) || Character.isDigit(randomChar));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomNumeric(int)} method.
     */
    @Test
    public void testRandomNumeric() {
        int length = 10;
        String randomString = RandomStringUtils.randomNumeric(length);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue(Character.isDigit(randomChar));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean)} method.
     */
    @Test
    public void testRandomWithRange() {
        int length = 10;
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", length);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(randomChar) || Character.isDigit(randomChar));
        }
    }
}