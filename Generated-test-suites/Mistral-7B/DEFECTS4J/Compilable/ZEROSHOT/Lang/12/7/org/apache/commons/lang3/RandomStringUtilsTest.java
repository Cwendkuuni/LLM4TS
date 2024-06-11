package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

/**
 * Test class for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test random method with default parameters.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test randomAscii method with default parameters.
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            char c = randomAsciiString.charAt(i);
            assertTrue((c >= 32 && c <= 126));
        }
    }

    /**
     * Test randomAlphabetic method with default parameters.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            char c = randomAlphabeticString.charAt(i);
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test randomAlphanumeric method with default parameters.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char c = randomAlphanumericString.charAt(i);
            assertTrue((Character.isAlphabetic(c) || Character.isDigit(c)));
        }
    }

    /**
     * Test randomNumeric method with default parameters.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            char c = randomNumericString.charAt(i);
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test random method with custom length and character set.
     */
    @Test
    public void testRandomWithCustomLengthAndCharacterSet() {
        String customString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomString = RandomStringUtils.random(10, customString.toCharArray());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(customString.indexOf(c) != -1);
        }
    }

    /**
     * Test random method with custom length, character set start and end, and custom Random.
     */
    @Test
    public void testRandomWithCustomLengthStartEndAndCustomRandom() {
        String customString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int startIndex = 0;
        int endIndex = 5;
        String subsetString = "EFGHIJ";
        Random random = new Random(1); // fixed seed for testing
        String randomString = RandomStringUtils.random(10, startIndex, endIndex, false, false, customString.toCharArray(), random);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            if (i < 5) {
                assertTrue(subsetString.indexOf(c) != -1);
            } else {
                assertTrue(customString.indexOf(c) != -1);
            }
        }
    }
}