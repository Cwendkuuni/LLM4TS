package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        final int length = 10;
        String randomString = RandomStringUtils.random(length);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        final int length = 10;
        String randomString = RandomStringUtils.randomAscii(length);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            char c = randomString.charAt(i);
            assertTrue(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        final int length = 10;
        String randomString = RandomStringUtils.randomAlphabetic(length);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            char c = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        final int length = 10;
        String randomString = RandomStringUtils.randomAlphanumeric(length);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            char c = randomString.charAt(i);
            assertTrue((Character.isAlphabetic(c) || Character.isDigit(c)));
        }
    }

    @Test
    public void testRandomNumeric() {
        final int length = 10;
        String randomString = RandomStringUtils.randomNumeric(length);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            char c = randomString.charAt(i);
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithRange() {
        final int length = 10;
        final int start = 97;
        final int end = 122;
        String randomString = RandomStringUtils.random(length, start, end, false, false, null, RANDOM);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            char c = randomString.charAt(i);
            assertTrue(c >= start && c <= end);
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        final int length = 10;
        String randomString = RandomStringUtils.random(length, true, true, null, RANDOM);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            char c = randomString.charAt(i);
            assertTrue((Character.isAlphabetic(c) || Character.isDigit(c)));
        }
    }

    @Test
    public void testRandomWithCustomCharArray() {
        final int length = 10;
        char[] chars = {'a', 'b', 'c', '1', '2', '3'};
        String randomString = RandomStringUtils.random(length, chars, RANDOM);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            char c = randomString.charAt(i);
            for (char charInArray : chars) {
                if (c == charInArray) {
                    break;
                }
                if (i == length - 1 && c != charInArray) {
                    fail("Expected character " + charInArray + " to appear in the random string");
                }
            }
        }
    }
}