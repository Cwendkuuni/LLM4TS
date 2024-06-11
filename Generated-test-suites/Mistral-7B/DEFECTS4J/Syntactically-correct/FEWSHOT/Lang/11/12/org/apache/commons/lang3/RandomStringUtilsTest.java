package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Test random method with default settings
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        // Test random method with specific character set and length
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomAlphanumericString);
        assertEquals(5, randomAlphanumericString.length());
        assertAlphanumeric(randomAlphanumericString.toCharArray());

        // Test random method with specific character set, length and seed
        String randomStringWithSeed = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10, new Random(12345));
        String expectedRandomStringWithSeed = "VVJXKXJLRQKDH";
        assertEquals(expectedRandomStringWithSeed, randomStringWithSeed);

        // Test random method with specific character set, length, seed and excluding certain characters
        String randomStringExcludingCharacters = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10, new Random(12345), '0');
        String expectedRandomStringExcludingCharacters = "VVJXKXJLRQKDHJ";
        assertEquals(expectedRandomStringExcludingCharacters, randomStringExcludingCharacters);

        // Test random method with specific character set, length, seed, and including only certain characters
        String randomStringIncludingCharacters = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10, new Random(12345), '5', '9');
        String expectedRandomStringIncludingCharacters = "VVJXKXJLRQKDHJJJJ";
        assertEquals(expectedRandomStringIncludingCharacters, randomStringIncludingCharacters);

        // Test random method with specific character set, length, seed, and including only certain characters in reverse order
        String randomStringIncludingCharactersInReverseOrder = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10, new Random(12345), '9', '5', false);
        String expectedRandomStringIncludingCharactersInReverseOrder = "JJJJJJDKLRQJXKXV";
        assertEquals(expectedRandomStringIncludingCharactersInReverseOrder, randomStringIncludingCharactersInReverseOrder);
    }

    @Test
    public void testRandomASCII() {
        // Test randomAscii method with default settings
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());

        // Test randomAscii method with specific length
        String randomAsciiStringWithSpecifiedLength = RandomStringUtils.randomAscii(5);
        assertNotNull(randomAsciiStringWithSpecifiedLength);
        assertEquals(5, randomAsciiStringWithSpecifiedLength.length());

        // Test randomAscii method with specific length and seed
        String randomAsciiStringWithSeed = RandomStringUtils.randomAscii(10, new Random(12345));
        String expectedRandomAsciiStringWithSeed = "HJlRtQwXz!@#$%";
        assertEquals(expectedRandomAsciiStringWithSeed, randomAsciiStringWithSeed);
    }

    @Test
    public void testRandomAlphanumeric() {
        // Test randomAlphanumeric method with default settings
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());

        // Test randomAlphanumeric method with specific length
        String randomAlphanumericStringWithSpecifiedLength = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomAlphanumericStringWithSpecifiedLength);
        assertEquals(5, randomAlphanumericStringWithSpecifiedLength.length());

        // Test randomAlphanumeric method with specific length and seed
        String randomAlphanumericStringWithSeed = RandomStringUtils.randomAlphanumeric(10, new Random(12345));
        String expectedRandomAlphanumericStringWithSeed = "TfDpSjLkMvNbQz";
        assertEquals(expectedRandomAlphanumericStringWithSeed, randomAlphanumericStringWithSeed);
    }

    @Test
    public void testRandomAlphabetic() {
        // Test randomAlphabetic method with default settings
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());

        // Test randomAlphabetic method with specific length
        String randomAlphabeticStringWithSpecifiedLength = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomAlphabeticStringWithSpecifiedLength);
        assertEquals(5, randomAlphabeticStringWithSpecifiedLength.length());

        // Test randomAlphabetic method with specific length and seed
        String randomAlphabeticStringWithSeed = RandomStringUtils.randomAlphabetic(10, new Random(12345));
        String expectedRandomAlphabeticStringWithSeed = "xgywvbnmkpqj";
        assertEquals(expectedRandomAlphabeticStringWithSeed, randomAlphabeticStringWithSeed);
    }

    @Test
    public void testRandomNumeric() {
        // Test randomNumeric method with default settings
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());

        // Test randomNumeric method with specific length
        String randomNumericStringWithSpecifiedLength = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomNumericStringWithSpecifiedLength);
        assertEquals(5, randomNumericStringWithSpecifiedLength.length());

        // Test randomNumeric method with specific length and seed
        String randomNumericStringWithSeed = RandomStringUtils.randomNumeric(10, new Random(12345));
        String expectedRandomNumericStringWithSeed = "6tBmYqLpDwNjKg";
        assertEquals(expectedRandomNumericStringWithSeed, randomNumericStringWithSeed);
    }

    private void assertAlphanumeric(char[] chars) {
        for (char c : chars) {
            if (!Character.isAlphabetic(c) && !Character.isDigit(c)) {
                throw new AssertionError("Characters in the array must be alphanumeric.");
            }
        }
    }

}