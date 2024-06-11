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
    public void testRandomStringWithCount() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAsciiStringWithCount() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomAsciiString);
        assertEquals(5, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assert(32 <= c && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabeticStringWithCount() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomAlphabeticString);
        assertEquals(5, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumericStringWithCount() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomAlphanumericString);
        assertEquals(5, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumericStringWithCount() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomNumericString);
        assertEquals(5, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomStringWithCountLettersAndNumbers() {
        String randomString = RandomStringUtils.random(5, true, true);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomStringWithCountStartEndLettersAndNumbers() {
        String randomString = RandomStringUtils.random(5, 'a', 'z', true, true);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c) || c == 'a' || c == 'z');
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNegativeCount() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptyCharArray() {
        RandomStringUtils.random(5, (char[]) null);
    }

}