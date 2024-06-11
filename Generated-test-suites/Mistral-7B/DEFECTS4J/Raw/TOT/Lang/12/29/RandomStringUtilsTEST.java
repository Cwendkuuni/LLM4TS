package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        // Test creating a random string of default length
        String randomString = RandomStringUtils.random();
        assertNotNull(randomString);
        assertFalse(randomString.isEmpty());
        int length = randomString.length();
        assertTrue(length > 0);

        // Test creating a random string of specified length
        String randomStringOfLength5 = RandomStringUtils.random(5);
        assertNotNull(randomStringOfLength5);
        assertFalse(randomStringOfLength5.isEmpty());
        assertEquals(5, randomStringOfLength5.length());

        // Test creating a random string of length 0
        String randomStringOfLength0 = RandomStringUtils.random(0);
        assertEquals("", randomStringOfLength0);

        // Test creating a random string of negative length
        try {
            String randomStringOfNegativeLength = RandomStringUtils.random(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test creating a random string with all characters
        String randomStringAllChars = RandomStringUtils.random();
        for (int i = 0; i < randomStringAllChars.length(); i++) {
            char c = randomStringAllChars.charAt(i);
            assertTrue((c >= 0 && c <= 0xFFFF) || (c >= 0xFF10 && c <= 0xFFEF));
        }

        // Test creating a random string with specified character range
        String randomStringAscii = RandomStringUtils.randomAscii(10);
        String randomStringAlphanumeric = RandomStringUtils.randomAlphanumeric(10);
        String randomStringAlphabetic = RandomStringUtils.randomAlphabetic(10);
        String randomStringNumeric = RandomStringUtils.randomNumeric(10);
        for (int i = 0; i < randomStringAscii.length(); i++) {
            char c = randomStringAscii.charAt(i);
            assertTrue(c >= 32 && c <= 126);
        }
        for (int i = 0; i < randomStringAlphanumeric.length(); i++) {
            char c = randomStringAlphanumeric.charAt(i);
            assertTrue((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= 0 && c <= 9) || (c >= 10 && c <= 122));
        }
        for (int i = 0; i < randomStringAlphabetic.length(); i++) {
            char c = randomStringAlphabetic.charAt(i);
            assertTrue((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'));
        }
        for (int i = 0; i < randomStringNumeric.length(); i++) {
            char c = randomStringNumeric.charAt(i);
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomStringWithCustomRandom() {
        Random random = new Random(1);
        String randomString = RandomStringUtils.random(10, 'A', 'Z', random);
        String randomString2 = RandomStringUtils.random(10, 'A', 'Z', random);
        assertEquals(randomString, randomString2);
    }

    @Test
    public void testRandomStringWithCustomCharSet() {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String randomString = RandomStringUtils.random(10, charSet.toCharArray());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(charSet.indexOf(c) != -1);
        }
    }

    @Test
    public void testRandomStringWithCustomStartEndAndLetters() {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String randomString = RandomStringUtils.random(10, 1, charSet.length(), true, true, null, RANDOM);
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            if (Character.isLetter(c)) {
                assertTrue(charSet.indexOf(c) != -1);
            } else {
                assertTrue(Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomStringWithCustomStartEndAndOnlyLetters() {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String randomString = RandomStringUtils.random(10, 1, charSet.length(), true, false, null, RANDOM);
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(Character.isLetter(c) && charSet.indexOf(c) != -1);
        }
    }

    @Test
    public void testRandomStringWithCustomStartEndAndOnlyNumbers() {
        String charSet = "0123456789";
        String randomString = RandomStringUtils.random(10, 1, charSet.length(), false, true, null, RANDOM);
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(Character.isDigit(c) && charSet.indexOf(c) != -1);
        }
    }

    @Test
    public void testRandomStringWithCustomStartEndAndInvalidFlags() {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        try {
            String randomString = RandomStringUtils.random(10, 1, charSet.length(), true, false, null, RANDOM);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            String randomString = RandomStringUtils.random(10, 1, charSet.length(), false, false, null, RANDOM);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRandomStringWithCustomStartEndAndEmptyCharSet() {
        String charSet = "";
        try {
            String randomString = RandomStringUtils.random(10, 1, charSet.length(), false, false, null, RANDOM);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRandomStringWithCustomStartEndAndNegativeLength() {
        String charSet = "";
        try {
            String randomString = RandomStringUtils.random(-1, 0, charSet.length(), false, false, null, RANDOM);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}