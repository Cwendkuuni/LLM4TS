package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

import org.junit.Test;

/**
 * Test class for {@link RandomStringUtils}
 *
 * @author John Doe
 */
public class RandomStringUtilsTest {

    /**
     * Test method for {@link RandomStringUtils#random(int)}.
     */
    @Test
    public void testRandom() {
        int count = 5;
        String randomString = RandomStringUtils.random(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        int count = 5;
        String randomString = RandomStringUtils.randomAscii(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            int ascii = (int) randomString.charAt(i);
            assertTrue(ascii >= 32 && ascii <= 126);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}.
     */
    @Test
    public void testRandomAlphabetic() {
        int count = 5;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isLetter(randomString.charAt(i)));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        int count = 5;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue((Character.isLetter(c) || Character.isDigit(c)) || (c == '_' || c == '-'));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}.
     */
    @Test
    public void testRandomNumeric() {
        int count = 5;
        String randomString = RandomStringUtils.randomNumeric(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, boolean, boolean)}.
     */
    @Test
    public void testRandomWithStartAndEndAndLettersAndNumbers() {
        int count = 5;
        int start = 65;
        int end = 90;
        boolean letters = true;
        boolean numbers = true;
        String randomString = RandomStringUtils.random(count, start, end, letters, numbers);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            if (letters) {
                assertTrue(Character.isLetter(c) || (c == '_' || c == '-'));
            }
            if (numbers) {
                assertTrue(Character.isDigit(c) || (c == '_' || c == '-'));
            }
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithCustomCharSetAndRandom() {
        int count = 5;
        int start = 65;
        int end = 90;
        boolean letters = true;
        boolean numbers = true;
        char[] charSet = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        Random random = new Random();
        String randomString = RandomStringUtils.random(count, start, end, letters, numbers, charSet, random);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            if (letters) {
                assertTrue(Character.isLetter(c) || (c == '_' || c == '-'));
                for (char charSetChar : charSet) {
                    if (charSetChar == c) {
                        break;
                    }
                    if (i == randomString.length() - 1) {
                        fail("Character " + c + " not present in char set");
                    }
                }
            }
            if (numbers) {
                assertTrue(Character.isDigit(c) || (c == '_' || c == '-'));
            }
        }
    }
}