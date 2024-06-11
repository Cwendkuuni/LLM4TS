package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandomStringLength() {
        // Test random string with default settings
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());

        // Test random string with specific character set
        String randomStringWithSet = RandomStringUtils.random(10, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertEquals(10, randomStringWithSet.length());

        // Test random string with negative length
        try {
            RandomStringUtils.random(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRandomStringAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        for (int i = 0; i < randomAsciiString.length(); i++) {
            int asciiValue = (int) randomAsciiString.charAt(i);
            assertTrue(asciiValue >= 32 && asciiValue <= 126);
        }
    }

    @Test
    public void testRandomStringAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomAlphabeticString.charAt(i)));
        }
    }

    @Test
    public void testRandomStringAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char randomChar = randomAlphanumericString.charAt(i);
            assertTrue(Character.isLetterOrDigit(randomChar));
        }
    }

    @Test
    public void testRandomStringNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        for (int i = 0; i < randomNumericString.length(); i++) {
            assertTrue(Character.isDigit(randomNumericString.charAt(i)));
        }
    }

    @Test
    public void testRandomStringWithCustomCharacterSet() {
        String customSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomStringWithCustomSet = RandomStringUtils.random(10, customSet.toCharArray());
        for (int i = 0; i < randomStringWithCustomSet.length(); i++) {
            char randomChar = randomStringWithCustomSet.charAt(i);
            assertTrue(customSet.indexOf(randomChar) != -1);
        }
    }

    @Test
    public void testRandomStringWithCustomStartAndEnd() {
        String randomStringWithSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String randomStringWithCustomSetStartEnd = RandomStringUtils.random(10, randomStringWithSet.charAt(0), randomStringWithSet.charAt(randomStringWithSet.length() - 1), false, false, null, new Random());
        for (int i = 0; i < randomStringWithCustomSetStartEnd.length(); i++) {
            char randomChar = randomStringWithCustomSetStartEnd.charAt(i);
            assertTrue(randomStringWithSet.indexOf(randomChar) != -1);
        }
    }

    @Test
    public void testRandomStringWithCustomCharacterSetAndRange() {
        String customSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABC";
        String randomStringWithCustomSetStartEnd = RandomStringUtils.random(10, customSet.charAt(0), customSet.charAt(customSet.length() - 1), false, false, customSet.toCharArray(), new Random());
        for (int i = 0; i < randomStringWithCustomSetStartEnd.length(); i++) {
            char randomChar = randomStringWithCustomSetStartEnd.charAt(i);
            assertTrue(customSet.indexOf(randomChar) != -1);
        }
    }

    @Test
    public void testRandomStringWithCustomCharacterSetAndSeed() {
        final String customSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random(1L);
        String randomStringWithCustomSetSeed = RandomStringUtils.random(10, customSet.toCharArray(), random);
        String randomStringWithCustomSetSeed2 = RandomStringUtils.random(10, customSet.toCharArray(), random);
        assertArrayEquals(randomStringWithCustomSetSeed.toCharArray(), randomStringWithCustomSetSeed2.toCharArray());
    }
}