package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test cases for RandomStringUtils class
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assert((c >= 32 && c <= 126));
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assert((Character.isAlphabetic(c) || Character.isDigit(c)));
        }
    }

    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomStringWithSetBounds() {
        String randomString = RandomStringUtils.random(10, 1, 11);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNegativeLength() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomStringWithEmptyCharacterSet() {
        String randomString = RandomStringUtils.random(10, (String) null);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomStringWithSpecifiedCharacterSet() {
        String characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(10, characterSet);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(characterSet.indexOf(c) > -1);
        }
    }

    @Test
    public void testRandomStringWithSpecifiedCharacterSetEmpty() {
        String randomString = RandomStringUtils.random(10, "");
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomStringWithSpecifiedCharacterSetNull() {
        String randomString = RandomStringUtils.random(10, (char[]) null);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomStringWithSpecifiedCharacterSetLengthZero() {
        String characterSet = "";
        String randomString = RandomStringUtils.random(10, characterSet);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomStringWithSpecifiedCharacterSetLengthLessThanZero() {
        String characterSet = "";
        try {
            RandomStringUtils.random(10, characterSet);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRandomStringWithSpecifiedCharacterSetOutOfBounds() {
        String characterSet = "abc";
        String randomString = RandomStringUtils.random(10, characterSet);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(characterSet.indexOf(c) > -1);
        }
    }
}