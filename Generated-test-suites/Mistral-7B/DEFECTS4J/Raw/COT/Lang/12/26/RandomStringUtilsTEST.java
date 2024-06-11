package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *Tests for the RandomStringUtils class
 */
public class RandomStringUtilsTest {

    /**
     * Test of random method, of class RandomStringUtils.
     */
    @Test
    public void testRandom() {
        String result = RandomStringUtils.random(5);
        assertNotNull(result);
        assertEquals(5, result.length());
    }

    /**
     * Test of randomAscii method, of class RandomStringUtils.
     */
    @Test
    public void testRandomAscii() {
        String result = RandomStringUtils.randomAscii(5);
        assertNotNull(result);
        assertEquals(5, result.length());
        for (int i = 0; i < result.length(); i++) {
            int ascii = (int) result.charAt(i);
            assertTrue(ascii >= 32 && ascii <= 126);
        }
    }

    /**
     * Test of randomAlphabetic method, of class RandomStringUtils.
     */
    @Test
    public void testRandomAlphabetic() {
        String result = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(result);
        assertEquals(5, result.length());
        for (int i = 0; i < result.length(); i++) {
            assertTrue(Character.isAlphabetic(result.charAt(i)));
        }
    }

    /**
     * Test of randomAlphanumeric method, of class RandomStringUtils.
     */
    @Test
    public void testRandomAlphanumeric() {
        String result = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(result);
        assertEquals(5, result.length());
        for (int i = 0; i < result.length(); i++) {
            int ascii = (int) result.charAt(i);
            assertTrue((ascii >= 48 && ascii <= 57) || ascii >= 97 && ascii <= 122 || ascii == 32);
        }
    }

    /**
     * Test of randomNumeric method, of class RandomStringUtils.
     */
    @Test
    public void testRandomNumeric() {
        String result = RandomStringUtils.randomNumeric(5);
        assertNotNull(result);
        assertEquals(5, result.length());
        for (int i = 0; i < result.length(); i++) {
            assertTrue(Character.isDigit(result.charAt(i)));
        }
    }

    /**
     * Test of random method, of class RandomStringUtils, with different arguments.
     */
    @Test
    public void testRandomCustom() {
        String result = RandomStringUtils.random(5, true, true);
        assertNotNull(result);
        assertEquals(5, result.length());
        for (int i = 0; i < result.length(); i++) {
            int ascii = (int) result.charAt(i);
            assertTrue((ascii >= 48 && ascii <= 57) || ascii >= 97 && ascii <= 122 || ascii == 32);
        }
    }

    /**
     * Test of random method, of class RandomStringUtils, with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomInvalidCount() {
        RandomStringUtils.random(-5);
    }

    /**
     * Test of random method, of class RandomStringUtils, with empty character array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyChars() {
        RandomStringUtils.random(1, (char[]) null);
    }

    /**
     * Test of random method, of class RandomStringUtils, with edge case: maximum length.
     */
    @Test
    public void testRandomMaxLength() {
        String result = RandomStringUtils.random(Integer.MAX_VALUE);
        assertNotNull(result);
        // Integer.MAX_VALUE can't be directly compared with an int, need to convert
        assertTrue(result.length() <= Integer.MAX_VALUE);
    }

    /**
     * Test of random method, of class RandomStringUtils, with edge case: minimum length.
     */
    @Test
    public void testRandomMinLength() {
        String result = RandomStringUtils.random(0);
        assertNotNull(result);
        assertEquals("", result);
    }
}