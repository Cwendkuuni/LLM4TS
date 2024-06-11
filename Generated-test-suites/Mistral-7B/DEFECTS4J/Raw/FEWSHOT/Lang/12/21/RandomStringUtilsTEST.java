package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char randomChar = randomString.charAt(i);
            int randomInt = (int) randomChar;
            assert (Character.isAlphabetic(randomChar) || Character.isDigit(randomChar) || (randomInt >= 32 && randomInt <= 126));
        }
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char randomChar = randomString.charAt(i);
            int randomInt = (int) randomChar;
            assert (Character.isAlphabetic(randomChar) || Character.isDigit(randomChar) || (randomInt >= 32 && randomInt <= 126));
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char randomChar = randomString.charAt(i);
            assert (Character.isAlphabetic(randomChar));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char randomChar = randomString.charAt(i);
            int randomInt = (int) randomChar;
            assert (Character.isAlphabetic(randomChar) || Character.isDigit(randomChar) || (randomInt >= 32 && randomInt <= 126));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char randomChar = randomString.charAt(i);
            assert (Character.isDigit(randomChar));
        }
    }

    @Test
    public void testRandomCustom() {
        String customChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomString = RandomStringUtils.random(5, customChars.toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char randomChar = randomString.charAt(i);
            assert (customChars.indexOf(randomChar) != -1);
        }
    }

    @Test
    public void testRandomWithRange() {
        String randomString = RandomStringUtils.random(5, 65, 90);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char randomChar = randomString.charAt(i);
            assert (randomChar >= 65 && randomChar <= 90);
        }
    }

    @Test
    public void testRandomWithEmptySet() {
        Random random = new Random();
        RandomStringUtils randomStringUtils = new RandomStringUtils();
        try {
            String randomString = randomStringUtils.random(5, null, random);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testRandomWithNegativeLength() {
        RandomStringUtils randomStringUtils = new RandomStringUtils();
        try {
            String randomString = randomStringUtils.random(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}