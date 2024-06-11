package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
        for (int i = 0; i < randomAsciiString.length(); i++) {
            char c = randomAsciiString.charAt(i);
            assert(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            char c = randomAlphabeticString.charAt(i);
            assert(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char c = randomAlphanumericString.charAt(i);
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            char c = randomNumericString.charAt(i);
            assert(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomStringWithRange() {
        String randomString = RandomStringUtils.random("a", "z", 10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assert(c >= 'a' && c <= 'z');
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRandomStringLength() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharArray() {
        RandomStringUtils.random(1, (char[]) null);
    }

    @Test
    public void testRandomStringWithEmptyCharArray() {
        String randomString = RandomStringUtils.random(1, new char[0]);
        assertNull(randomString);
    }

    @Test
    public void testRandomStringWithCustomRandom() {
        String randomString = RandomStringUtils.random(10, new Random(1));
        String expected = "552965619256322552965619256322"; // High surrogates followed by low surrogates
        assertEquals(expected, randomString);
    }

    @Test
    public void testRandomStringWithCustomCharArray() {
        String randomString = RandomStringUtils.random(10, "abcdef".toCharArray());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assert(Character.isAlphabetic(c) || Character.isDigit(c) || c == '\u0000');
        }
    }

    @Test
    public void testRandomStringWithCustomCharArrayAndCustomRandom() {
        char[] chars = {'a', 'b', 'c'};
        String randomString = RandomStringUtils.random(10, chars, new Random(1));
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assert(Character.isAlphabetic(c) || c == 'a' || c == 'b' || c == 'c');
        }
    }

}