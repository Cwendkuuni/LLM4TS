package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    /**
     * Test for {@link RandomStringUtils#random(int)} method.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertEquals(5, randomString.length());
        // basic functionality check
        assertFalse(randomString.isEmpty());
        // edge cases
        String emptyString = RandomStringUtils.random(0);
        assertTrue(emptyString.isEmpty());
        String longString = RandomStringUtils.random(Integer.MAX_VALUE);
        assertTrue(longString.length() > Integer.MAX_VALUE() / 2);
        // error scenarios
        try {
            RandomStringUtils.random(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAscii(int)} method.
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertEquals(5, randomAsciiString.length());
        // basic functionality check
        assertFalse(randomAsciiString.isEmpty());
        // edge cases
        String emptyString = RandomStringUtils.randomAscii(0);
        assertTrue(emptyString.isEmpty());
        String longString = RandomStringUtils.randomAscii(Integer.MAX_VALUE);
        assertTrue(longString.length() > Integer.MAX_VALUE() / 2);
        // error scenarios
        try {
            RandomStringUtils.randomAscii(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphabetic(int)} method.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, randomAlphabeticString.length());
        // basic functionality check
        assertFalse(randomAlphabeticString.isEmpty());
        // edge cases
        String emptyString = RandomStringUtils.randomAlphabetic(0);
        assertTrue(emptyString.isEmpty());
        String longString = RandomStringUtils.randomAlphabetic(Integer.MAX_VALUE);
        assertTrue(longString.length() > Integer.MAX_VALUE() / 2);
        // error scenarios
        try {
            RandomStringUtils.randomAlphabetic(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphanumeric(int)} method.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, randomAlphanumericString.length());
        // basic functionality check
        assertFalse(randomAlphanumericString.isEmpty());
        // edge cases
        String emptyString = RandomStringUtils.randomAlphanumeric(0);
        assertTrue(emptyString.isEmpty());
        String longString = RandomStringUtils.randomAlphanumeric(Integer.MAX_VALUE);
        assertTrue(longString.length() > Integer.MAX_VALUE() / 2);
        // error scenarios
        try {
            RandomStringUtils.randomAlphanumeric(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomNumeric(int)} method.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertEquals(5, randomNumericString.length());
        // basic functionality check
        assertFalse(randomNumericString.isEmpty());
        // edge cases
        String emptyString = RandomStringUtils.randomNumeric(0);
        assertTrue(emptyString.isEmpty());
        String longString = RandomStringUtils.randomNumeric(Integer.MAX_VALUE);
        assertTrue(longString.length() > Integer.MAX_VALUE() / 2);
        // error scenarios
        try {
            RandomStringUtils.randomNumeric(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean)}
     * method.
     */
    @Test
    public void testRandomWithOptions() {
        String randomString = RandomStringUtils.random(5, 0, 26, true, false);
        assertEquals(5, randomString.length());
        String randomStringWithNumbers = RandomStringUtils.random(5, 0, 26, true, true);
        assertEquals(5, randomStringWithNumbers.length());

        // edge cases
        String emptyString = RandomStringUtils.random(0, 0, 26, true, false);
        assertTrue(emptyString.isEmpty());
        String longString = RandomStringUtils.random(Integer.MAX_VALUE, 0, 26, true, false);
        assertTrue(longString.length() > Integer.MAX_VALUE() / 2);

        // error scenarios
        try {
            RandomStringUtils.random(-1, 0, 26, true, false);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            RandomStringUtils.random(5, -1, 26, true, false);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            RandomStringUtils.random(5, 0, Integer.MIN_VALUE, true, false);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}
     * method.
     */
    @Test
    public void testRandomWithCharacterSet() {
        String randomString = RandomStringUtils.random(5, 0, 26, true, false, new char[]{}, Randomizer.DEFAULT);
        assertEquals(5, randomString.length());

        String randomStringWithSpecifiedCharset = RandomStringUtils.random(5, 0, 26, true, false, new char[]{'a', 'b', 'c'}, Randomizer.DEFAULT);
        assertEquals(5, randomStringWithSpecifiedCharset.length());

        // edge cases
        String emptyString = RandomStringUtils.random(0, 0, 26, true, false, new char[0], Randomizer.DEFAULT);
        assertTrue(emptyString.isEmpty());
        String longString = RandomStringUtils.random(Integer.MAX_VALUE, 0, 26, true, false, new char[27], Randomizer.DEFAULT);
        assertTrue(longString.length() > Integer.MAX_VALUE() / 2);

        // error scenarios
        try {
            RandomStringUtils.random(-1, 0, 26, true, false, new char[0], Randomizer.DEFAULT);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            RandomStringUtils.random(5, -1, 26, true, false, new char[]{'a', 'b', 'c'}, Randomizer.DEFAULT);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            RandomStringUtils.random(5, 0, Integer.MIN_VALUE, true, false, new char[0], Randomizer.DEFAULT);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}