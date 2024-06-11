package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for RandomStringUtils class.
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        // Check randomString contains only characters between 0 and 127 (ASCII)
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(randomString.charAt(i) >= 0 && randomString.charAt(i) <= 127);
        }

        // Check randomString contains only alphabetic characters
        String randomAlphabetic = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabetic);
        assertEquals(10, randomAlphabetic.length());
        for (int i = 0; i < randomAlphabetic.length(); i++) {
            assertTrue(Character.isAlphabetic(randomAlphabetic.charAt(i)));
        }

        // Check randomString contains only alphanumeric characters
        String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumeric);
        assertEquals(10, randomAlphanumeric.length());
        for (int i = 0; i < randomAlphanumeric.length(); i++) {
            assertTrue(Character.isAlphabetic(randomAlphanumeric.charAt(i)) || Character.isDigit(randomAlphanumeric.charAt(i)));
        }

        // Check randomString contains only numeric characters
        String randomNumeric = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumeric);
        assertEquals(10, randomNumeric.length());
        for (int i = 0; i < randomNumeric.length(); i++) {
            assertTrue(Character.isDigit(randomNumeric.charAt(i)));
        }

        // Test for empty string case
        String emptyRandomString = RandomStringUtils.random(0);
        assertEquals("", emptyRandomString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharsArray() {
        RandomStringUtils.random(5, (char[]) null);
    }

    @Test
    public void testRandomStringWithCustomChars() {
        String customRandomString = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10);
        assertNotNull(customRandomString);
        assertEquals(10, customRandomString.length());
        for (int i = 0; i < customRandomString.length(); i++) {
            assertTrue(customRandomString.charAt(i) >= 'A' && customRandomString.charAt(i) <= 'Z' || customRandomString.charAt(i) >= '0' && customRandomString.charAt(i) <= '9');
        }
    }
}