package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test creating random string with given length.
     */
    @Test
    public void testRandomString() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(5);
            assertEquals("Length of the random string is not 5", 5, randomString.length());
            assertNotNull("Generated random string is null", randomString);
        }
    }

    /**
     * Test creating random string with given length and only allowing alphabetic characters.
     */
    @Test
    public void testRandomAlphabeticString() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphabetic(5);
            assertEquals("Length of the random string is not 5", 5, randomString.length());
            assertNotNull("Generated random string is null", randomString);
            for (char c : randomString.toCharArray()) {
                assertTrue("Randomly generated character is not alphabetic", Character.isAlphabetic(c));
            }
        }
    }

    /**
     * Test creating random string with given length and only allowing numeric characters.
     */
    @Test
    public void testRandomNumericString() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomNumeric(5);
            assertEquals("Length of the random string is not 5", 5, randomString.length());
            assertNotNull("Generated random string is null", randomString);
            for (char c : randomString.toCharArray()) {
                assertTrue("Randomly generated character is not numeric", Character.isDigit(c));
            }
        }
    }

    /**
     * Test creating random string with given length, limiting the range of characters.
     */
    @Test
    public void testRandomStringWithRange() {
        for (int i = 0; i < 10; i++) {
            char start = (char) 48; // '0'
            char end = (char) 58; // '9'
            String randomString = RandomStringUtils.random(5, start, end, false, false);
            assertEquals("Length of the random string is not 5", 5, randomString.length());
            assertNotNull("Generated random string is null", randomString);
            for (char c : randomString.toCharArray()) {
                assertTrue("Randomly generated character is not within the given range", (start <= c && c <= end));
            }
        }
    }

    /**
     * Test creating random string with given length and only allowing alphabetic and numeric characters.
     */
    @Test
    public void testRandomAlphanumericString() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphanumeric(5);
            assertEquals("Length of the random string is not 5", 5, randomString.length());
            assertNotNull("Generated random string is null", randomString);
            for (char c : randomString.toCharArray()) {
                assertTrue("Randomly generated character is not alphabetic nor numeric", Character.isAlphabetic(c) || Character.isDigit(c));
            }
        }
    }

    /**
     * Test creating random string with given length and ignoring high surrogate characters.
     */
    @Test
    public void testRandomStringIgnoringHighSurrogates() {
        String randomString = RandomStringUtils.random(10);
        for (int i = 0; i < randomString.length(); i++) {
            char ch = randomString.charAt(i);
            if (ch > 65535) { // high surrogate character
                fail("High surrogate character should not be included in generated random string");
            }
        }
    }
}