package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test cases for the RandomStringUtils class.
 */
public class RandomStringUtilsTest {

    /**
     * Test random method with default settings.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    /**
     * Test randomAscii method with default settings.
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomAsciiString);
        assertEquals(5, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(Character.isASCII(c));
        }
    }

    /**
     * Test randomAlphabetic method with default settings.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomAlphabeticString);
        assertEquals(5, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    /**
     * Test randomAlphanumeric method with default settings.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomAlphanumericString);
        assertEquals(5, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test randomNumeric method with default settings.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomNumericString);
        assertEquals(5, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    /**
     * Test random method with specified length and character set.
     */
    @Test
    public void testRandomWithCustomCharacterSet() {
        String customCharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomString = RandomStringUtils.random(5, customCharacterSet.toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(customCharacterSet.indexOf(c) >= 0);
        }
    }

    /**
     * Test randomAscii method with specified length and character set.
     */
    @Test
    public void testRandomAsciiWithCustomCharacterSet() {
        String customCharacterSet = "!@#$%^&*()_";
        String randomAsciiString = RandomStringUtils.randomAscii(5, customCharacterSet.toCharArray());
        assertNotNull(randomAsciiString);
        assertEquals(5, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(Character.isASCII(c) && Character.isDefined(c));
            assertTrue(customCharacterSet.indexOf(c) >= 0);
        }
    }

    /**
     * Test randomAlphabetic method with specified length and character set.
     */
    @Test
    public void testRandomAlphabeticWithCustomCharacterSet() {
        String customCharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5, customCharacterSet.toCharArray());
        assertNotNull(randomAlphabeticString);
        assertEquals(5, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) && Character.isUpperCase(c));
            assertTrue(customCharacterSet.indexOf(c) >= 0);
        }
    }

    /**
     * Test randomAlphanumeric method with specified length and character set.
     */
    @Test
    public void testRandomAlphanumericWithCustomCharacterSet() {
        String customCharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5, customCharacterSet.toCharArray());
        assertNotNull(randomAlphanumericString);
        assertEquals(5, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || Character.isWhitespace(c));
            assertTrue(customCharacterSet.indexOf(c) >= 0);
        }
    }

    /**
     * Test randomNumeric method with specified length and character set.
     */
    @Test
    public void testRandomNumericWithCustomCharacterSet() {
        String customCharacterSet = "0123456789";
        String randomNumericString = RandomStringUtils.randomNumeric(5, customCharacterSet.toCharArray());
        assertNotNull(randomNumericString);
        assertEquals(5, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
            assertTrue(customCharacterSet.indexOf(c) >= 0);
        }
    }

    /**
     * Test random method with specified length, character set, and seed.
     */
    @Test
    public void testRandomWithSeed() {
        Long seed = 123L;
        RandomStringUtils randomStringUtils = new RandomStringUtils();
        String randomString1 = randomStringUtils.random(5, 0, 0, false, false, null, new Random(seed));
        String randomString2 = randomStringUtils.random(5, 0, 0, false, false, null, new Random(seed));
        assertEquals(randomString1, randomString2);
    }

    /**
     * Test randomAscii method with specified length, character set, and seed.
     */
    @Test
    public void testRandomAsciiWithSeed() {
        Long seed = 123L;
        RandomStringUtils randomStringUtils = new RandomStringUtils();
        String randomAsciiString1 = randomStringUtils.randomAscii(5, 0, 0, false, false, null, new Random(seed));
        String randomAsciiString2 = randomStringUtils.randomAscii(5, 0, 0, false, false, null, new Random(seed));
        assertEquals(randomAsciiString1, randomAsciiString2);
    }

    /**
     * Test randomAlphabetic method with specified length, character set, and seed.
     */
    @Test
    public void testRandomAlphabeticWithSeed() {
        Long seed = 123L;
        RandomStringUtils randomStringUtils = new RandomStringUtils();
        String randomAlphabeticString1 = randomStringUtils.randomAlphabetic(5, 0, 0, false, false, null, new Random(seed));
        String randomAlphabeticString2 = randomStringUtils.randomAlphabetic(5, 0, 0, false, false, null, new Random(seed));
        assertEquals(randomAlphabeticString1, randomAlphabeticString2);
    }

    /**
     * Test randomAlphanumeric method with specified length, character set, and seed.
     */
    @Test
    public void testRandomAlphanumericWithSeed() {
        Long seed = 123L;
        RandomStringUtils randomStringUtils = new RandomStringUtils();
        String randomAlphanumericString1 = randomStringUtils.randomAlphanumeric(5, 0, 0, false, false, null, new Random(seed));
        String randomAlphanumericString2 = randomStringUtils.randomAlphanumeric(5, 0, 0, false, false, null, new Random(seed));
        assertEquals(randomAlphanumericString1, randomAlphanumericString2);
    }

    /**
     * Test random method with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeLength() {
        RandomStringUtils.random(-5);
    }

    /**
     * Test randomAscii method with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAsciiWithNegativeLength() {
        RandomStringUtils.randomAscii(-5);
    }

    /**
     * Test randomAlphabetic method with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphabeticWithNegativeLength() {
        RandomStringUtils.randomAlphabetic(-5);
    }

    /**
     * Test randomAlphanumeric method with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphanumericWithNegativeLength() {
        RandomStringUtils.randomAlphanumeric(-5);
    }

    /**
     * Test random method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterSet() {
        RandomStringUtils.random(5, (String) null);
    }

    /**
     * Test randomAscii method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAsciiWithEmptyCharacterSet() {
        RandomStringUtils.randomAscii(5, (String) null);
    }

    /**
     * Test randomAlphabetic method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphabeticWithEmptyCharacterSet() {
        RandomStringUtils.randomAlphabetic(5, (String) null);
    }

    /**
     * Test randomAlphanumeric method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphanumericWithEmptyCharacterSet() {
        RandomStringUtils.randomAlphanumeric(5, (String) null);
    }
}