package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    /**
     * Test for random method with default options.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertNotSame("", randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for randomAscii method with default options.
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertNotSame("", randomAsciiString);
        assertEquals(10, randomAsciiString.length());
    }

    /**
     * Test for randomAlphabetic method with default options.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertNotSame("", randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
    }

    /**
     * Test for randomAlphanumeric method with default options.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertNotSame("", randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
    }

    /**
     * Test for randomNumeric method with default options.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertNotSame("", randomNumericString);
        assertEquals(10, randomNumericString.length());
    }

    /**
     * Test for random method with specified length and character set.
     */
    @Test
    public void testRandomWithSpecifiedLengthAndCharacterSet() {
        String randomString = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 10);
        assertNotNull(randomString);
        assertNotSame("", randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetter(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for random method with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeLength() {
        RandomStringUtils.random(-1);
    }

    /**
     * Test for random method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterSet() {
        RandomStringUtils.random(new char[0]);
    }

    /**
     * Test for random method with specified length and character set, including edge cases.
     */
    @Test
    public void testRandomWithSpecifiedLengthAndCharacterSetEdgeCases() {
        String randomString = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 0);
        assertEquals("", randomString);

        randomString = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ", Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, randomString.length());
    }

    /**
     * Test for random method with specified length and character set, including exception scenarios.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithSpecifiedLengthAndCharacterSetExceptionScenarios() {
        char[] chars = new char[52];
        RandomStringUtils.random(chars, 10, 0);
    }
}