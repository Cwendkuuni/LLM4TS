package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    /**
     * Test for random method with default arguments.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < 5; i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test for randomAscii method with default arguments.
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < 5; i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)) || Character.isWhitespace(randomString.charAt(i)) || Character.isISOControl(randomString.charAt(i)) || Character.isHighSurrogate(randomString.charAt(i)) || Character.isLowSurrogate(randomString.charAt(i)));
        }
    }

    /**
     * Test for randomAlphabetic method with default arguments.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < 5; i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)));
        }
    }

    /**
     * Test for randomAlphanumeric method with default arguments.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < 5; i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test for randomNumeric method with default arguments.
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < 5; i++) {
            assertTrue(Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test for random method with custom arguments.
     */
    @Test
    public void testRandomCustom() {
        String randomString = RandomStringUtils.random(5, 1, 10, true, false);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < 5; i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
            assertTrue(randomString.charAt(i) >= '1' && randomString.charAt(i) <= '10');
        }
    }

    /**
     * Test for random method with custom source of randomness.
     */
    @Test
    public void testRandomCustomSource() {
        Random customRandom = new Random(123L);
        String randomString = RandomStringUtils.random(5, 1, 10, true, false, null, customRandom);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < 5; i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
            assertTrue(randomString.charAt(i) >= '1' && randomString.charAt(i) <= '10');
        }
    }

    /**
     * Test for random method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharacterSet() {
        RandomStringUtils.random(5, new char[0]);
    }

    /**
     * Test for random method with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-5);
    }

    /**
     * Test for randomAscii method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAsciiEmptyCharacterSet() {
        RandomStringUtils.randomAscii(5, new char[0]);
    }

    /**
     * Test for randomAlphabetic method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphabeticEmptyCharacterSet() {
        RandomStringUtils.randomAlphabetic(5, new char[0]);
    }

    /**
     * Test for randomAlphanumeric method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphanumericEmptyCharacterSet() {
        RandomStringUtils.randomAlphanumeric(5, new char[0]);
    }

    /**
     * Test for randomNumeric method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNumericEmptyCharacterSet() {
        RandomStringUtils.randomNumeric(5, new char[0]);
    }
}