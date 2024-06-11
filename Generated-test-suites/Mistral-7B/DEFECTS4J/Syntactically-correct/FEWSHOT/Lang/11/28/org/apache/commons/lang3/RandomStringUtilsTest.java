package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10);
            assertNotNull(randomString);
            assertEquals(10, randomString.length());
        }
    }

    @Test
    public void testRandomAscii() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAscii(10);
            assertNotNull(randomString);
            assertEquals(10, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                assertTrue(randomString.charAt(j) >= 32 && randomString.charAt(j) <= 126);
            }
        }
    }

    @Test
    public void testRandomAlphabetic() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphabetic(10);
            assertNotNull(randomString);
            assertEquals(10, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                assertTrue(Character.isAlphabetic(randomString.charAt(j)));
            }
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphanumeric(10);
            assertNotNull(randomString);
            assertEquals(10, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                assertTrue(Character.isAlphabetic(randomString.charAt(j)) || Character.isDigit(randomString.charAt(j)));
            }
        }
    }

    @Test
    public void testRandomNumeric() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomNumeric(10);
            assertNotNull(randomString);
            assertEquals(10, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                assertTrue(Character.isDigit(randomString.charAt(j)));
            }
        }
    }

    @Test
    public void testRandomWithOptions() {
        char[] chars = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, 0, chars.length, true, true, chars, RANDOM);
            assertNotNull(randomString);
            assertEquals(10, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                assertTrue(chars[random.nextInt(chars.length)] == randomString.charAt(j) || chars[random.nextInt(chars.length)] == randomString.charAt(j + 1));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharsArray() {
        RandomStringUtils.random(1, (char[]) null);
    }

}