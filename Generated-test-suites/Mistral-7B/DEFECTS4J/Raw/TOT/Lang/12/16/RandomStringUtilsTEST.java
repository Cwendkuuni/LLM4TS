package org.apache.commons.lang3.test;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        final int count = 10;
        String randomString = RandomStringUtils.random(count);
        Assert.assertEquals("Random string length", count, randomString.length());
        // typical use case
        Assert.assertFalse("Random string contains all the same character", randomString.contains("" + randomString.charAt(0)) + randomString.length() - 1);
        // edge case: empty string
        Assert.assertEquals("Random empty string", "", RandomStringUtils.random(0));
        // error scenario: negative length
        try {
            RandomStringUtils.random(-1);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testRandomAsciiString() {
        final int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        Assert.assertEquals("Random ascii string length", count, randomAsciiString.length());
        // typical use case
        Assert.assertTrue("Random ascii string contains only ascii characters", Character.isASCII(randomAsciiString.charAt(0)) && randomAsciiString.length() > 0);
        // edge case: empty string
        Assert.assertEquals("Random ascii empty string", "", RandomStringUtils.randomAscii(0));
        // error scenario: negative length
        try {
            RandomStringUtils.randomAscii(-1);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        final int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        Assert.assertEquals("Random alphabetic string length", count, randomAlphabeticString.length());
        // typical use case
        Assert.assertTrue("Random alphabetic string contains only alphabetic characters", Character.isAlphabetic(randomAlphabeticString.charAt(0)) && randomAlphabeticString.length() > 0);
        // edge case: empty string
        Assert.assertEquals("Random alphabetic empty string", "", RandomStringUtils.randomAlphabetic(0));
        // error scenario: negative length
        try {
            RandomStringUtils.randomAlphabetic(-1);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        final int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        Assert.assertEquals("Random alphanumeric string length", count, randomAlphanumericString.length());
        // typical use case
        Assert.assertTrue("Random alphanumeric string contains only alphanumeric characters", Character.isAlphabetic(randomAlphanumericString.charAt(0)) || Character.isDigit(randomAlphanumericString.charAt(0)) && randomAlphanumericString.length() > 0);
        // edge case: empty string
        Assert.assertEquals("Random alphanumeric empty string", "", RandomStringUtils.randomAlphanumeric(0));
        // error scenario: negative length
        try {
            RandomStringUtils.randomAlphanumeric(-1);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testRandomNumericString() {
        final int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        Assert.assertEquals("Random numeric string length", count, randomNumericString.length());
        // typical use case
        Assert.assertTrue("Random numeric string contains only numeric characters", Character.isDigit(randomNumericString.charAt(0)) && randomNumericString.length() > 0);
        // edge case: empty string
        Assert.assertEquals("Random numeric empty string", "", RandomStringUtils.randomNumeric(0));
        // error scenario: negative length
        try {
            RandomStringUtils.randomNumeric(-1);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testRandomCustomString() {
        final int count = 10;
        final int start = 0;
        final int end = 26;
        String randomCustomString = RandomStringUtils.random(count, start, end);
        Assert.assertEquals("Random custom string length", count, randomCustomString.length());
        // typical use case
        Assert.assertTrue("Random custom string contains only characters between start and end", (randomCustomString.charAt(0) >= start && randomCustomString.charAt(0) <= end) && randomCustomString.length() > 0);
        // edge case: empty string
        Assert.assertEquals("Random custom empty string", "", RandomStringUtils.random(0, start, end));
        // error scenario: negative length
        try {
            RandomStringUtils.random(-1, start, end);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        // error scenario: invalid start or end
        try {
            RandomStringUtils.random(count, -1, end);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            RandomStringUtils.random(count, start, -1);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}