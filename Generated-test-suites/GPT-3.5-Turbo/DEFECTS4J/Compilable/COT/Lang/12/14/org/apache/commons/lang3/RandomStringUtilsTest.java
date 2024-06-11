package org.apache.commons.lang3;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {
  
    @Test
    public void testRandom() {
        int count = 10;
        String result = RandomStringUtils.random(count);
        Assert.assertEquals(count, result.length());
    }
    
    @Test
    public void testRandomAscii() {
        int count = 10;
        String result = RandomStringUtils.randomAscii(count);
        Assert.assertEquals(count, result.length());
        
        for (char ch : result.toCharArray()) {
            Assert.assertTrue(ch >= 32 && ch <= 127);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String result = RandomStringUtils.randomAlphabetic(count);
        Assert.assertEquals(count, result.length());
        
        for (char ch : result.toCharArray()) {
            Assert.assertTrue(Character.isLetter(ch));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String result = RandomStringUtils.randomAlphanumeric(count);
        Assert.assertEquals(count, result.length());
        
        for (char ch : result.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(ch));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        int count = 10;
        String result = RandomStringUtils.randomNumeric(count);
        Assert.assertEquals(count, result.length());
        
        for (char ch : result.toCharArray()) {
            Assert.assertTrue(Character.isDigit(ch));
        }
    }
    
    @Test
    public void testRandomLetters() {
        int count = 10;
        String result = RandomStringUtils.random(count, true, false);
        Assert.assertEquals(count, result.length());
        
        for (char ch : result.toCharArray()) {
            Assert.assertTrue(Character.isLetter(ch));
        }
    }
    
    @Test
    public void testRandomNumbers() {
        int count = 10;
        String result = RandomStringUtils.random(count, false, true);
        Assert.assertEquals(count, result.length());
        
        for (char ch : result.toCharArray()) {
            Assert.assertTrue(Character.isDigit(ch));
        }
    }
    
    @Test
    public void testRandomWithChars() {
        int count = 10;
        char[] chars = {'a', 'b', 'c'};
        String result = RandomStringUtils.random(count, chars);
        Assert.assertEquals(count, result.length());
        
        for (char ch : result.toCharArray()) {
            Assert.assertTrue(ch == 'a' || ch == 'b' || ch == 'c');
        }
    }
    
    @Test
    public void testRandomWithStartAndEnd() {
        int count = 10;
        int start = 32;
        int end = 126;
        String result = RandomStringUtils.random(count, start, end, false, false);
        Assert.assertEquals(count, result.length());
        
        for (char ch : result.toCharArray()) {
            Assert.assertTrue(ch >= 32 && ch <= 126);
        }
    }
    
}