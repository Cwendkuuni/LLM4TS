package org.apache.commons.lang3;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(5);
            int length = randomString.length();
            assertEquals("Expected length to be greater than or equal to 5", length, 5);
        }
    }

    @Test
    public void testRandomAscii() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAscii(5);
            int length = randomString.length();
            assertEquals("Expected length to be greater than or equal to 5", length, 5);
            for (int j = 0; j < length; j++) {
                char c = randomString.charAt(j);
                assertTrue("Expected character to be an ASCII character", Character.isASCII(c));
            }
        }
    }

    @Test
    public void testRandomAlphabetic() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphabetic(5);
            int length = randomString.length();
            assertEquals("Expected length to be greater than or equal to 5", length, 5);
            for (int j = 0; j < length; j++) {
                char c = randomString.charAt(j);
                assertTrue("Expected character to be alphabetic", Character.isAlphabetic(c));
            }
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphanumeric(5);
            int length = randomString.length();
            assertEquals("Expected length to be greater than or equal to 5", length, 5);
            for (int j = 0; j < length; j++) {
                char c = randomString.charAt(j);
                assertTrue("Expected character to be alphanumeric", Character.isAlphabetic(c) || Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomNumeric() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomNumeric(5);
            int length = randomString.length();
            assertEquals("Expected length to be greater than or equal to 5", length, 5);
            for (int j = 0; j < length; j++) {
                char c = randomString.charAt(j);
                assertTrue("Expected character to be numeric", Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomLettersAndNumbers() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(5, true, true);
            int length = randomString.length();
            assertEquals("Expected length to be greater than or equal to 5", length, 5);
            for (int j = 0; j < length; j++) {
                char c = randomString.charAt(j);
                assertTrue("Expected character to be alphanumeric", Character.isAlphabetic(c) || Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomOnlyLetters() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(5, true, false);
            int length = randomString.length();
            assertEquals("Expected length to be greater than or equal to 5", length, 5);
            for (int j = 0; j < length; j++) {
                char c = randomString.charAt(j);
                assertTrue("Expected character to be alphabetic", Character.isAlphabetic(c));
            }
        }
    }

    @Test
    public void testRandomOnlyNumbers() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(5, false, true);
            int length = randomString.length();
            assertEquals("Expected length to be greater than or equal to 5", length, 5);
            for (int j = 0; j < length; j++) {
                char c = randomString.charAt(j);
                assertTrue("Expected character to be numeric", Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomCustom() {
        String chars = "1234567890ABCDEF0HIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(5, chars.toCharArray());
            int length = randomString.length();
            assertEquals("Expected length to be greater than or equal to 5", length, 5);
            for (int j = 0; j < length; j++) {
                char c = randomString.charAt(j);
                assertTrue("Expected character to be in set: " + chars, chars.indexOf(c) != -1);
            }
        }
    }

    @Test
    public void testRandomWithCustomRandomSource() {
        String chars = "1234567890ABCDEF0HIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String seed = "abcdefg";
        Random random = new Random(seed.hashCode());
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(5, chars.toCharArray(), random);
            int length = randomString.length();
            assertEquals("Expected length to be greater than or equal to 5", length, 5);
            for (int j = 0; j < length; j++) {
                char c = randomString.charAt(j);
                assertTrue("Expected character to be in set: " + chars, chars.indexOf(c) != -1);
            }
        }
    }
}