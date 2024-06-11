package org.apache.commons.lang3;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Test if returned string has correct length
        Assert.assertEquals(5, RandomStringUtils.random(5).length());

        // Test if returned string has only valid characters
        String randomString = RandomStringUtils.random(10);
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomAscii() {
        // Test if returned string has correct length
        Assert.assertEquals(5, RandomStringUtils.randomAscii(5).length());

        // Test if returned string has only ascii characters
        String randomString = RandomStringUtils.randomAscii(10);
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        // Test if returned string has correct length
        Assert.assertEquals(5, RandomStringUtils.randomAlphabetic(5).length());

        // Test if returned string has only alphabetic characters
        String randomString = RandomStringUtils.randomAlphabetic(10);
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        // Test if returned string has correct length
        Assert.assertEquals(5, RandomStringUtils.randomAlphanumeric(5).length());

        // Test if returned string has only alphanumeric characters
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        // Test if returned string has correct length
        Assert.assertEquals(5, RandomStringUtils.randomNumeric(5).length());

        // Test if returned string has only numeric characters
        String randomString = RandomStringUtils.randomNumeric(10);
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        // Test if returned string has correct length
        Assert.assertEquals(5, RandomStringUtils.random(5, true, true).length());

        // Test if returned string has only alphanumeric characters
        String randomString = RandomStringUtils.random(10, true, true);
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomWithStartAndEnd() {
        // Test if returned string has correct length
        Assert.assertEquals(5, RandomStringUtils.random(5, 65, 70, false, false).length());

        // Test if returned string has only characters between start and end
        String randomString = RandomStringUtils.random(10, 65, 70, false, false);
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(c >= 65 && c <= 70);
        }
    }

    @Test
    public void testRandomWithChars() {
        char[] chars = {'a', 'b', 'c'};

        // Test if returned string has correct length
        Assert.assertEquals(5, RandomStringUtils.random(5, chars).length());

        // Test if returned string has only characters from chars array
        String randomString = RandomStringUtils.random(10, chars);
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(c == 'a' || c == 'b' || c == 'c');
        }
    }

    @Test
    public void testRandomWithRandom() {
        // Test if returned string has correct length
        Assert.assertEquals(5, RandomStringUtils.random(5, 0, 0, false, false, null, new java.util.Random()).length());

        // Test if returned string has only valid characters
        String randomString = RandomStringUtils.random(10, 0, 0, false, false, null, new java.util.Random());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomWithCountLessThanZero() {
        try {
            RandomStringUtils.random(-1);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testRandomWithCharsArrayEmpty() {
        try {
            RandomStringUtils.random(10, new char[]{});
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testRandomWithCharsStringEmpty() {
        try {
            RandomStringUtils.random(10, "");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }
}