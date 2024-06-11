package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;

import java.util.Random;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        final int count = 10;
        String randomStr = RandomStringUtils.random(count);
        assertNotNull(randomStr);
        assertEquals(count, randomStr.length());
    }

    @Test
    public void testRandomAscii() {
        final int count = 10;
        String randomAsciiStr = RandomStringUtils.randomAscii(count);
        assertNotNull(randomAsciiStr);
        assertEquals(count, randomAsciiStr.length());
        for (char c : randomAsciiStr.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        final int count = 10;
        String randomAlphabeticStr = RandomStringUtils.randomAlphabetic(count);
        assertNotNull(randomAlphabeticStr);
        assertEquals(count, randomAlphabeticStr.length());
        for (char c : randomAlphabeticStr.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        final int count = 10;
        String randomAlphanumericStr = RandomStringUtils.randomAlphanumeric(count);
        assertNotNull(randomAlphanumericStr);
        assertEquals(count, randomAlphanumericStr.length());
        for (char c : randomAlphanumericStr.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                assertTrue(Character.isLetter(c));
            } else if (Character.isDigit(c)) {
                assertTrue(Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomNumeric() {
        final int count = 10;
        String randomNumericStr = RandomStringUtils.randomNumeric(count);
        assertNotNull(randomNumericStr);
        assertEquals(count, randomNumericStr.length());
        for (char c : randomNumericStr.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithRange() {
        final int count = 10;
        final char start = 'A';
        final char end = 'Z';
        String randomStr = RandomStringUtils.random(count, start, end, true, false, null, RANDOM);
        assertNotNull(randomStr);
        assertEquals(count, randomStr.length());
        for (char c : randomStr.toCharArray()) {
            assertTrue(c >= start && c <= end);
        }
    }

    @Test
    public void testRandomWithCharsArray() {
        final int count = 10;
        final char[] chars = new char[]{'A', 'B', 'C'};
        String randomStr = RandomStringUtils.random(count, 0, chars.length, false, false, chars, RANDOM);
        assertNotNull(randomStr);
        assertEquals(count, randomStr.length());
        Set<Character> set = new HashSet<>();
        for (char c : randomStr.toCharArray()) {
            set.add(c);
        }
        assertArrayEquals(new char[]{ 'A', 'B', 'C' }, set.stream().mapToPrimative(Character.class).toArray());
    }

    @Test
    public void testRandomWithString() {
        final int count = 10;
        final String chars = "ABC";
        String randomStr = RandomStringUtils.random(count, chars);
        assertNotNull(randomStr);
        assertEquals(count, randomStr.length());
        Set<Character> set = new HashSet<>();
        for (char c : randomStr.toCharArray()) {
            set.add(c);
        }
        assertArrayEquals(chars.toCharArray(), set.stream().mapToPrimative(Character.class).toArray());
    }

}