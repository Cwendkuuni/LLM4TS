package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test for random method with default arguments.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
    }

    /**
     * Test for randomAscii method with default arguments.
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    /**
     * Test for randomAlphabetic method with default arguments.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test for randomAlphanumeric method with default arguments.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for randomNumeric method with default arguments.
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test for random method with custom arguments.
     */
    @Test
    public void testRandomCustom() {
        String randomString = RandomStringUtils.random(10, 'a', 'z');
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test for random method with custom start and end arguments.
     */
    @Test
    public void testRandomCustomStartEnd() {
        String randomString = RandomStringUtils.random(10, '0', '9');
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test for random method with custom start, end and character array arguments.
     */
    @Test
    public void testRandomCustomStartEndWithArray() {
        String randomString = RandomStringUtils.random(10, 'A', 'Z', new char[]{'!', '@', '#'});
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue((c == '!' || c == '@' || c == '#' || Character.isAlphabetic(c)));
        }
    }

    /**
     * Test for random method with custom character array and random instance arguments.
     */
    @Test
    public void testRandomCustomArrayAndRandomInstance() {
        char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        Random random = new Random();
        String randomString = RandomStringUtils.random(10, chars, random);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Arrays.binarySearch(chars, c) >= 0);
        }
    }

    /**
     * Test for exception handling when count is negative number.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    /**
     * Test for exception handling when custom character array is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharacterArray() {
        RandomStringUtils.random(1, new char[0]);
    }
}