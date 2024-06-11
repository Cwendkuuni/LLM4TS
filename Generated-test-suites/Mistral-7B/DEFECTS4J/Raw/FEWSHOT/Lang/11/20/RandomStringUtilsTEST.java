package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        for (int i = 1; i <= 10; i++) {
            String randomString = RandomStringUtils.random(i);
            assertNotNull(randomString);
            assertEquals(i, randomString.length());
        }
    }

    @Test
    public void testRandomAscii() {
        for (int i = 1; i <= 10; i++) {
            String randomAsciiString = RandomStringUtils.randomAscii(i);
            assertNotNull(randomAsciiString);
            assertEquals(i, randomAsciiString.length());
            for (char c : randomAsciiString.toCharArray()) {
                assert(c >= 32 && c <= 126);
            }
        }
    }

    @Test
    public void testRandomAlphabetic() {
        for (int i = 1; i <= 10; i++) {
            String randomAlphabeticString = RandomStringUtils.randomAlphabetic(i);
            assertNotNull(randomAlphabeticString);
            assertEquals(i, randomAlphabeticString.length());
            for (char c : randomAlphabeticString.toCharArray()) {
                assert(Character.isAlphabetic(c));
            }
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        for (int i = 1; i <= 10; i++) {
            String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(i);
            assertNotNull(randomAlphanumericString);
            assertEquals(i, randomAlphanumericString.length());
            for (char c : randomAlphanumericString.toCharArray()) {
                assert(Character.isAlphabetic(c) || Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomNumeric() {
        for (int i = 1; i <= 10; i++) {
            String randomNumericString = RandomStringUtils.randomNumeric(i);
            assertNotNull(randomNumericString);
            assertEquals(i, randomNumericString.length());
            for (char c : randomNumericString.toCharArray()) {
                assert(Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomWithCustomCharacterSet() {
        String characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 1; i <= 10; i++) {
            String randomString = RandomStringUtils.random(i, characterSet.toCharArray());
            assertNotNull(randomString);
            assertEquals(i, randomString.length());
            for (char c : randomString.toCharArray()) {
                assert(characterSet.indexOf(c) != -1);
            }
        }
    }

    @Test
    public void testRandomWithCustomStartAndEnd() {
        String characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int startIndex = 10;
        int endIndex = 20;
        for (int i = 1; i <= 10; i++) {
            String randomString = RandomStringUtils.random(i, characterSet.toCharArray(), startIndex, endIndex);
            assertNotNull(randomString);
            assertEquals(i, randomString.length());
            for (char c : randomString.toCharArray()) {
                int index = characterSet.indexOf(c);
                assertTrue(index >= startIndex && index <= endIndex);
            }
        }
    }

    @Test
    public void testRandomWithCustomCharacterSetAndRandom() {
        String characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            String randomString = RandomStringUtils.random(i, characterSet.toCharArray(), random);
            assertNotNull(randomString);
            assertEquals(i, randomString.length());
            for (char c : randomString.toCharArray()) {
                int index = characterSet.indexOf(c);
                assertTrue(index != -1);
            }
        }
    }

    @Test
    public void testRandomWithCustomCharacterSetAndSourceOfRandomness() {
        String characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        byte[] seed = new byte[] { 1, 2, 3, 4, 5 };
        Random random = new Random(seed);
        for (int i = 1; i <= 10; i++) {
            String randomString = RandomStringUtils.random(i, characterSet.toCharArray(), random);
            assertNotNull(randomString);
            assertEquals(i, randomString.length());
            for (char c : randomString.toCharArray()) {
                int index = characterSet.indexOf(c);
                assertTrue(index != -1);
            }
        }
    }
}