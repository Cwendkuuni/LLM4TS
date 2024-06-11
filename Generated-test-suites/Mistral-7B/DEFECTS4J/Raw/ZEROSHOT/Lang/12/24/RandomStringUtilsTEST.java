package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test method for {@link RandomStringUtils#random(int)}.
     */
    @Test
    public void testRandom() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length should be equal to the specified count", count, randomString.length());
        assertNotNull("Random string should not be null", randomString);
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        int count = 10;
        String randomString = RandomStringUtils.randomAscii(count);
        assertEquals("Length should be equal to the specified count", count, randomString.length());
        assertNotNull("Random string should not be null", randomString);
        for (char c : randomString.toCharArray()) {
            assertTrue("All characters should be ASCII", (c >= 0x20 && c <= 0x7E));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)}.
     */
    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Length should be equal to the specified count", count, randomString.length());
        assertNotNull("Random string should not be null", randomString);
        for (char c : randomString.toCharArray()) {
            assertTrue("All characters should be alphabetic", Character.isAlphabetic(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Length should be equal to the specified count", count, randomString.length());
        assertNotNull("Random string should not be null", randomString);
        for (char c : randomString.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                continue;
            }
            fail("All characters should be alphanumeric");
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomNumeric(int)}.
     */
    @Test
    public void testRandomNumeric() {
        int count = 10;
        String randomString = RandomStringUtils.randomNumeric(count);
        assertEquals("Length should be equal to the specified count", count, randomString.length());
        assertNotNull("Random string should not be null", randomString);
        for (char c : randomString.toCharArray()) {
            assertTrue("All characters should be numeric", Character.isDigit(c));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean)}.
     */
    @Test
    public void testRandomWithConstraints() {
        int count = 10;
        int start = 'a';
        int end = 'z';
        String randomString = RandomStringUtils.random(count, start, end, true, false);
        assertEquals("Length should be equal to the specified count", count, randomString.length());
        assertNotNull("Random string should not be null", randomString);
        for (char c : randomString.toCharArray()) {
            assertTrue("All characters should be alphabetic", Character.isAlphabetic(c));
            assertTrue("All characters should be within the specified range", (c >= start && c <= end));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithCustomCharsetAndSeed() {
        int count = 10;
        int start = '0';
        int end = '9';
        char[] customCharset = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Random customRandom = new Random(System.currentTimeMillis());
        String randomString = RandomStringUtils.random(count, start, end, true, true, customCharset, customRandom);
        assertEquals("Length should be equal to the specified count", count, randomString.length());
        assertNotNull("Random string should not be null", randomString);
        for (char c : randomString.toCharArray()) {
            assertTrue("All characters should be numeric", Character.isDigit(c));
            assertTrue("All characters should be within the specified range", (c >= start && c <= end));
        }
    }
}