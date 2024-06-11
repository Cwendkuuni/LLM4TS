package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(randomString.length(), 5);
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomString);
        assertEquals(randomString.length(), 5);
        for (char c : randomString.toCharArray()) {
            assert(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomString);
        assertEquals(randomString.length(), 5);
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomString);
        assertEquals(randomString.length(), 5);
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomString);
        assertEquals(randomString.length(), 5);
        for (char c : randomString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomCustom() {
        String randomString = RandomStringUtils.random(5, 'a', 'z');
        assertNotNull(randomString);
        assertEquals(randomString.length(), 5);
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomCustomWithLimit() {
        String randomString = RandomStringUtils.random(5, 'a', 'z', 2);
        assertNotNull(randomString);
        assertEquals(randomString.length(), 5);
        int count = 0;
        for (char c : randomString.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                count++;
                assert(count <= 2);
            }
        }
    }

    @Test
    public void testRandomCustomWithNumbers() {
        String randomString = RandomStringUtils.random(5, true, true);
        assertNotNull(randomString);
        assertEquals(randomString.length(), 5);
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomCustomWithStartEnd() {
        String randomString = RandomStringUtils.random(5, 'a', 'z', false, false, "abcdef".toCharArray());
        assertNotNull(randomString);
        assertEquals(randomString.length(), 5);
        for (char c : randomString.toCharArray()) {
            assert(c >= 'a' && c <= 'f');
        }
    }
}