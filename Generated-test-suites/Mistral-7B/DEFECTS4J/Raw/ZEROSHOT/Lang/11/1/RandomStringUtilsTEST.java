package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test method for {@link RandomStringUtils#random(int)}.
     */
    @Test
    public void testRandom() {
        int length = 10;
        String randomString = RandomStringUtils.random(length);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        int length = 10;
        String randomString = RandomStringUtils.randomAscii(length);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}.
     */
    @Test
    public void testRandomAlphabetic() {
        int length = 10;
        String randomString = RandomStringUtils.randomAlphabetic(length);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());

        for (char c : randomString.toCharArray()) {
            assertEquals(true, Character.isAlphabetic(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        int length = 10;
        String randomString = RandomStringUtils.randomAlphanumeric(length);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());

        for (char c : randomString.toCharArray()) {
            assertEquals(true, Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}.
     */
    @Test
    public void testRandomNumeric() {
        int length = 10;
        String randomString = RandomStringUtils.randomNumeric(length);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());

        for (char c : randomString.toCharArray()) {
            assertEquals(true, Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, boolean, boolean)}.
     */
    @Test
    public void testRandomWithFlags() {
        int length = 10;
        String randomString = RandomStringUtils.random(length, true, true);

        assertNotNull(randomString);
        assertEquals(length, randomString.length());

        for (char c : randomString.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                assertEquals(true, Character.isLetter(c));
            } else if (Character.isDigit(c)) {
                assertEquals(true, Character.isDigit(c));
            } else {
                // Other characters are possible too, but not tested here
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithCustomRandom() {
        int length = 10;
        int min = 32;
        int max = 127;
        Random customRandom = new Random(123);
        String randomString = RandomStringUtils.random(length, min, max, true, true, null, customRandom);

        assertNotNull(randomString);
        assertEquals(length, randomString.length());

        for (char c : randomString.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                assertEquals(true, Character.isLetter(c));
            } else if (Character.isDigit(c)) {
                assertEquals(true, Character.isDigit(c));
            } else {
                int ascii = (int) c;
                assertEquals(true, ascii >= min && ascii <= max);
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, String)}.
     */
    @Test
    public void testRandomWithString() {
        int length = 10;
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(length, chars);

        assertNotNull(randomString);
        assertEquals(length, randomString.length());

        for (char c : randomString.toCharArray()) {
            assertEquals(true, chars.indexOf(c) >= 0);
        }
    }
}