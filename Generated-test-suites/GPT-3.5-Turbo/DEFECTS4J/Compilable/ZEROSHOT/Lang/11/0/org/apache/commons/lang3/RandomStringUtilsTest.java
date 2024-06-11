package org.apache.commons.lang3;
import org.junit.Assert;
import org.junit.Test;
import java.util.Random;

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
        for (char ch : randomAsciiString.toCharArray()) {
            Assert.assertTrue(ch >= 32 && ch <= 126);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        Assert.assertEquals(count, randomAlphabeticString.length());
        for (char ch : randomAlphabeticString.toCharArray()) {
            Assert.assertTrue(Character.isLetter(ch));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        Assert.assertEquals(count, randomAlphanumericString.length());
        for (char ch : randomAlphanumericString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(ch));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        Assert.assertEquals(count, randomNumericString.length());
        for (char ch : randomNumericString.toCharArray()) {
            Assert.assertTrue(Character.isDigit(ch));
        }
    }

    @Test
    public void testRandomWithStartEnd() {
        int count = 10;
        int start = 65;
        int end = 90;
        boolean letters = true;
        boolean numbers = false;
        String randomString = RandomStringUtils.random(count, start, end, letters, numbers);
        Assert.assertEquals(count, randomString.length());
        for (char ch : randomString.toCharArray()) {
            Assert.assertTrue(ch >= start && ch <= end);
        }
    }
    
    @Test
    public void testRandomWithChars() {
        int count = 10;
        char[] chars = {'A', 'B', 'C'};
        String randomString = RandomStringUtils.random(count, chars);
        Assert.assertEquals(count, randomString.length());
        for (char ch : randomString.toCharArray()) {
            Assert.assertTrue(ch == 'A' || ch == 'B' || ch == 'C');
        }
    }

}