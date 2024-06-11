package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for RandomStringUtils class.
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String str = RandomStringUtils.random(10);
        assertNotNull(str);
        assertEquals(10, str.length());
    }

    @Test
    public void testRandomAscii() {
        String str = RandomStringUtils.randomAscii(10);
        assertNotNull(str);
        assertEquals(10, str.length());
        for (char c : str.toCharArray()) {
            assertTrue(Character.isASCII(c));
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String str = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(str);
        assertEquals(10, str.length());
        for (char c : str.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String str = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(str);
        assertEquals(10, str.length());
        for (char c : str.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String str = RandomStringUtils.randomNumeric(10);
        assertNotNull(str);
        assertEquals(10, str.length());
        for (char c : str.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithRange() {
        String str = RandomStringUtils.random(10, 48, 58);
        assertNotNull(str);
        assertEquals(10, str.length());
        for (char c : str.toCharArray()) {
            assertTrue(Character.isDigit(c) && (c >= 48 && c <= 58));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyChars() {
        RandomStringUtils.random(1, (char[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomInvalidStartEnd() {
        RandomStringUtils.random(1, 1, 0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomOutOfRange() {
        char[] chars = new char[10];
        RandomStringUtils.random(-1, chars.length + 2, chars);
    }
}