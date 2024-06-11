package org.apache.commons.lang3.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Tests for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test for creating random strings with different parameters.
     */
    @Test
    public void testRandomStrings() {
        // Test creating random string with default parameters
        String randomString = RandomStringUtils.random(10);
        Assert.assertEquals(10, randomString.length());
        Assert.assertTrue(randomString.matches("[\\p{L}\\p{N}]+")); // Matches any letter or number

        // Test creating random string with specific character set
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        Assert.assertEquals(10, randomAsciiString.length());
        Assert.assertTrue(randomAsciiString.matches("[\\x20-\\x7E]+")); // Matches ASCII printable characters

        // Test creating random string with alphabetic characters only
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        Assert.assertEquals(10, randomAlphabeticString.length());
        Assert.assertTrue(randomAlphabeticString.matches("[a-zA-Z]+")); // Matches only alphabetic characters

        // Test creating random string with alphanumeric characters only
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        Assert.assertEquals(10, randomAlphanumericString.length());
        Assert.assertTrue(randomAlphanumericString.matches("[a-zA-Z0-9]+")); // Matches only alphanumeric characters

        // Test creating random string with numeric characters only
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        Assert.assertEquals(10, randomNumericString.length());
        Assert.assertTrue(randomNumericString.matches("[0-9]+")); // Matches only numeric characters
    }

    /**
     * Test for creating random strings with custom character set and length.
     */
    @Test
    public void testRandomStringWithCustomSet() {
        String customSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomString = RandomStringUtils.random(5, customSet.toCharArray());
        Assert.assertEquals(5, randomString.length());
        Assert.assertTrue(randomString.matches("[A-Za-z0-9]+")); // Matches only characters in the custom set
    }

    /**
     * Test for creating random strings with custom character set, length, start index, and end index.
     */
    @Test
    public void testRandomStringWithCustomSetAndRange() {
        String customSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomString = RandomStringUtils.random(5, 5, 10, customSet.toCharArray());
        Assert.assertEquals(5, randomString.length());
        Assert.assertTrue(randomString.matches("[A-Za-z0-9]+")); // Matches only characters in the custom set
        Assert.assertTrue(randomString.matches(".*[5,6,7,8,9].*")); // At least one character from the specified range is present
    }

    /**
     * Test for creating random strings with custom character set, length, and seed.
     */
    @Test
    public void testRandomStringWithCustomSetAndSeed() {
        String customSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String seed = "TestSeed";
        String randomString = RandomStringUtils.random(5, customSet.toCharArray(), seed);
        Assert.assertEquals(5, randomString.length());
        Assert.assertTrue(randomString.matches("[A-Za-z0-9]+")); // Matches only characters in the custom set
        // TODO: Implement a method to compare the seed used to generate the string
    }

    /**
     * Test for creating random strings with an empty custom character set.
     * This should throw an IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptyCustomSet() {
        String emptyCustomSet = "";
        RandomStringUtils.random(5, emptyCustomSet.toCharArray());
    }

    /**
     * Test for creating random strings with a negative length.
     * This should throw an IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNegativeLength() {
        RandomStringUtils.random(-5);
    }
}