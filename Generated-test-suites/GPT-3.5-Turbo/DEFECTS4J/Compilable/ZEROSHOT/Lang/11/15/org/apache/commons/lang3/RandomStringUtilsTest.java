package org.apache.commons.lang3;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        Assert.assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        Assert.assertEquals(10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            Assert.assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        Assert.assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            Assert.assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        Assert.assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        Assert.assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        Assert.assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomWithStartAndEnd() {
        String randomString = RandomStringUtils.random(10, 'A', 'Z', true, false);
        Assert.assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(c >= 'A' && c <= 'Z');
        }
    }

    @Test
    public void testRandomWithCharsArray() {
        char[] chars = {'A', 'B', 'C'};
        String randomString = RandomStringUtils.random(10, chars);
        Assert.assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(c == 'A' || c == 'B' || c == 'C');
        }
    }
}