package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(5);
            assertTrue(randomString.length() == 5);
            assertNotNull(randomString);
        }
    }

    @Test
    public void testRandomAscii() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAscii(5);
            assertTrue(randomString.length() == 5);
            assertNotNull(randomString);
            for (char c : randomString.toCharArray()) {
                assertTrue((int) c >= 32 && (int) c <= 126);
            }
        }
    }

    @Test
    public void testRandomAlphabetic() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphabetic(5);
            assertTrue(randomString.length() == 5);
            assertNotNull(randomString);
            for (char c : randomString.toCharArray()) {
                assertTrue(Character.isAlphabetic(c));
            }
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphanumeric(5);
            assertTrue(randomString.length() == 5);
            assertNotNull(randomString);
            for (char c : randomString.toCharArray()) {
                assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomNumeric() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomNumeric(5);
            assertTrue(randomString.length() == 5);
            assertNotNull(randomString);
            for (char c : randomString.toCharArray()) {
                assertTrue(Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomWithCustomSet() {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(5, chars);
            assertTrue(randomString.length() == 5);
            assertNotNull(randomString);
            for (char c : randomString.toCharArray()) {
                assertTrue(chars.indexOf(c) != -1);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptySet() {
        RandomStringUtils.random(5, new char[0]);
    }

    @Test
    public void testRandomWithCustomStartEnd() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String expected = "ghijPq";
        String randomString = RandomStringUtils.random(5, chars, 2, 6, true, true);
        assertTrue(randomString.length() == 5);
        assertNotNull(randomString);
        assertEquals(expected, randomString);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomOutOfBounds() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        RandomStringUtils.random(5, chars, 27, 6, true, true);
    }
}