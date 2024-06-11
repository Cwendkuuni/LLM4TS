package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test for {@link RandomStringUtils#random(int)}.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < 5; i++) {
            assertFalse(randomString.charAt(i) < 0 || randomString.charAt(i) > 127);
        }
        String randomString2 = RandomStringUtils.random(5);
        assertNotSame(randomString, randomString2);
    }

    /**
     * Test for {@link RandomStringUtils#randomAscii(int)}.
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < 5; i++) {
            assertTrue(randomString.charAt(i) >= 32 && randomString.charAt(i) <= 126);
        }
        String randomString2 = RandomStringUtils.randomAscii(5);
        assertNotSame(randomString, randomString2);
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphabetic(int)}.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < 5; i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)));
        }
        String randomString2 = RandomStringUtils.randomAlphabetic(5);
        assertNotSame(randomString, randomString2);
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphanumeric(int)}.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < 5; i++) {
            assertTrue(Character.isLetterOrDigit(randomString.charAt(i)));
        }
        String randomString2 = RandomStringUtils.randomAlphanumeric(5);
        assertNotSame(randomString, randomString2);
    }

    /**
     * Test for {@link RandomStringUtils#randomNumeric(int)}.
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < 5; i++) {
            assertTrue(Character.isDigit(randomString.charAt(i)));
        }
        String randomString2 = RandomStringUtils.randomNumeric(5);
        assertNotSame(randomString, randomString2);
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean)}.
     */
    @Test
    public void testRandomWithBounds() {
        String randomString = RandomStringUtils.random(3, 1, 5, true, false);
        assertEquals(3, randomString.length());
        for (int i = 0; i < 3; i++) {
            assertTrue(Character.isLetter(randomString.charAt(i)));
        }
        String randomString2 = RandomStringUtils.random(3, 1, 5, true, false);
        assertNotSame(randomString, randomString2);
        String randomString3 = RandomStringUtils.random(3, 1, 5, true, true);
        assertEquals(3, randomString3.length());
        for (int i = 0; i < 3; i++) {
            assertTrue(Character.isLetterOrDigit(randomString3.charAt(i)));
        }
        String randomString4 = RandomStringUtils.random(3, 1, 5, true, true);
        assertNotSame(randomString3, randomString4);
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)}.
     */
    @Test
    public void testRandomWithCustomCharset() {
        char[] customCharset = new char[]{'a', 'b', 'c', 'd', 'e'};
        String randomString = RandomStringUtils.random(5, 0, customCharset.length, true, false, customCharset, new Random());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < 5; i++) {
            assertTrue(i < customCharset.length && randomString.charAt(i) == customCharset[i]);
        }
        String randomString2 = RandomStringUtils.random(5, 0, customCharset.length, true, false, customCharset, new Random());
        assertNotSame(randomString, randomString2);
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} with empty charset.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharset() {
        RandomStringUtils.random(5, 0, 0, true, false, null, new Random());
    }
}