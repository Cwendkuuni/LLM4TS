package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link RandomStringUtils}
 */
public class RandomStringUtilsTest {

    /**
     * Test for {@link RandomStringUtils#random(int)} method
     */
    @Test
    public void testRandom() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10);
            assertEquals("Length should be 10", 10, randomString.length());
            assertTrue("Should contain random characters", randomString.matches("[\\p{L}\\p{N}\\p{P}]+"));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAscii(int)} method
     */
    @Test
    public void testRandomAscii() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAscii(10);
            assertEquals("Length should be 10", 10, randomString.length());
            assertTrue("Should contain only ASCII characters", randomString.matches("[\\p{ASCII}]+"));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphabetic(int)} method
     */
    @Test
    public void testRandomAlphabetic() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphabetic(10);
            assertEquals("Length should be 10", 10, randomString.length());
            assertTrue("Should contain only alphabetic characters", randomString.matches("[a-zA-Z]+"));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphanumeric(int)} method
     */
    @Test
    public void testRandomAlphanumeric() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphanumeric(10);
            assertEquals("Length should be 10", 10, randomString.length());
            assertTrue("Should contain only alphanumeric characters", randomString.matches("[\\p{Alnum}]+"));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomNumeric(int)} method
     */
    @Test
    public void testRandomNumeric() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomNumeric(10);
            assertEquals("Length should be 10", 10, randomString.length());
            assertTrue("Should contain only numeric characters", randomString.matches("[\\p{Digit}]+"));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean)} method
     */
    @Test
    public void testRandomWithOptions() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, 1, 25, true, true);
            assertEquals("Length should be 10", 10, randomString.length());
            assertTrue("Should contain only alphanumeric characters", randomString.matches("[\\p{Alnum}]+"));
        }

        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, 'A', 'Z', true, false);
            assertEquals("Length should be 10", 10, randomString.length());
            assertTrue("Should contain only alphabetic characters", randomString.matches("[a-zA-Z]+"));
        }

        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, '0', '9', false, true);
            assertEquals("Length should be 10", 10, randomString.length());
            assertTrue("Should contain only numeric characters", randomString.matches("[0-9]+"));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method
     */
    @Test
    public void testRandomWithCustomSourceOfRandomness() {
        Random random = new Random();
        String customRandomString = RandomStringUtils.random(10, 1, 25, true, true, null, random);
        assertEquals("Length should be 10", 10, customRandomString.length());
        assertTrue("Should contain only alphanumeric characters", customRandomString.matches("[\\p{Alnum}]+"));

        String customRandomString2 = RandomStringUtils.random(10, 1, 25, true, true, null, random);
        assertNotEquals("Should not be the same string", customRandomString, customRandomString2);
        assertTrue("Should contain only alphanumeric characters", customRandomString2.matches("[\\p{Alnum}]+"));
    }

    /**
     * Test for {@link RandomStringUtils#random(int, String)} method
     */
    @Test
    public void testRandomWithCustomCharacterSet() {
        String customCharacterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, customCharacterSet);
            assertEquals("Length should be 10", 10, randomString.length());
            assertTrue("Should contain only the given characters", randomString.matches("[" + customCharacterSet + "]+"));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, char...)} method
     */
    @Test
    public void testRandomWithCustomCharacterArray() {
        char[] customCharacterSet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, customCharacterSet);
            assertEquals("Length should be 10", 10, randomString.length());
            assertTrue("Should contain only the given characters", randomString.matches("[\\p{JavaLower}]+"));
        }
    }

    /**
     * Test for empty character set in {@link RandomStringUtils#random(int, String)} method
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharacterSet() {
        RandomStringUtils.random(10, "");
    }

    /**
     * Test for negative length in {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-1, 1, 25, true, true, null, new Random());
    }
}