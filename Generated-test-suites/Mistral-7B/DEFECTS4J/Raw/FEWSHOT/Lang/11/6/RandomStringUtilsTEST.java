package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.Random;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        for (int length = 0; length <= 10; length++) {
            String randomString = RandomStringUtils.random(length);
            assertNotNull(randomString);
            assertEquals(length, randomString.length());
        }
    }

    @Test
    public void testRandomAscii() {
        for (int length = 0; length <= 10; length++) {
            String randomString = RandomStringUtils.randomAscii(length);
            assertNotNull(randomString);
            assertEquals(length, randomString.length());
            for (int i = 0; i < length; i++) {
                int ascii = randomString.charAt(i);
                assertTrue(ascii >= 32 && ascii <= 126);
            }
        }
    }

    @Test
    public void testRandomAlphabetic() {
        for (int length = 0; length <= 10; length++) {
            String randomString = RandomStringUtils.randomAlphabetic(length);
            assertNotNull(randomString);
            assertEquals(length, randomString.length());
            for (int i = 0; i < length; i++) {
                assertTrue(Character.isAlphabetic(randomString.charAt(i)));
            }
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        for (int length = 0; length <= 10; length++) {
            String randomString = RandomStringUtils.randomAlphanumeric(length);
            assertNotNull(randomString);
            assertEquals(length, randomString.length());
            for (int i = 0; i < length; i++) {
                char c = randomString.charAt(i);
                assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomNumeric() {
        for (int length = 0; length <= 10; length++) {
            String randomString = RandomStringUtils.randomNumeric(length);
            assertNotNull(randomString);
            assertEquals(length, randomString.length());
            for (int i = 0; i < length; i++) {
                assertTrue(Character.isDigit(randomString.charAt(i)));
            }
        }
    }

    @Test
    public void testRandomWithRange() {
        for (int length = 0; length <= 10; length++) {
            String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyz", length);
            assertNotNull(randomString);
            assertEquals(length, randomString.length());
            for (int i = 0; i < length; i++) {
                assertTrue(Character.isAlphabetic(randomString.charAt(i)));
            }
        }

        for (int length = 0; length <= 10; length++) {
            String randomString = RandomStringUtils.random("0123456789", length);
            assertNotNull(randomString);
            assertEquals(length, randomString.length());
            for (int i = 0; i < length; i++) {
                assertTrue(Character.isDigit(randomString.charAt(i)));
            }
        }
    }

    @Test
    public void testRandomWithNull() {
        for (int length = 0; length <= 10; length++) {
            String randomString = RandomStringUtils.random(length);
            assertNotNull(randomString);
            assertEquals(length, randomString.length());
            for (int i = 0; i < length; i++) {
                assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
            }
        }
    }
}