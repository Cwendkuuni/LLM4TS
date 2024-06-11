package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Test the random method with count = 5
        String randomString = RandomStringUtils.random(5);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        // Test the randomAscii method with count = 5
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertEquals(5, randomAsciiString.length());
        for (char ch : randomAsciiString.toCharArray()) {
            assertTrue(ch >= 32 && ch <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        // Test the randomAlphabetic method with count = 5
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, randomAlphabeticString.length());
        for (char ch : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isLetter(ch));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        // Test the randomAlphanumeric method with count = 5
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, randomAlphanumericString.length());
        for (char ch : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(ch));
        }
    }

    @Test
    public void testRandomNumeric() {
        // Test the randomNumeric method with count = 5
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertEquals(5, randomNumericString.length());
        for (char ch : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(ch));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        // Test the random method with letters = true and numbers = true, count = 5
        String randomString = RandomStringUtils.random(5, true, true);
        assertEquals(5, randomString.length());
        for (char ch : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(ch));
        }
    }

    @Test
    public void testRandomWithStartAndEnd() {
        // Test the random method with start = 65, end = 90 (uppercase letters), count = 5
        String randomString = RandomStringUtils.random(5, 65, 90, false, false);
        assertEquals(5, randomString.length());
        for (char ch : randomString.toCharArray()) {
            assertTrue(ch >= 65 && ch <= 90);
        }
    }

    @Test
    public void testRandomWithChars() {
        // Test the random method with chars = {'A', 'B', 'C'}, count = 5
        String randomString = RandomStringUtils.random(5, new char[] {'A', 'B', 'C'});
        assertEquals(5, randomString.length());
        for (char ch : randomString.toCharArray()) {
            assertTrue(ch == 'A' || ch == 'B' || ch == 'C');
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        // Test the random method with negative count
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        // Test the random method with empty chars array
        RandomStringUtils.random(5, new char[] {});
    }

    @Test
    public void testRandomWithNullChars() {
        // Test the random method with null chars
        String randomString = RandomStringUtils.random(5, null);
        assertNotEquals("", randomString);
    }

    @Test
    public void testRandomWithStringChars() {
        // Test the random method with chars = "ABC", count = 5
        String randomString = RandomStringUtils.random(5, "ABC");
        assertEquals(5, randomString.length());
        for (char ch : randomString.toCharArray()) {
            assertTrue(ch == 'A' || ch == 'B' || ch == 'C');
        }
    }

}