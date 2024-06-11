package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.util.Random;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10);
            assertNotNull(randomString);
            assertEquals(randomString.length(), 10);
        }
    }

    @Test
    public void testRandomAscii() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAscii(10);
            assertNotNull(randomString);
            assertEquals(randomString.length(), 10);
            for (char c : randomString.toCharArray()) {
                assertTrue(c >= 32 && c <= 126);
            }
        }
    }

    @Test
    public void testRandomAlphabetic() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphabetic(10);
            assertNotNull(randomString);
            assertEquals(randomString.length(), 10);
            for (char c : randomString.toCharArray()) {
                assertTrue(Character.isAlphabetic(c));
            }
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphanumeric(10);
            assertNotNull(randomString);
            assertEquals(randomString.length(), 10);
            for (char c : randomString.toCharArray()) {
                assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomNumeric() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomNumeric(10);
            assertNotNull(randomString);
            assertEquals(randomString.length(), 10);
            for (char c : randomString.toCharArray()) {
                assertTrue(Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomWithCustomCharSet() {
        char[] customCharSet = new char[]{'A', 'B', 'C'};
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, customCharSet);
            assertNotNull(randomString);
            assertEquals(randomString.length(), 10);
            for (char c : randomString.toCharArray()) {
                assertArrayEquals(new char[]{c}, customCharSet);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharArray() {
        RandomStringUtils.randomAlphanumeric(0);
    }
}