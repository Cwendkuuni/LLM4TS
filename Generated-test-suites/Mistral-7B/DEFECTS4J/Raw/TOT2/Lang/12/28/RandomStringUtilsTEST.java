package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link RandomStringUtils}.
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
            assertTrue(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomStringWithRange() {
        String randomString = RandomStringUtils.random(10, 'a', 'z');
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) && c >= 'a' && c <= 'z');
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringNegativeLength() {
        RandomStringUtils.random(-5);
    }

    @Test
    public void testRandomStringWithCharArray() {
        String randomString = RandomStringUtils.random(10, new char[]{'a', 'b', 'c'});
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(new String(new char[]{c}).indexOf('a') >= 0 || new String(new char[]{c}).indexOf('b') >= 0 || new String(new char[]{c}).indexOf('c') >= 0);
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringWithEmptyCharArray() {
        RandomStringUtils.random(10, new char[0]);
    }

    @Test
    public void testRandomStringWithString() {
        String randomString = RandomStringUtils.random(10, "abc");
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(new String(new char[]{c}).indexOf('a') >= 0 || new String(new char[]{c}).indexOf('b') >= 0 || new String(new char[]{c}).indexOf('c') >= 0);
        }
    }

    @Test
    public void testRandomStringWithNullCharArray() {
        String randomString = RandomStringUtils.random(10, (char[]) null);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNegativeCount() {
        RandomStringUtils.random(-5, "abc".toCharArray());
    }
}