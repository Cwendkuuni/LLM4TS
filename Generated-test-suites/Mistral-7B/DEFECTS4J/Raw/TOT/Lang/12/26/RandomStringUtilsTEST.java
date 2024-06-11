package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test random method with default character set.
     */
    @Test
    public void testRandomMethod() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertNotSame("", randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test randomAscii method with default character set.
     */
    @Test
    public void testRandomAsciiMethod() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertNotSame("", randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    /**
     * Test randomAlphabetic method with default character set.
     */
    @Test
    public void testRandomAlphabeticMethod() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertNotSame("", randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    /**
     * Test randomAlphanumeric method with default character set.
     */
    @Test
    public void testRandomAlphanumericMethod() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertNotSame("", randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    /**
     * Test randomNumeric method with default character set.
     */
    @Test
    public void testRandomNumericMethod() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertNotSame("", randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test random method with specified character set.
     */
    @Test
    public void testRandomMethodWithSpecifiedCharset() {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomString = RandomStringUtils.random(10, charSet.toCharArray());
        assertNotNull(randomString);
        assertNotSame("", randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(charSet.indexOf(c) != -1);
        }
    }

    /**
     * Test random method with specified character set and seeded random.
     */
    @Test
    public void testRandomMethodWithSpecifiedCharsetAndSeededRandom() {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomString = RandomStringUtils.random(10, 10, charSet.toCharArray(), new Random(12345));
        assertNotNull(randomString);
        assertNotSame("", randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(charSet.indexOf(c) != -1);
        }
    }

    /**
     * Test random method with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomMethodWithNegativeLength() {
        RandomStringUtils.random(-5);
    }

    /**
     * Test random method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomMethodWithEmptyCharacterSet() {
        RandomStringUtils.random(1, new char[0]);
    }
}