package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * JUnit 4 test cases for RandomStringUtils class
 */
public class RandomStringUtilsTest {

    /**
     * Test for random method with default arguments
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for randomAscii method with default arguments
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assert(c >= 32 && c <= 126);
        }
    }

    /**
     * Test for randomAlphabetic method with default arguments
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    /**
     * Test for randomAlphanumeric method with default arguments
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for randomNumeric method with default arguments
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    /**
     * Test for random method with specified length and characters set
     */
    @Test
    public void testRandomWithCustomSet() {
        String customSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(10, customSet.toCharArray());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(customSet.indexOf(c) != -1);
        }
    }

    /**
     * Test for random method with negative length
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-1);
    }

    /**
     * Test for random method with empty character array
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharacterArray() {
        RandomStringUtils.random(1, new char[0]);
    }

    /**
     * Test for random method with high and low surrogates
     */
    @Test
    public void testRandomWithSurrogates() {
        String randomString = RandomStringUtils.random(2);
        assertNotNull(randomString);
        assertEquals(2, randomString.length());
        int indexHighSurrogate = -1;
        int indexLowSurrogate = -1;
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            if (Character.isHighSurrogate(c)) {
                indexHighSurrogate = i;
            } else if (Character.isLowSurrogate(c)) {
                indexLowSurrogate = i;
                break;
            }
        }
        assert(indexHighSurrogate != -1 && indexLowSurrogate != -1);
        assertEquals(indexHighSurrogate + 1, indexLowSurrogate);
    }
}