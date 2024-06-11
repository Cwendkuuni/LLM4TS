package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandomStringWithDefaultParams() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(randomString.length(), 10);
    }

    @Test
    public void testRandomAsciiString() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(randomAsciiString.length(), 10);
        for (int i = 0; i < randomAsciiString.length(); i++) {
            char c = randomAsciiString.charAt(i);
            assertTrue(Character.isASCII(c));
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(randomAlphabeticString.length(), 10);
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            char c = randomAlphabeticString.charAt(i);
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(randomAlphanumericString.length(), 10);
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            char c = randomAlphanumericString.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(randomNumericString.length(), 10);
        for (int i = 0; i < randomNumericString.length(); i++) {
            char c = randomNumericString.charAt(i);
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomStringWithCustomStartAndEnd() {
        String randomString = RandomStringUtils.random("a", "z", 10);
        assertNotNull(randomString);
        assertEquals(randomString.length(), 10);
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(c) && Character.isLowerCase(c) && Character.isDefined(c));
        }
    }

    @Test
    public void testRandomStringWithCustomStartAndEndAndNumbers() {
        String randomString = RandomStringUtils.random("0123456789", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", 10);
        assertNotNull(randomString);
        assertEquals(randomString.length(), 10);
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomStringWithCustomStartAndEndAndLettersOnly() {
        String randomString = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 10);
        assertNotNull(randomString);
        assertEquals(randomString.length(), 10);
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(c) && Character.isUpperCase(c) && Character.isDefined(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomStringWithEmptyCharacterArray() {
        String randomString = RandomStringUtils.random(new char[0]);
        assertEquals("", randomString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNullCharacterArray() {
        RandomStringUtils.random(null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringWithInvalidCustomStartAndEnd() {
        String randomString = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 23, 2);
    }

    @Test
    public void testRandomStringWithCustomCharacterArray() {
        char[] customChars = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
        String randomString = RandomStringUtils.random(10, customChars);
        assertNotNull(randomString);
        assertEquals(randomString.length(), 10);
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(containsCharacter(customChars, c));
        }
    }

    private boolean containsCharacter(char[] arr, char target) {
        for (char c : arr) {
            if (c == target) {
                return true;
            }
        }
        return false;
    }

}