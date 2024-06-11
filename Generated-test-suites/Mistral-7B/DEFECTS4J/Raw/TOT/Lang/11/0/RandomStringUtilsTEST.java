package org.apache.commons.lang3.test;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Random;

/**
 * Test class for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test random method with default options.
     */
    @Test
    public void testRandom() {
        String string = RandomStringUtils.random(10);
        Assert.assertEquals(10, string.length());
        for (char c : string.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || Character.isWhitespace(c) || c == '\u0000');
        }
    }

    /**
     * Test randomAscii method with default options.
     */
    @Test
    public void testRandomAscii() {
        String string = RandomStringUtils.randomAscii(10);
        Assert.assertEquals(10, string.length());
        for (char c : string.toCharArray()) {
            assertTrue(Character.isWhitespace(c) || Character.isISOControl(c) || Character.isAlphabetic(c) || Character.isDigit(c) || c == '\u0000');
        }
    }

    /**
     * Test randomAlphabetic method with default options.
     */
    @Test
    public void testRandomAlphabetic() {
        String string = RandomStringUtils.randomAlphabetic(10);
        Assert.assertEquals(10, string.length());
        for (char c : string.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test randomAlphanumeric method with default options.
     */
    @Test
    public void testRandomAlphanumeric() {
        String string = RandomStringUtils.randomAlphanumeric(10);
        Assert.assertEquals(10, string.length());
        for (char c : string.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '\u0000');
        }
    }

    /**
     * Test randomNumeric method with default options.
     */
    @Test
    public void testRandomNumeric() {
        String string = RandomStringUtils.randomNumeric(10);
        Assert.assertEquals(10, string.length());
        for (char c : string.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test random method with custom options.
     */
    @Test
    public void testRandomCustom() {
        String string = RandomStringUtils.random(10, false, true, "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray());
        Assert.assertEquals(10, string.length());
        for (char c : string.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test random method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmpty() {
        RandomStringUtils.random(10, null);
    }

    /**
     * Test random method with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegative() {
        RandomStringUtils.random(-1);
    }

    /**
     * Test random method with custom random instance.
     */
    @Test
    public void testRandomCustomRandom() {
        Random random = new Random(1L);
        String string = RandomStringUtils.random(10, false, true, null, random);
        Assert.assertEquals(10, string.length());
        for (char c : string.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '\u0000');
        }
    }
}