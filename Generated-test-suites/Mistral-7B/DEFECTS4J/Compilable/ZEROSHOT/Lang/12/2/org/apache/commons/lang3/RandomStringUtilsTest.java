package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    /**
     * Test for random method with default options.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Character c = randomString.charAt(i);
            assertTrue("Randomly generated character out of range", c >= 0 && c <= 127);
        }
    }

    /**
     * Test for randomAscii method.
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            Character c = randomAsciiString.charAt(i);
            assertTrue("Randomly generated character out of range", c >= 32 && c <= 126);
        }
    }

    /**
     * Test for randomAlphabetic method.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            Character c = randomAlphabeticString.charAt(i);
            assertTrue("Randomly generated character out of range or not alphabetic",
                       (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
        }
    }

    /**
     * Test for randomAlphanumeric method.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            Character c = randomAlphanumericString.charAt(i);
            assertTrue("Randomly generated character out of range or not alphanumeric",
                       (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'));
        }
    }

    /**
     * Test for randomNumeric method.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            Character c = randomNumericString.charAt(i);
            assertTrue("Randomly generated character out of range or not numeric", c >= '0' && c <= '9');
        }
    }

    /**
     * Test for random method with custom character set.
     */
    @Test
    public void testRandomCustomCharacterSet() {
        String customCharacterSet = "abcd1234";
        String randomString = RandomStringUtils.random(10, customCharacterSet.toCharArray());
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Character c = randomString.charAt(i);
            assertTrue("Randomly generated character out of range or not in custom character set",
                       customCharacterSet.indexOf(c) != -1);
        }
    }

    /**
     * Test for random method with surrogate pairs.
     */
    @Test
    public void testRandomSurrogatePairs() {
        String randomString = RandomStringUtils.random(2, false, true);
        assertEquals(2, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Character c = randomString.charAt(i);
            if (i == 0) {
                assertTrue("First character is not a high surrogate", (c >= '\uD800' && c <= '\uDBFF'));
            } else {
                assertTrue("Second character is not a low surrogate", (c >= '\uDC00' && c <= '\uDFFF'));
            }
        }
    }
}