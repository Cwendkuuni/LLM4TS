package org.apache.commons.lang3;

import java.util.Random;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomStringUtilsTest {

    private static final int DEFAULT_TIMEOUT = 1000;

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testRandom() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(count, randomString.length());
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testRandomAscii() {
        int count = 10;
        String randomString = RandomStringUtils.randomAscii(count);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(count, randomString.length());

        char[] chars = randomString.toCharArray();
        for (char ch : chars) {
            boolean isAscii = ch >= 32 && ch <= 127;
            Assert.assertTrue("Not an ASCII character: " + ch, isAscii);
        }
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testRandomAlphabetic() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(count, randomString.length());

        char[] chars = randomString.toCharArray();
        for (char ch : chars) {
            boolean isAlphabetic = Character.isLetter(ch);
            Assert.assertTrue("Not an alphabetic character: " + ch, isAlphabetic);
        }
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(count, randomString.length());

        char[] chars = randomString.toCharArray();
        for (char ch : chars) {
            boolean isAlphanumeric = Character.isLetterOrDigit(ch);
            Assert.assertTrue("Not an alphanumeric character: " + ch, isAlphanumeric);
        }
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testRandomNumeric() {
        int count = 10;
        String randomString = RandomStringUtils.randomNumeric(count);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(count, randomString.length());

        char[] chars = randomString.toCharArray();
        for (char ch : chars) {
            boolean isNumeric = Character.isDigit(ch);
            Assert.assertTrue("Not a numeric character: " + ch, isNumeric);
        }
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testRandomWithLettersAndNumbers() {
        int count = 10;
        String randomString = RandomStringUtils.random(count, true, true);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(count, randomString.length());

        char[] chars = randomString.toCharArray();
        for (char ch : chars) {
            boolean isLetterOrDigit = Character.isLetterOrDigit(ch);
            Assert.assertTrue("Not a letter or digit character: " + ch, isLetterOrDigit);
        }
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testRandomWithCustomCharacterSet() {
        int count = 10;
        String chars = "abcd1234";
        String randomString = RandomStringUtils.random(count, chars);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(count, randomString.length());

        char[] stringChars = randomString.toCharArray();
        for (char ch : stringChars) {
            boolean isValidChar = chars.indexOf(ch) >= 0;
            Assert.assertTrue("Not a valid character for the custom character set: " + ch, isValidChar);
        }
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testRandomWithCustomCharacterSetArray() {
        int count = 10;
        char[] chars = {'a', 'b', 'c', 'd', '1', '2', '3', '4'};
        String randomString = RandomStringUtils.random(count, chars);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(count, randomString.length());

        char[] stringChars = randomString.toCharArray();
        for (char ch : stringChars) {
            boolean isValidChar = contains(chars, ch);
            Assert.assertTrue("Not a valid character for the custom character set: " + ch, isValidChar);
        }
    }

    private boolean contains(char[] chars, char ch) {
        for (char c : chars) {
            if (c == ch) {
                return true;
            }
        }
        return false;
    }
}
