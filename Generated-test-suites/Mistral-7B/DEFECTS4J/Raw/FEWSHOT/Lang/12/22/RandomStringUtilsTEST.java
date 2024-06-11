package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            int randomChar = randomAsciiString.charAt(i);
            assertEquals(true, (randomChar >= 32 && randomChar <= 126));
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            int randomChar = randomAlphabeticString.charAt(i);
            assertEquals(true, (randomChar >= 97 && randomChar <= 122) || (randomChar >= 65 && randomChar <= 90));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            int randomChar = randomAlphanumericString.charAt(i);
            assertEquals(true, (randomChar >= 48 && randomChar <= 57) || (randomChar >= 97 && randomChar <= 122) || (randomChar >= 65 && randomChar <= 90));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            int randomChar = randomNumericString.charAt(i);
            assertEquals(true, (randomChar >= 48 && randomChar <= 57));
        }
    }

    @Test
    public void testRandomWithCustomStartEndAndChars() {
        String customString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(10, customString);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            int randomChar = randomString.charAt(i);
            assertEquals(true, customString.indexOf(randomChar) != -1);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyString() {
        RandomStringUtils.random("".toCharArray());
    }
}