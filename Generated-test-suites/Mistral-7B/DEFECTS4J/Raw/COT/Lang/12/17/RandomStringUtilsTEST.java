package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 5;
        String randomString = RandomStringUtils.random(count);
        assertEquals(count, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        int count = 5;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals(count, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            char character = randomAsciiString.charAt(i);
            assertTrue(character >= 32 && character <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 5;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertEquals(count, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            char character = randomAlphabeticString.charAt(i);
            assertTrue(Character.isAlphabetic(character));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 5;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals(count, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char character = randomAlphanumericString.charAt(i);
            assertTrue((Character.isAlphabetic(character) || Character.isDigit(character)));
        }
    }

    @Test
    public void testRandomNumeric() {
        int count = 5;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertEquals(count, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            char character = randomNumericString.charAt(i);
            assertTrue(Character.isDigit(character));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-5);
    }

    @Test
    public void testRandomWithAllChars() {
        int count = 5;
        String randomString = RandomStringUtils.random(count);
        assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char character = randomString.charAt(i);
            assertTrue(character >= 0 && character <= 0x10FFFF);
        }
    }

    @Test
    public void testRandomWithRange() {
        int count = 5;
        int start = 65;
        int end = 90;
        String randomString = RandomStringUtils.random(count, start, end, false, false, null, RandomStringUtils.RANDOM);
        assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char character = randomString.charAt(i);
            assertTrue(character >= start && character <= end);
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithInvalidRange() {
        int count = 5;
        int start = 0;
        int end = -1;
        RandomStringUtils.random(count, start, end, false, false, null, RandomStringUtils.RANDOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharArray() {
        int count = 5;
        char[] chars = new char[0];
        RandomStringUtils.random(count, 0, 0, false, false, chars, RandomStringUtils.RANDOM);
    }

}