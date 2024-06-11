package org.apache.commons.lang3;

import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {
    
    @Test
    public void testRandom() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        Assert.assertEquals(count, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        Assert.assertEquals(count, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            Assert.assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        Assert.assertEquals(count, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        Assert.assertEquals(count, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        Assert.assertEquals(count, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        int count = 10;
        boolean letters = true;
        boolean numbers = true;
        String randomString = RandomStringUtils.random(count, letters, numbers);
        Assert.assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithChars() {
        int count = 10;
        char[] chars = { 'a', 'b', 'c' };
        String randomString = RandomStringUtils.random(count, chars);
        Assert.assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(c == 'a' || c == 'b' || c == 'c');
        }
    }
}
