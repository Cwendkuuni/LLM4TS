package org.apache.commons.lang3;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;

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
        String randomString = RandomStringUtils.randomAscii(count);
        Assert.assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        Assert.assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        Assert.assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        int count = 10;
        String randomString = RandomStringUtils.randomNumeric(count);
        Assert.assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        int count = 10;
        String randomString = RandomStringUtils.random(count, true, true);
        Assert.assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomWithStartAndEnd() {
        int count = 10;
        int start = 'a';
        int end = 'z';
        String randomString = RandomStringUtils.random(count, start, end, true, false);
        Assert.assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(c >= start && c <= end);
        }
    }

    @Test
    public void testRandomWithCustomChars() {
        int count = 10;
        char[] chars = new char[]{'a', 'b', 'c', 'd', 'e'};
        String randomString = RandomStringUtils.random(count, chars);
        Assert.assertEquals(count, randomString.length());
        Set<Character> charSet = new HashSet<>();
        for (char c : randomString.toCharArray()) {
            charSet.add(c);
        }
        for (char c : chars) {
            Assert.assertTrue(charSet.contains(c));
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        int count = -1;
        RandomStringUtils.random(count);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyChars() {
        int count = 10;
        char[] chars = {};
        RandomStringUtils.random(count, chars);
    }
    
}