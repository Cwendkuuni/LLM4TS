package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Test for RandomStringUtils class
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            char c = randomAsciiString.charAt(i);
            assertTrue(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            char c = randomAlphabeticString.charAt(i);
            assertTrue(isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char c = randomAlphanumericString.charAt(i);
            assertTrue(isAlphabetic(c) || isDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            char c = randomNumericString.charAt(i);
            assertTrue(isDigit(c));
        }
    }

    @Test
    public void testRandomWithSpecifiedLengthAndChars() {
        String randomString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomStringWithSpecifiedChars = RandomStringUtils.random(10, randomString.toCharArray());
        assertNotNull(randomStringWithSpecifiedChars);
        assertEquals(10, randomStringWithSpecifiedChars.length());
        for (int i = 0; i < randomStringWithSpecifiedChars.length(); i++) {
            char c = randomStringWithSpecifiedChars.charAt(i);
            assertTrue(randomString.indexOf(c) != -1);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharsArray() {
        RandomStringUtils.random(10, (char[]) null);
    }

    @Test
    public void testRandomWithCustomRandomSource() {
        Random random = new Random();
        String randomString = RandomStringUtils.random(10, 33, 126, false, false, null, random);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(c >= 33 && c <= 126);
        }

        String randomStringWithCustomSource = RandomStringUtils.random(10, 33, 126, false, false, null, new Random(12345));
        assertNotNull(randomStringWithCustomSource);
        assertEquals(10, randomStringWithCustomSource.length());
        for (int i = 0; i < randomStringWithCustomSource.length(); i++) {
            char c = randomStringWithCustomSource.charAt(i);
            assertTrue(c >= 33 && c <= 126);
        }
        String constantString = "12345";
        String randomStringWithCustomSourceAndConstantChars = RandomStringUtils.random(10, 33, 126, false, false, constantString.toCharArray(), new Random(12345));
        assertEquals(constantString, randomStringWithCustomSourceAndConstantChars.substring(0, constantString.length()));
    }

    @Test
    public void testRandomWithCustomStartEndAndChars() {
        char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'};
        String randomString = RandomStringUtils.random(10, 0, chars.length, false, false, chars, RANDOM);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(Arrays.binarySearch(chars, c) != -1);
        }
    }

    private boolean isAlphabetic(char c) {
        return (c >= 65 && c <= 90) || (c >= 97 && c <= 122);
    }

    private boolean isDigit(char c) {
        return c >= 48 && c <= 57;
    }
}