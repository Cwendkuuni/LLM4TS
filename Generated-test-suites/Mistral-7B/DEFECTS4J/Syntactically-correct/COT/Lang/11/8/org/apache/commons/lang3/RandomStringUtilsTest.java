package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        Assert.assertEquals("Randomly generated string should not be empty", count, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        Assert.assertEquals("Randomly generated ascii string should not be empty", count, randomAsciiString.length());
    }

    @Test
    public void testRandomAlphabeticString() {
        int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        Assert.assertEquals("Randomly generated alphabetic string should not be empty", count, randomAlphabeticString.length());
    }

    @Test
    public void testRandomAlphanumericString() {
        int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        Assert.assertEquals("Randomly generated alphanumeric string should not be empty", count, randomAlphanumericString.length());
    }

    @Test
    public void testRandomNumericString() {
        int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        Assert.assertEquals("Randomly generated numeric string should not be empty", count, randomNumericString.length());
    }

    @Test
    public void testRandomStringWithRange() {
        int count = 10;
        boolean letters = true;
        boolean numbers = true;
        String randomString = RandomStringUtils.random(count, letters, numbers);
        Assert.assertEquals("Randomly generated string should not be empty", count, randomString.length());
    }

    @Test
    public void testRandomStringWithStartAndEnd() {
        int count = 10;
        int start = 33;
        int end = 126;
        boolean letters = false;
        boolean numbers = false;
        String randomString = RandomStringUtils.random(count, start, end, letters, numbers);
        Assert.assertEquals("Randomly generated string should not be empty", count, randomString.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() {
        int count = -5;
        try {
            RandomStringUtils.random(count);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Requested random string length is less than 0.", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharsArray() {
        int count = 10;
        char[] emptyChars = new char[0];
        try {
            RandomStringUtils.random(count, emptyChars);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The chars array must not be empty", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testRandomStringWithCustomCharArray() {
        int count = 10;
        char[] customChars = {'a', 'b', 'c'};
        String randomString = RandomStringUtils.random(count, customChars);
        Assert.assertEquals("Randomly generated string should not be empty", count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Assert.assertArrayEquals("Randomly generated string should contain only specified chars", new char[]{customChars[RandomStringUtils.nextInt(customChars.length)]}, new char[]{randomString.charAt(i)});
        }
    }

    @Test
    public void testRandomStringWithCustomCharArrayAndCustomRandom() {
        int count = 10;
        int start = 33;
        int end = 126;
        boolean letters = false;
        boolean numbers = false;
        char[] customChars = {'a', 'b', 'c'};
        Random customRandom = new Random();
        String randomString = RandomStringUtils.random(count, start, end, letters, numbers, customChars, customRandom);
        Assert.assertEquals("Randomly generated string should not be empty", count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Assert.assertArrayEquals("Randomly generated string should contain only specified chars", new char[]{customChars[RandomStringUtils.nextInt(customChars.length)]}, new char[]{randomString.charAt(i)});
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testInvalidStartEndRange() {
        int count = 10;
        int start = 33;
        int end = 32;
        boolean letters = false;
        boolean numbers = false;
        char[] customChars = {'a', 'b', 'c'};
        Random customRandom = new Random();
        try {
            RandomStringUtils.random(count, start, end, letters, numbers, customChars, customRandom);
        } catch (ArrayIndexOutOfBoundsException e) {
            Assert.assertEquals("Invalid range, end should be greater than start", e.getMessage());
            throw e;
        }
    }
}