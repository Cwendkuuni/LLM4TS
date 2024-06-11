package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        int length = 10;
        String randomString = RandomStringUtils.random(length);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        int length = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(length);
        assertNotNull(randomAsciiString);
        assertEquals(length, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        int length = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(length);
        assertNotNull(randomAlphabeticString);
        assertEquals(length, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        int length = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(length);
        assertNotNull(randomAlphanumericString);
        assertEquals(length, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumericString() {
        int length = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(length);
        assertNotNull(randomNumericString);
        assertEquals(length, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomStringWithRange() {
        int length = 10;
        int start = 65;
        int end = 90;
        String randomString = RandomStringUtils.random(length, start, end, false, false);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= start && c <= end);
        }
    }

    @Test
    public void testRandomStringWithLettersAndNumbers() {
        int length = 10;
        boolean letters = true;
        boolean numbers = true;
        String randomString = RandomStringUtils.random(length, letters, numbers);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (letters) {
                assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '_');
            } else {
                assertTrue(Character.isDigit(c));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNegativeLength() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptyCharacterSet() {
        RandomStringUtils.random(1, (char[]) null);
    }

    @Test
    public void testRandomStringWithEmptyCharacterSetWithDefaultRandom() {
        int length = 1;
        String randomString = RandomStringUtils.random(length, (char[]) null);
        assertEquals("", randomString);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringWithInvalidRange() {
        int length = 1;
        int start = 0;
        int end = -1;
        String randomString = RandomStringUtils.random(length, start, end, false, false, null, RANDOM);
    }

    @Test
    public void testRandomStringWithNullRandom() {
        int length = 10;
        String randomString = RandomStringUtils.random(length, 0, 0, false, false, null, null);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
    }
}