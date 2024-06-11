package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test cases for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    /**
     * Test for {@link RandomStringUtils#random(int)} method.
     */
    @Test
    public void testRandomInt() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for {@link RandomStringUtils#randomAscii(int)} method.
     */
    @Test
    public void testRandomAsciiInt() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphabetic(int)} method.
     */
    @Test
    public void testRandomAlphabeticInt() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphanumeric(int)} method.
     */
    @Test
    public void testRandomAlphanumericInt() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomNumeric(int)} method.
     */
    @Test
    public void testRandomNumericInt() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean)} method.
     */
    @Test
    public void testRandomIntIntBooleanBoolean() {
        String randomString = RandomStringUtils.random(10, 32, 126, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean)} method with specified length less than 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomIntIntBooleanBooleanNegativeLength() {
        RandomStringUtils.random(5);
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean)} method.
     */
    @Test
    public void testRandomIntIntIntBooleanBoolean() {
        String randomString = RandomStringUtils.random(10, 32, 126, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method.
     */
    @Test
    public void testRandomIntIntIntBooleanBooleanCharRandom() {
        String randomString = RandomStringUtils.random(10, 32, 126, true, true, new char[]{}, new Random());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method with null char array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomIntIntIntBooleanBooleanCharArrayNull() {
        RandomStringUtils.random(10, 32, 126, true, true, null, new Random());
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method with empty char array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomIntIntIntBooleanBooleanCharArrayEmpty() {
        RandomStringUtils.random(10, 32, 126, true, true, new char[0], new Random());
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)} method.
     */
    @Test
    public void testRandomIntIntBooleanBooleanCharRandom() {
        String randomString = RandomStringUtils.random(10, 32, true, true, new char[]{'a', 'b', 'c'}, new Random());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126 || (c == 'a' || c == 'b' || c == 'c'));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)} method with negative start index.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomIntIntBooleanBooleanCharRandomNegativeStart() {
        String randomString = RandomStringUtils.random(10, -1, true, true, new char[]{'a', 'b', 'c'}, new Random());
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)} method with end index greater than array length.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomIntIntBooleanBooleanCharRandomEndGreaterThanLength() {
        String randomString = RandomStringUtils.random(10, 3, true, true, new char[]{'a', 'b'}, new Random());
    }
}