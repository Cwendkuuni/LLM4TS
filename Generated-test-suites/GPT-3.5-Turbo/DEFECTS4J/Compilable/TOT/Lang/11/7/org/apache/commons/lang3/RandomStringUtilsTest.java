package org.apache.commons.lang3;

import org.junit.Test;
import org.junit.Assert;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Test for creating a random string of specified length
        String randomString = RandomStringUtils.random(10);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        // Test for creating a random string of ASCII characters
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        Assert.assertNotNull(randomAsciiString);
        Assert.assertEquals(10, randomAsciiString.length());
        // Test that all characters in the string are ASCII characters
        for (char c : randomAsciiString.toCharArray()) {
            Assert.assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        // Test for creating a random string of alphabetic characters
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        Assert.assertNotNull(randomAlphabeticString);
        Assert.assertEquals(10, randomAlphabeticString.length());
        // Test that all characters in the string are alphabetic characters
        for (char c : randomAlphabeticString.toCharArray()) {
            Assert.assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        // Test for creating a random string of alphanumeric characters
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        Assert.assertNotNull(randomAlphanumericString);
        Assert.assertEquals(10, randomAlphanumericString.length());
        // Test that all characters in the string are alphanumeric characters
        for (char c : randomAlphanumericString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        // Test for creating a random string of numeric characters
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        Assert.assertNotNull(randomNumericString);
        Assert.assertEquals(10, randomNumericString.length());
        // Test that all characters in the string are numeric characters
        for (char c : randomNumericString.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        // Test for creating a random string with both letters and numbers
        String randomString = RandomStringUtils.random(10, true, true);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        // Test that all characters in the string are either letters or numbers
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomWithCustomCharacters() {
        // Test for creating a random string with custom characters
        char[] customChars = {'!', '@', '#', '$', '%', '^', '&', '*'};
        String randomString = RandomStringUtils.random(10, customChars);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        // Test that all characters in the string are one of the custom characters
        for (char c : randomString.toCharArray()) {
            boolean isCustomChar = false;
            for (char customChar : customChars) {
                if (c == customChar) {
                    isCustomChar = true;
                    break;
                }
            }
            Assert.assertTrue(isCustomChar);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        // Test for creating a random string with negative count
        RandomStringUtils.random(-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        // Test for creating a random string with an empty chars array
        RandomStringUtils.random(10, new char[]{});
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithInvalidCharsArrayIndices() {
        // Test for creating a random string with invalid chars array indices
        RandomStringUtils.random(10, 5, 10, true, false, new char[]{'a', 'b', 'c', 'd'});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCountAndEmptyCharsArray() {
        // Test for creating a random string with negative count and empty chars array
        RandomStringUtils.random(-10, new char[]{});
    }

}
