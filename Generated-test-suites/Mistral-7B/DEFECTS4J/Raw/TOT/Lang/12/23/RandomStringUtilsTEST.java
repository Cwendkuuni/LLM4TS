package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test creating a random string of a given length.
     */
    @Test
    public void testRandomString() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10);
            assertEquals("Random length 10 string", 10, randomString.length());
        }
    }

    /**
     * Test creating a random string of alphabetic characters.
     */
    @Test
    public void testRandomAlphabetic() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphabetic(10);
            assertEquals("Random length 10 alphabetic string", 10, randomString.length());
            for (char c : randomString.toCharArray()) {
                assertTrue("Random alphabetic character", Character.isAlphabetic(c));
            }
        }
    }

    /**
     * Test creating a random string of alphanumeric characters.
     */
    @Test
    public void testRandomAlphanumeric() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphanumeric(10);
            assertEquals("Random length 10 alphanumeric string", 10, randomString.length());
            for (char c : randomString.toCharArray()) {
                assertTrue("Random alphanumeric character", Character.isAlphabetic(c) || Character.isDigit(c));
            }
        }
    }

    /**
     * Test creating a random string of numeric characters.
     */
    @Test
    public void testRandomNumeric() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomNumeric(10);
            assertEquals("Random length 10 numeric string", 10, randomString.length());
            for (char c : randomString.toCharArray()) {
                assertTrue("Random numeric character", Character.isDigit(c));
            }
        }
    }

    /**
     * Test creating a random string with a given start and end range.
     */
    @Test
    public void testRandomStringWithRange() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, 0, chars.length(), false, false, null, RANDOM);
            assertEquals("Random length 10 string from set", 10, randomString.length());
            for (char c : randomString.toCharArray()) {
                assertTrue("Random character is in set", chars.indexOf(c) != -1);
            }
        }
    }

    /**
     * Test creating a random string with a given set of characters.
     */
    @Test
    public void testRandomStringWithSet() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=";
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, chars, RANDOM);
            assertEquals("Random length 10 string from set", 10, randomString.length());
            for (char c : randomString.toCharArray()) {
                assertTrue("Random character is in set", chars.indexOf(c) != -1);
            }
        }
    }

    /**
     * Test creating a random string with a given set of alphabetic characters.
     */
    @Test
    public void testRandomAlphabeticWithSet() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, chars, RANDOM);
            assertEquals("Random length 10 alphabetic string from set", 10, randomString.length());
            for (char c : randomString.toCharArray()) {
                assertTrue("Random alphabetic character is in set", chars.indexOf(c) != -1);
                assertTrue("Random alphabetic character is alphabetic", Character.isAlphabetic(c));
            }
        }
    }

    /**
     * Test creating a random string with a given set of alphanumeric characters.
     */
    @Test
    public void testRandomAlphanumericWithSet() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, chars, RANDOM);
            assertEquals("Random length 10 alphanumeric string from set", 10, randomString.length());
            for (char c : randomString.toCharArray()) {
                assertTrue("Random alphanumeric character is in set", chars.indexOf(c) != -1);
                assertTrue("Random alphanumeric character is alphanumeric", Character.isAlphabetic(c) || Character.isDigit(c));
            }
        }
    }

    /**
     * Test creating a random string with a given set of numeric characters.
     */
    @Test
    public void testRandomNumericWithSet() {
        String chars = "0123456789";
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, chars, RANDOM);
            assertEquals("Random length 10 numeric string from set", 10, randomString.length());
            for (char c : randomString.toCharArray()) {
                assertTrue("Random numeric character is in set", chars.indexOf(c) != -1);
                assertTrue("Random numeric character is numeric", Character.isDigit(c));
            }
        }
    }

    /**
     * Test creating a random string with a given set of characters and with no letters or numbers.
     */
    @Test
    public void testRandomStringWithoutLettersOrNumbers() {
        String chars = "!@#$%^&*()_+-=";
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, chars, RANDOM);
            assertEquals("Random length 10 string from set without letters or numbers", 10, randomString.length());
            for (char c : randomString.toCharArray()) {
                assertTrue("Random character is in set", chars.indexOf(c) != -1);
                assertFalse("Random character is alphabetic", Character.isAlphabetic(c));
                assertFalse("Random character is numeric", Character.isDigit(c));
            }
        }
    }
}