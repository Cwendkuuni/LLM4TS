package org.apache.commons.lang3.test;

import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.lang3.RandomStringUtils.*;

/**
 * Test class for {@link RandomStringUtils}
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        // Test creating random strings of various lengths
        for (int i = 0; i < 10; i++) {
            String randomString = random(i);
            assertEquals("Random string of length " + i, i, randomString.length());
        }

        // Test creating random strings with different character sets
        String asciiString = randomAscii(10);
        String alphanumericString = randomAlphanumeric(10);
        String alphabeticString = randomAlphabetic(10);
        String numericString = randomNumeric(10);

        // Test that only alphabetic characters are present in the alphabetic string
        for (int i = 0; i < alphabeticString.length(); i++) {
            assertTrue("Character at index " + i + " is alphabetic: " + alphabeticString.charAt(i), Character.isAlphabetic(alphabeticString.charAt(i)));
        }

        // Test that only numeric characters are present in the numeric string
        for (int i = 0; i < numericString.length(); i++) {
            assertTrue("Character at index " + i + " is numeric: " + numericString.charAt(i), Character.isDigit(numericString.charAt(i)));
        }

        // Test that only ASCII characters are present in the ascii string
        for (int i = 0; i < asciiString.length(); i++) {
            assertTrue("Character at index " + i + " is ASCII: " + (int) asciiString.charAt(i), (int) asciiString.charAt(i) < 128);
        }

        // Test creating random strings with specified length and character sets
        String randomStringWithStartAndEnd = random(10, 33, 126, false, false);
        String randomStringWithLetters = random(10, true, false);
        String randomStringWithNumbers = random(10, false, true);
        String randomStringWithLettersAndNumbers = random(10, true, true);

        // Test that generated random strings only contain the specified characters
        for (int i = 0; i < randomStringWithStartAndEnd.length(); i++) {
            assertTrue("Character at index " + i + " is within the specified range: " + randomStringWithStartAndEnd.charAt(i), (int) randomStringWithStartAndEnd.charAt(i) >= 33 && (int) randomStringWithStartAndEnd.charAt(i) <= 126);
        }
        for (int i = 0; i < randomStringWithLetters.length(); i++) {
            assertTrue("Character at index " + i + " is alphabetic: " + randomStringWithLetters.charAt(i), Character.isAlphabetic(randomStringWithLetters.charAt(i)));
        }
        for (int i = 0; i < randomStringWithNumbers.length(); i++) {
            assertTrue("Character at index " + i + " is numeric: " + randomStringWithNumbers.charAt(i), Character.isDigit(randomStringWithNumbers.charAt(i)));
        }
        for (int i = 0; i < randomStringWithLettersAndNumbers.length(); i++) {
            assertTrue("Character at index " + i + " is alphabetic or numeric: " + randomStringWithLettersAndNumbers.charAt(i), Character.isAlphabetic(randomStringWithLettersAndNumbers.charAt(i)) || Character.isDigit(randomStringWithLettersAndNumbers.charAt(i)));
        }

        // Test creating random strings with custom character sets
        String customCharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?/";
        String randomStringWithCustomCharacterSet = random(10, 0, customCharacterSet.length(), false, false, customCharacterSet.toCharArray());
        for (int i = 0; i < randomStringWithCustomCharacterSet.length(); i++) {
            assertTrue("Character at index " + i + " is present in the custom character set: " + randomStringWithCustomCharacterSet.charAt(i), customCharacterSet.indexOf(randomStringWithCustomCharacterSet.charAt(i)) != -1);
        }

        // Test creating random strings with high and low surrogate characters
        String randomStringWithSurrogates = random(10, 0, 0, false, false);
        assertTrue("Random string with surrogates contains at least one high and one low surrogate", randomStringWithSurrogates.matches(".*[\\uD800-\\uDBFF][\\uDC00-\\uDFFF].*"));
    }

    @Test
    public void testRandomStringWithNullOrEmptyCharacterSet() {
        try {
            random(10, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            random(10, "");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRandomStringWithNegativeLength() {
        try {
            random(-1, 0, 0, false, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRandomStringWithInvalidCharacterRange() {
        try {
            random(10, 128, 32, false, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}