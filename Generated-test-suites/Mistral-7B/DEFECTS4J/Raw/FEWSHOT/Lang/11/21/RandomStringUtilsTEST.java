package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        Random StringUtilsRandom = new Random();
        String randomString = RandomStringUtils.random(5);
        assertTrue(randomString.length() == 5);
        Random random = new Random();
        String randomStringFromRandom = RandomStringUtils.random(5, random);
        assertFalse(randomString.equals(randomStringFromRandom));
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertTrue(randomString.length() == 5);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(randomString.charAt(i) >= 32 && randomString.charAt(i) <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertTrue(randomString.length() == 5);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertTrue(randomString.length() == 5);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertTrue(randomString.length() == 5);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isDigit(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomWithSpecifiedCharacterSet() {
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=<>?";
        String randomString = RandomStringUtils.random(5, characterSet.toCharArray());
        assertTrue(randomString.length() == 5);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(characterSet.indexOf(randomString.charAt(i)) != -1);
        }
    }

    @Test
    public void testRandomWithSpecifiedCharacterSetAndStartEnd() {
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String randomString = RandomStringUtils.random(5, 0, characterSet.length(), true, false, null, RANDOM);
        assertTrue(randomString.length() == 5);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(characterSet.indexOf(randomString.charAt(i)) != -1);
            assertTrue(Character.isAlphabetic(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomWithSpecifiedCharacterSetWithNumbers() {
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(5, 0, characterSet.length(), true, true, null, RANDOM);
        assertTrue(randomString.length() == 5);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(characterSet.indexOf(randomString.charAt(i)) != -1);
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomWithSpecifiedCharacterSetAndStartEndWithLettersOnly() {
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String randomString = RandomStringUtils.random(5, 0, characterSet.length(), true, false, null, RANDOM);
        assertTrue(randomString.length() == 5);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(characterSet.indexOf(randomString.charAt(i)) != -1);
            assertTrue(Character.isAlphabetic(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomWithSpecifiedCharacterSetAndStartEndWithNumbersOnly() {
        String characterSet = "0123456789";
        String randomString = RandomStringUtils.random(5, 0, characterSet.length(), false, true, null, RANDOM);
        assertTrue(randomString.length() == 5);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(characterSet.indexOf(randomString.charAt(i)) != -1);
            assertTrue(Character.isDigit(randomString.charAt(i)));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-5, 0, 0, false, false, null, RANDOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharacterSet() {
        String emptyCharacterSet = "";
        RandomStringUtils.random(5, emptyCharacterSet.toCharArray());
    }

}