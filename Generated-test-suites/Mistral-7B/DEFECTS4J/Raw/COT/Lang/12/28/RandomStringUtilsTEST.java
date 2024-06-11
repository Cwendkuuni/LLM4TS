package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

import org.junit.Test;

/**
 * Test class for RandomStringUtils
 */
public class RandomStringUtilsTest {

    /**
     * Test method for {@link RandomStringUtils#random(int)}
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(c >= 32 && c <= 126);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, boolean, boolean)}
     */
    @Test
    public void testRandomBoolean() {
        String randomString = RandomStringUtils.random(5, true, false);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(c));
        }

        String randomString2 = RandomStringUtils.random(5, false, true);
        assertNotNull(randomString2);
        assertEquals(5, randomString2.length());
        for (int i = 0; i < randomString2.length(); i++) {
            char c = randomString2.charAt(i);
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     */
    @Test
    public void testRandomCharArray() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomString = RandomStringUtils.random(5, chars.toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(chars.indexOf(c) != -1);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} with empty char array
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharArray() {
        String[] emptyChars = new String[0];
        RandomStringUtils.random(5, emptyChars[0].toCharArray(), new Random());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} with null char array
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNullCharArray() {
        RandomStringUtils.random(5, null, new Random());
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} with negative count
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        RandomStringUtils.random(-1, chars.toCharArray(), new Random());
    }

}