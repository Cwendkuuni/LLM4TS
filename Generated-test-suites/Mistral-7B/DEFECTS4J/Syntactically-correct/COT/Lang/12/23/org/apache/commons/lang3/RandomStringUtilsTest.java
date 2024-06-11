package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

import org.junit.Test;

/**
 * Tests for {@link RandomStringUtils} class
 *
 * @author John Doe
 */
public class RandomStringUtilsTest {

    /**
     * Test for {@link RandomStringUtils#random(int)} method
     */
    @Test
    public void testRandomInt() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    /**
     * Test for {@link RandomStringUtils#randomAscii(int)} method
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphabetic(int)} method
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphanumeric(int)} method
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomNumeric(int)} method
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, boolean, boolean)} method
     */
    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(5, true, true);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method
     */
    @Test
    public void testRandomWithCustomChars() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(5, 0, chars.length(), true, true, chars.toCharArray(), new Random());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
            assertTrue(chars.indexOf(c) >= 0);
        }
    }

    /**
     * Test for edge case when count is zero
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithZeroCount() {
        RandomStringUtils.random(0);
    }

    /**
     * Test for edge case when chars array is empty
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        RandomStringUtils.random(5, new char[0]);
    }
}