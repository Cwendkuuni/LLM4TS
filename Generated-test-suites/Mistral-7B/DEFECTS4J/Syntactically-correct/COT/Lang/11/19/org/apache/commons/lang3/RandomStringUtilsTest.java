package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test for random method with default settings.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for randomAscii method.
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(c >= 32 && c <= 126);
        }
    }

    /**
     * Test for randomAlphabetic method.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    /**
     * Test for randomAlphanumeric method.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for randomNumeric method.
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    /**
     * Test for random method with custom length, letters, and numbers.
     */
    @Test
    public void testRandomWithCustomSettings() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for random method with custom length, start, end, letters, and numbers.
     */
    @Test
    public void testRandomWithCustomSettings2() {
        String randomString = RandomStringUtils.random(10, 'a', 'z', true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c) || c == 'a' || c == 'z');
        }
    }

    /**
     * Test for random method with custom length, start, end, letters, numbers and custom chars array.
     */
    @Test
    public void testRandomWithCustomSettings3() {
        char[] chars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
        String randomString = RandomStringUtils.random(10, 0, chars.length, true, true, chars, null);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c) || (Arrays.binarySearch(chars, c) >= 0));
        }
    }

    /**
     * Test for random method with negative count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-5);
    }

    /**
     * Test for random method with empty chars array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharsArray() {
        RandomStringUtils.random(5, (char[]) null);
    }

    /**
     * Test for random method with start > end.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStartEnd() {
        RandomStringUtils.random(5, 'z', 'a');
    }

    /**
     * Test for random method with negative start index.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomNegativeStartIndex() {
        char[] chars = new char[]{'a', 'b', 'c', 'd', 'e'};
        RandomStringUtils.random(5, -1, chars.length, false, false, chars, null);
    }

    /**
     * Test for random method with negative end index.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomNegativeEndIndex() {
        char[] chars = new char[]{'a', 'b', 'c', 'd', 'e'};
        RandomStringUtils.random(5, 0, -1, false, false, chars, null);
    }
}