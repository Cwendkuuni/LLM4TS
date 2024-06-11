package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the RandomStringUtils class.
 */
public class RandomStringUtilsTest {

    /**
     * Test for random method with default parameters.
     */
    @Test
    public void testRandomDefault() {
        String randomString = RandomStringUtils.random(5);
        Assert.assertEquals(5, randomString.length());
        // Add more assertions as needed to check the randomness of the generated string.
    }

    /**
     * Test for randomAscii method with default parameters.
     */
    @Test
    public void testRandomAsciiDefault() {
        String randomString = RandomStringUtils.randomAscii(5);
        Assert.assertEquals(5, randomString.length());
        Assert.assertTrue(randomString.matches(".*[\\x00-\\x7F]+.*"));
    }

    /**
     * Test for randomAlphabetic method with default parameters.
     */
    @Test
    public void testRandomAlphabeticDefault() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        Assert.assertEquals(5, randomString.length());
        Assert.assertTrue(randomString.matches(".*[a-zA-Z]+.*"));
    }

    /**
     * Test for randomAlphanumeric method with default parameters.
     */
    @Test
    public void testRandomAlphanumericDefault() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        Assert.assertEquals(5, randomString.length());
        Assert.assertTrue(randomString.matches(".*[0-9a-zA-Z]+.*"));
    }

    /**
     * Test for randomNumeric method with default parameters.
     */
    @Test
    public void testRandomNumericDefault() {
        String randomString = RandomStringUtils.randomNumeric(5);
        Assert.assertEquals(5, randomString.length());
        Assert.assertTrue(randomString.matches(".*[0-9]+.*"));
    }

    /**
     * Test for random method with custom length and character set.
     */
    @Test
    public void testRandomCustom() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(5, chars.toCharArray());
        Assert.assertEquals(5, randomString.length());
        Assert.assertTrue(randomString.matches(".*[" + new String(chars) + "]+.*"));
    }

    /**
     * Test for random method with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-5);
    }

    /**
     * Test for random method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharacterSet() {
        RandomStringUtils.random(5, new char[0]);
    }

    /**
     * Test for random method with surrogate characters.
     */
    @Test
    public void testRandomSurrogateCharacters() {
        String randomString = RandomStringUtils.random(1, new char[]{'😃', '😋', '🤔'});
        Assert.assertEquals(1, randomString.length());
        // Add more assertions as needed to check the randomness of the generated string and the presence of surrogate characters.
    }
}