package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomCustom() {
        String randomString = RandomStringUtils.random(5, 'A', 'Z');
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c));
            assert(c >= 'A' && c <= 'Z');
        }

        randomString = RandomStringUtils.random(5, '0', '9');
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isDigit(c));
            assert(c >= '0' && c <= '9');
        }

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        randomString = RandomStringUtils.random(5, chars.toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(chars.indexOf(c) != -1);
        }
    }

    @Test
    public void testRandomWithRange() {
        RandomStringUtils.random(0, 'A', 'Z');
        RandomStringUtils.random(-1, 'A', 'Z');
        RandomStringUtils.random(5, 'Z', 'A');

        Random random = new Random();
        String randomString = RandomStringUtils.random(5, random);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        randomString = RandomStringUtils.random(5, 0, 0, false, false, null, random);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        randomString = RandomStringUtils.random(5, 'A', 'Z', false, false, null, random);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        randomString = RandomStringUtils.random(5, 'A', 'Z', true, false, null, random);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        randomString = RandomStringUtils.random(5, 'A', 'Z', false, true, null, random);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        randomString = RandomStringUtils.random(5, 'A', 'Z', true, true, null, random);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        randomString = RandomStringUtils.random(5, 'A', 'Z', false, false, "abc".toCharArray(), random);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        randomString = RandomStringUtils.random(5, 'A', 'Z', true, false, "abc".toCharArray(), random);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        randomString = RandomStringUtils.random(5, 'A', 'Z', false, true, "abc".toCharArray(), random);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        randomString = RandomStringUtils.random(5, 'A', 'Z', true, true, "abc".toCharArray(), random);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-1, false, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNullChars() {
        RandomStringUtils.random(5, (char[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyChars() {
        RandomStringUtils.random(5, new char[0]);
    }
}