package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test class for RandomStringUtils
 */
public class RandomStringUtilsTest {

    /**
     * Test for random method with no arguments
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    /**
     * Test for randomAscii method with no arguments
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomAsciiString);
        assertEquals(5, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assert(c >= 32 && c <= 126);
        }
    }

    /**
     * Test for randomAlphabetic method with no arguments
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomAlphabeticString);
        assertEquals(5, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    /**
     * Test for randomAlphanumeric method with no arguments
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomAlphanumericString);
        assertEquals(5, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for randomNumeric method with no arguments
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomNumericString);
        assertEquals(5, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    /**
     * Test for random method with specified length and default character set
     */
    @Test
    public void testRandomWithDefaultCharacterSet() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for randomAscii method with specified length and default character set
     */
    @Test
    public void testRandomAsciiWithDefaultCharacterSet() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assert(c >= 32 && c <= 126);
        }
    }

    /**
     * Test for randomAlphabetic method with specified length and default character set
     */
    @Test
    public void testRandomAlphabeticWithDefaultCharacterSet() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    /**
     * Test for randomAlphanumeric method with specified length and default character set
     */
    @Test
    public void testRandomAlphanumericWithDefaultCharacterSet() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for randomNumeric method with specified length and default character set
     */
    @Test
    public void testRandomNumericWithDefaultCharacterSet() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    /**
     * Test for random method with specified length, start, end, letters, and numbers, and null character set
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNullCharacterSet() {
        RandomStringUtils.random(10, null, null, true, true);
    }

    /**
     * Test for random method with specified length, start, end, letters, and numbers, empty character set, and default random
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterSet() {
        RandomStringUtils.random(10, new char[0], 0, true, true, null, null);
    }

    /**
     * Test for random method with specified length, start, end, letters, and numbers, empty character set, and supplied random
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterSetAndSuppliedRandom() {
        RandomStringUtils.random(10, new char[0], 0, true, true, null, new Random());
    }

    /**
     * Test for random method with specified length, start, end, letters, and numbers, and character set containing only one character
     */
    @Test
    public void testRandomWithOneCharacterSet() {
        String randomString = RandomStringUtils.random(10, new char[]{'a'}, 0, true, true, null, null);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals('a', c);
        }
    }

    /**
     * Test for random method with specified length, start, end, letters, and numbers, character set containing no letters
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNoLetterCharacterSet() {
        String randomString = RandomStringUtils.random(10, new char[]{'0'}, 0, false, true, null, null);
    }

    /**
     * Test for random method with specified length, start, end, letters, and numbers, character set containing no numbers
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNoNumberCharacterSet() {
        String randomString = RandomStringUtils.random(10, new char[]{'a', 'b', 'c'}, 0, true, false, null, null);
    }

    /**
     * Test for random method with specified length, start, end, letters, and numbers, and character set containing only high surrogate characters
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithOnlyHighSurrogateCharacterSet() {
        String randomString = RandomStringUtils.random(10, new char[]{'𠓂', '𠓃', '𠓄'}, 0, true, true, null, null);
    }

    /**
     * Test for random method with specified length, start, end, letters, and numbers, and character set containing only low surrogate characters
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithOnlyLowSurrogateCharacterSet() {
        String randomString = RandomStringUtils.random(10, new char[]{'𠃆', '𠃇', '𠃈'}, 0, true, true, null, null);
    }

    /**
     * Test for random method with specified length, start, end, letters, and numbers, and character set containing high and low surrogate characters
     */
    @Test
    public void testRandomWithHighAndLowSurrogateCharacterSet() {
        String randomString = RandomStringUtils.random(10, new char[]{'𠓂', '𠓃', '𠓄', '𠃆', '𠃇', '𠃈'}, 0, true, true, null, null);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (c >= 0x55296 && c <= 0x56191) {
                // High surrogate character
                assertTrue(Character.isHighSurrogate(c));
            } else if (c >= 0x56320 && c <= 0x57343) {
                // Low surrogate character
                assertTrue(Character.isLowSurrogate(c));
            } else {
                // Basic character
                assert(Character.isLetterOrDigit(c) || Character.isWhitespace(c));
            }
        }
    }

    /**
     * Test for random method with specified length, start, end, letters, and numbers, and character set containing all characters
     */
    @Test
    public void testRandomWithAllCharacterSet() {
        String randomString = RandomStringUtils.random(10, null, 0, true, true, null, null);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isLetterOrDigit(c) || Character.isWhitespace(c) || Character.isSurrogate(c));
        }
    }

    /**
     * Test for random method with specified length, start, end, letters, and numbers, and supplied random
     */
    @Test
    public void testRandomWithSuppliedRandom() {
        Random random = new Random();
        String randomString = RandomStringUtils.random(10, null, 0, true, true, null, random);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for random method with specified length, start, end, letters, and numbers, and supplied random with fixed seed
     */
    @Test
    public void testRandomWithSuppliedRandomFixedSeed() {
        Random random = new Random(1);
        String randomString1 = RandomStringUtils.random(10, null, 0, true, true, null, random);
        String randomString2 = RandomStringUtils.random(10, null, 0, true, true, null, random);
        assertEquals(randomString1, randomString2);
    }
}