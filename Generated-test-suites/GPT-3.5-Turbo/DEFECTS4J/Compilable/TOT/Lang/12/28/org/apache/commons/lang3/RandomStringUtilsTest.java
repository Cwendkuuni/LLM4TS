package org.apache.commons.lang3;

import org.junit.Assert;
import org.junit.Test;
import java.util.regex.Pattern;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Typical use case
        String randomString = RandomStringUtils.random(5);
        Assert.assertEquals(5, randomString.length());

        // Edge case
        randomString = RandomStringUtils.random(0);
        Assert.assertEquals(0, randomString.length());
    }
    
    @Test
    public void testRandomAscii() {
        // Typical use case
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        Assert.assertEquals(5, randomAsciiString.length());
        Assert.assertTrue(Pattern.matches("[\\x20-\\x7E]+", randomAsciiString));

        // Edge case
        randomAsciiString = RandomStringUtils.randomAscii(0);
        Assert.assertEquals(0, randomAsciiString.length());
    }
    
    @Test
    public void testRandomAlphabetic() {
        // Typical use case
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        Assert.assertEquals(5, randomAlphabeticString.length());
        Assert.assertTrue(randomAlphabeticString.matches("[a-zA-Z]+"));

        // Edge case
        randomAlphabeticString = RandomStringUtils.randomAlphabetic(0);
        Assert.assertEquals(0, randomAlphabeticString.length());
    }
    
    @Test
    public void testRandomAlphanumeric() {
        // Typical use case
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        Assert.assertEquals(5, randomAlphanumericString.length());
        Assert.assertTrue(randomAlphanumericString.matches("[a-zA-Z0-9]+"));

        // Edge case
        randomAlphanumericString = RandomStringUtils.randomAlphanumeric(0);
        Assert.assertEquals(0, randomAlphanumericString.length());
    }

    @Test
    public void testRandomNumeric() {
        // Typical use case
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        Assert.assertEquals(5, randomNumericString.length());
        Assert.assertTrue(randomNumericString.matches("[0-9]+"));

        // Edge case
        randomNumericString = RandomStringUtils.randomNumeric(0);
        Assert.assertEquals(0, randomNumericString.length());
    }
    
    @Test
    public void testRandomCustomChars() {
        // Typical use case
        String randomCustomString = RandomStringUtils.random(5, "abc");
        Assert.assertEquals(5, randomCustomString.length());
        Assert.assertTrue(randomCustomString.matches("[abc]+"));

        // Edge case
        randomCustomString = RandomStringUtils.random(0, "abc");
        Assert.assertEquals(0, randomCustomString.length());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyChars() {
        RandomStringUtils.random(5, "");
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomInvalidChars() {
        RandomStringUtils.random(5, 'a', 'a', false, false, new char[0]);
    }

}
