package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test of random method, of class RandomStringUtils.
     */
    @Test
    public void testRandom() {
        final int count = 10;
        String expected = RandomStringUtils.random(count);
        String actual = RandomStringUtils.random(count);
        assertEquals(count, actual.length());
        assertNotSame(expected, actual);
    }

    /**
     * Test of randomAscii method, of class RandomStringUtils.
     */
    @Test
    public void testRandomAscii() {
        final int count = 10;
        String expected = RandomStringUtils.randomAscii(count);
        String actual = RandomStringUtils.randomAscii(count);
        assertEquals(count, actual.length());
        assertNotSame(expected, actual);
    }

    /**
     * Test of randomAlphabetic method, of class RandomStringUtils.
     */
    @Test
    public void testRandomAlphabetic() {
        final int count = 10;
        String expected = RandomStringUtils.randomAlphabetic(count);
        String actual = RandomStringUtils.randomAlphabetic(count);
        assertEquals(count, actual.length());
        assertNotSame(expected, actual);
    }

    /**
     * Test of randomAlphanumeric method, of class RandomStringUtils.
     */
    @Test
    public void testRandomAlphanumeric() {
        final int count = 10;
        String expected = RandomStringUtils.randomAlphanumeric(count);
        String actual = RandomStringUtils.randomAlphanumeric(count);
        assertEquals(count, actual.length());
        assertNotSame(expected, actual);
    }

    /**
     * Test of randomNumeric method, of class RandomStringUtils.
     */
    @Test
    public void testRandomNumeric() {
        final int count = 10;
        String expected = RandomStringUtils.randomNumeric(count);
        String actual = RandomStringUtils.randomNumeric(count);
        assertEquals(count, actual.length());
        assertNotSame(expected, actual);
    }

    /**
     * Test of random method with custom character set and false for letters and numbers,
     * expected to generate a random string of all printable ASCII characters.
     */
    @Test
    public void testRandomPrintableAscii() {
        final int count = 10;
        String expected = "QWERTYUIOPqwertyuiop1234567890";
        String actual = RandomStringUtils.random(count, 32, 126, false, false);
        assertEquals(count, actual.length());
        assertNotSame(expected, actual);
    }

    /**
     * Test of random method with custom character set and true for letters and false for numbers,
     * expected to generate a random string of only alphabetic characters.
     */
    @Test
    public void testRandomAlphabeticWithCustomSet() {
        final int count = 10;
        String expected = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String actual = RandomStringUtils.random(count, 97, 122, true, false, null, new Random());
        assertEquals(count, actual.length());
        assertNotSame(expected, actual);
    }

    /**
     * Test of random method with custom character set and false for letters and true for numbers,
     * expected to generate a random string of only numeric characters.
     */
    @Test
    public void testRandomNumericWithCustomSet() {
        final int count = 10;
        String expected = "0123456789";
        String actual = RandomStringUtils.random(count, 48, 57, false, true, null, new Random());
        assertEquals(count, actual.length());
        assertNotSame(expected, actual);
    }

    /**
     * Test of random method with custom character set and true for both letters and numbers,
     * expected to generate a random string of alphanumeric characters.
     */
    @Test
    public void testRandomAlphanumericWithCustomSet() {
        final int count = 10;
        String expected = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String actual = RandomStringUtils.random(count, 97, 122, true, true, null, new Random());
        assertEquals(count, actual.length());
        assertNotSame(expected, actual);
    }
}