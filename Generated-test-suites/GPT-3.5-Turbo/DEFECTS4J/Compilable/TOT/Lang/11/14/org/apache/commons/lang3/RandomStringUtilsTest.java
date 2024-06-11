package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);

        assertNotNull(randomString);
        assertEquals(count, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);

        assertNotNull(randomAsciiString);
        assertEquals(count, randomAsciiString.length());

        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);

        assertNotNull(randomAlphabeticString);
        assertEquals(count, randomAlphabeticString.length());

        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);

        assertNotNull(randomAlphanumericString);
        assertEquals(count, randomAlphanumericString.length());

        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);

        assertNotNull(randomNumericString);
        assertEquals(count, randomNumericString.length());

        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomCustom() {
        int count = 10;
        int start = 20;
        int end = 30;
        boolean letters = true;
        boolean numbers = true;
        char[] customChars = {'a', 'b', 'c', '1', '2'};

        String randomString = RandomStringUtils.random(count, start, end, letters, numbers, customChars);

        assertNotNull(randomString);
        assertEquals(count, randomString.length());

        for (char c : randomString.toCharArray()) {
            assertTrue((c >= start && c <= end) || (Character.isLetter(c) || Character.isDigit(c)));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        int count = -10;

        RandomStringUtils.random(count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyChars() {
        int count = 10;
        char[] emptyChars = {};

        RandomStringUtils.random(count, emptyChars);
    }
}