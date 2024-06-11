package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.isA;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
        assertNotNull(randomString);

        String randomAscii = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomAscii.length());
        assertNotNull(randomAscii);
        for (char c : randomAscii.toCharArray()) {
            assert(Character.isASCII(c) && Character.isDefined(c));
        }

        String randomAlphabetic = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomAlphabetic.length());
        assertNotNull(randomAlphabetic);
        for (char c : randomAlphabetic.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c) || Character.isWhitespace(c));
        }

        String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomAlphanumeric.length());
        assertNotNull(randomAlphanumeric);
        for (char c : randomAlphanumeric.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c) || Character.isWhitespace(c) || Character.isSpaceChar(c));
        }

        String randomNumeric = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomNumeric.length());
        assertNotNull(randomNumeric);
        for (char c : randomNumeric.toCharArray()) {
            assert(Character.isDigit(c) || Character.isWhitespace(c) || Character.isSpaceChar(c));
        }

        String random = RandomStringUtils.random(10, true, true);
        assertEquals(10, random.length());
        assertNotNull(random);
        for (char c : random.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                continue;
            }
            assert(Character.isWhitespace(c) || Character.isSpaceChar(c));
        }
    }

    @Test
    public void testRandomWithConstraints() {
        String random = RandomStringUtils.random(10, 48, 58, true, true);
        assertEquals(10, random.length());
        assertNotNull(random);
        for (char c : random.toCharArray()) {
            assert(Character.isDigit(c) || Character.isWhitespace(c) || Character.isSpaceChar(c));
            if (Character.isDigit(c)) {
                assert(c >= 48 && c <= 58);
            }
        }

        String randomAlphabetic = RandomStringUtils.random(10, true, false);
        assertEquals(10, randomAlphabetic.length());
        assertNotNull(randomAlphabetic);
        for (char c : randomAlphabetic.toCharArray()) {
            assert(Character.isLetter(c) || Character.isWhitespace(c) || Character.isSpaceChar(c));
        }

        String randomAlphanumeric = RandomStringUtils.random(10, true, true);
        assertEquals(10, randomAlphanumeric.length());
        assertNotNull(randomAlphanumeric);
        for (char c : randomAlphanumeric.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                continue;
            }
            assert(Character.isWhitespace(c) || Character.isSpaceChar(c));
        }

        String randomWithChars = RandomStringUtils.random(10, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        assertEquals(10, randomWithChars.length());
        assertNotNull(randomWithChars);
        for (char c : randomWithChars.toCharArray()) {
            assert(Character.isLetterOrDigit(c) || Character.isWhitespace(c) || Character.isSpaceChar(c));
            if (Character.isLetterOrDigit(c)) {
                assert(Character.isLetter(c) || Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomWithCustomCharsetAndRandom() {
        String randomString = RandomStringUtils.random(10, 'a', 'z');
        assertEquals(10, randomString.length());
        assertNotNull(randomString);
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c));
            assert(c >= 'a' && c <= 'z');
        }

        String randomStringWithCustomRandom = RandomStringUtils.random(10, 'a', 'z', new Random());
        assertEquals(10, randomStringWithCustomRandom.length());
        assertNotNull(randomStringWithCustomRandom);
        for (char c : randomStringWithCustomRandom.toCharArray()) {
            assert(Character.isAlphabetic(c));
            assert(c >= 'a' && c <= 'z');
        }
    }

    @Test
    public void testRandomWithInvalidArgument() {
        String chars = null;
        try {
            RandomStringUtils.random(1, chars);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            RandomStringUtils.random(-1, 'a', 'z');
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}