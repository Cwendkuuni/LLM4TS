package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for RandomStringUtils class
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            characterIsValid(randomString.charAt(i));
        }
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            isAscii(randomAsciiString.charAt(i));
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            isAlpha(randomAlphabeticString.charAt(i));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            isAlphanumeric(randomAlphanumericString.charAt(i));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            isDigit(randomNumericString.charAt(i));
        }
    }

    @Test
    public void testRandomWithCustomCharacterSet() {
        String customCharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomStringWithCustomCharacterSet = RandomStringUtils.random(10, customCharacterSet.toCharArray());
        assertEquals(10, randomStringWithCustomCharacterSet.length());
        for (int i = 0; i < randomStringWithCustomCharacterSet.length(); i++) {
            assertTrue(customCharacterSet.indexOf(randomStringWithCustomCharacterSet.charAt(i)) != -1);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStringLength() {
        RandomStringUtils.random(5); // this line should not be reached
        fail("Expected IllegalArgumentException when length is negative.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharacterSet() {
        String emptyCharacterSet = "";
        RandomStringUtils.random(5, emptyCharacterSet.toCharArray());
        fail("Expected IllegalArgumentException when character set is empty.");
    }

    @Test
    public void testRandomWithSurrogateCharacters() {
        String randomString = RandomStringUtils.random(1);
        int length = randomString.length();
        int randomIndex = RANDOM.nextInt(length);
        char randomChar = randomString.charAt(randomIndex);
        if (Character.isHighSurrogate(randomChar)) {
            int nextIndex = RANDOM.nextInt(128);
            char lowSurrogate = (char) (Character.highSurrogate(randomChar) + nextIndex);
            String randomStringWithSurrogates = RandomStringUtils.random(2, randomChar, lowSurrogate);
            assertEquals(2, randomStringWithSurrogates.length());
            assertEquals(randomChar, randomStringWithSurrogates.charAt(0));
            assertEquals(lowSurrogate, randomStringWithSurrogates.charAt(1));
        }
    }

    private void characterIsValid(char c) {
        if ((c < 0x0020 || c > 0x007E) && c != '\u007F') {
            fail("Invalid character: " + c);
        }
    }

    private void isAscii(char c) {
        if ((c < 0x0020 || c > 0x007E) && c != '\u007F') {
            fail("Character is not ASCII: " + c);
        }
    }

    private void isAlpha(char c) {
        if (!Character.isLetter(c)) {
            fail("Character is not alpha: " + c);
        }
    }

    private void isAlphanumeric(char c) {
        if (!Character.isAlphabetic(c) && !Character.isDigit(c)) {
            fail("Character is not alphanumeric: " + c);
        }
    }

    private void isDigit(char c) {
        if (!Character.isDigit(c)) {
            fail("Character is not digit: " + c);
        }
    }
}