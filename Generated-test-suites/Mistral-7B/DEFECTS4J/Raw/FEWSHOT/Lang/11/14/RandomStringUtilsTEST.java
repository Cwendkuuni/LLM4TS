package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Test random method with default options
        String str = RandomStringUtils.random(10);
        assertNotNull(str);
        assertTrue(str.length() == 10);

        // Test randomAscii method
        String strAscii = RandomStringUtils.randomAscii(10);
        assertNotNull(strAscii);
        assertTrue(strAscii.length() == 10);
        for (char c : strAscii.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }

        // Test randomAlphabetic method
        String strAlphabetic = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(strAlphabetic);
        assertTrue(strAlphabetic.length() == 10);
        for (char c : strAlphabetic.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }

        // Test randomAlphanumeric method
        String strAlphanumeric = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(strAlphanumeric);
        assertTrue(strAlphanumeric.length() == 10);
        for (char c : strAlphanumeric.toCharArray()) {
            if (Character.isDigit(c)) {
                assertTrue(Character.isDigit(c));
            } else {
                assertTrue(Character.isAlphabetic(c));
            }
        }

        // Test randomNumeric method
        String strNumeric = RandomStringUtils.randomNumeric(10);
        assertNotNull(strNumeric);
        assertTrue(strNumeric.length() == 10);
        for (char c : strNumeric.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }

        // Test random method with custom options
        String strCustom = RandomStringUtils.random(10, true, true);
        assertNotNull(strCustom);
        assertTrue(strCustom.length() == 10);
        for (char c : strCustom.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }

        String strCustomStartEnd = RandomStringUtils.random(10, 2, 4, true, true);
        assertNotNull(strCustomStartEnd);
        assertTrue(strCustomStartEnd.length() == 10);
        for (char c : strCustomStartEnd.toCharArray()) {
            if (Character.isDigit(c)) {
                assertTrue(Character.isDigit(c));
                assertTrue(c >= '0' && c <= '9');
            } else {
                assertTrue(Character.isAlphabetic(c));
                assertTrue(Character.isLetterOrDigit(c));
            }
        }

        // Test random method with custom character set
        String strCustomChars = RandomStringUtils.random(10, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        assertNotNull(strCustomChars);
        assertTrue(strCustomChars.length() == 10);
        for (char c : strCustomChars.toCharArray()) {
            assertTrue(strCustomChars.indexOf(c) != -1);
        }

        // Test invalid arguments
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.random(-1));
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.random(0));
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.random(10, ""));

    }

    @Test
    public void testRandomWithCustomRandom() {
        // Test random method with custom source of randomness
        Random random = new Random();
        String str = RandomStringUtils.random(10, 0, 0, false, false, null, random);
        assertNotNull(str);
        assertTrue(str.length() == 10);

        Random randomSeeded = new Random(12345);
        String strSeeded = RandomStringUtils.random(10, 0, 0, false, false, null, randomSeeded);
        assertNotNull(strSeeded);
        assertTrue(strSeeded.length() == 10);
        String seededStr = strSeeded; // Save the seeded string for next test

        // Test that generating the same random string with the same seed produces the same result
        String strSameSeed = RandomStringUtils.random(10, 0, 0, false, false, null, randomSeeded);
        assertTrue(strSameSeed.equals(seededStr));
    }

    @Test
    public void testRandomWithNonAsciiChars() {
        // Test random method with non-ASCII characters
        String strNonAscii = RandomStringUtils.random(10, 0x410, 0x44F, true, true);
        assertNotNull(strNonAscii);
        assertTrue(strNonAscii.length() == 10);
        for (char c : strNonAscii.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || Character.isSupplementaryCodePoint(c));
        }
    }
}