package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character at index " + i + " is not an ASCII character", (c >= 32 && c <= 126));
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character at index " + i + " is not an alphabetic character", Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character at index " + i + " is not an alphanumeric character", Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character at index " + i + " is not a numeric character", Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithRange() {
        String randomString = RandomStringUtils.random(10, 48, 58);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character at index " + i + " is not a digit", Character.isDigit(c));
            assertTrue("Character at index " + i + " is not in range [48, 58]", (c >= 48 && c <= 58));
        }
    }

    @Test
    public void testRandomWithChars() {
        String randomString = RandomStringUtils.random(10, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character at index " + i + " is not a valid character", Character.isAlphabetic(c) || Character.isDigit(c));
            assertTrue("Character at index " + i + " is not in given set", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".indexOf(c) != -1);
        }
    }

    @Test
    public void testRandomWithCustomRandom() {
        char[] chars = {'a', 'b', 'c'};
        String randomString = RandomStringUtils.random(10, 0, chars.length, false, false, chars, new Random(12345));
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character at index " + i + " is not one of the given characters", Character.toString(chars[i % chars.length]).equals(Character.toString(c)));
        }
    }
}