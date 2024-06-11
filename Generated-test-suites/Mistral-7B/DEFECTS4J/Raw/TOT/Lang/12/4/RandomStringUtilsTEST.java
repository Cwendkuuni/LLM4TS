package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test creating a random string of length 0.
     */
    @Test
    public void testEmptyString() {
        String randomString = RandomStringUtils.random(0);
        assertEquals("", randomString);
    }

    /**
     * Test creating a random string of length less than 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLength() {
        RandomStringUtils.random(-1);
    }

    /**
     * Test creating a random string of length 1.
     */
    @Test
    public void testSingleCharacterString() {
        char[] validChars = new char[]{'a', 'z', 'A', 'Z', '0', '9'};
        String randomString = RandomStringUtils.random(1, validChars, new Random());
        assertTrue(validChars[new Random().nextInt(validChars.length)] == randomString.charAt(0));
    }

    /**
     * Test creating a random string of length more than 1.
     */
    @Test
    public void testLongerString() {
        char[] validChars = new char[]{'a', 'z', 'A', 'Z', '0', '9'};
        String randomString = RandomStringUtils.random(5, validChars, new Random());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(validChars[new Random().nextInt(validChars.length)] == randomString.charAt(i)
                    || Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test creating a random string with no alphabetic characters.
     */
    @Test
    public void testNumericString() {
        String randomString = RandomStringUtils.randomNumeric(5);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test creating a random string with no numeric characters.
     */
    @Test
    public void testAlphabeticString() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)));
        }
    }

    /**
     * Test creating a random string with alphabetic and numeric characters.
     */
    @Test
    public void testAlphanumericString() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test creating a random string with surrogate characters.
     */
    @Test
    public void testSurrogateString() {
        String randomString = RandomStringUtils.random(5, 0, 0x10FFFF, true, true, null, new Random());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            if (Character.isHighSurrogate(c)) {
                assertTrue(Character.isLowSurrogate(randomString.charAt(++i)));
                i++;
            }
            if (Character.isSurrogate(c)) {
                fail("Unexpected surrogate character: " + c);
            }
        }
    }
}