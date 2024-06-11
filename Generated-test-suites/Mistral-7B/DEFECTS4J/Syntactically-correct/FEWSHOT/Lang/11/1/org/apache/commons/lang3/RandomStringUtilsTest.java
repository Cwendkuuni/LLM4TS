package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        // Test random string with default settings
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
        assertNotNull(randomString);

        // Test random string with specific character set and length
        String alphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, alphanumericString.length());
        assertNotNull(alphanumericString);
        assertAlphanumeric(alphanumericString);

        // Test random string with specific character set and length (ASCII only)
        String asciiString = RandomStringUtils.randomAscii(10);
        assertEquals(10, asciiString.length());
        assertNotNull(asciiString);
        assertAscii(asciiString);

        // Test random string with specific character set and length (alphabetic only)
        String alphabeticString = RandomStringUtils.randomAlphabetic(15);
        assertEquals(15, alphabeticString.length());
        assertNotNull(alphabeticString);
        assertAlphabetic(alphabeticString);

        // Test random string with specific character set and length (numeric only)
        String numericString = RandomStringUtils.randomNumeric(7);
        assertEquals(7, numericString.length());
        assertNotNull(numericString);
        assertNumeric(numericString);

        // Test random string with specific character set, length, start and end indices
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        String substringString = RandomStringUtils.random(5, chars, 0, 6, false, false);
        assertEquals(5, substringString.length());
        assertNotNull(substringString);
        for (int i = 0; i < substringString.length(); i++) {
            assertEquals(chars[i], substringString.charAt(i));
        }
    }

    @Test
    public void testRandomStringWithInvalidArguments() {
        // Test null length
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.random(0));

        // Test invalid character array length
        char[] emptyChars = new char[0];
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.random(1, emptyChars));
    }

    @Test
    public void testRandomStringWithInvalidCharacterRanges() {
        String randomString = RandomStringUtils.random(10, 'z', 'a');
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertAlphabetic(randomString);
    }

    @Test
    public void testRandomStringWithCustomRandom() {
        Random customRandom = new Random(1L);
        String randomString = RandomStringUtils.random(5, 0, 0, false, false, null, customRandom);
        assertEquals(5, randomString.length());
        assertNotNull(randomString);
    }

    @Test
    public void testRandomStringWithCustomCharacterSetAndCustomRandom() {
        Random customRandom = new Random(1L);
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        String randomString = RandomStringUtils.random(5, 0, chars.length, false, false, chars, customRandom);
        assertEquals(5, randomString.length());
        assertNotNull(randomString);
        for (int i = 0; i < randomString.length(); i++) {
            assertEquals(chars[i], randomString.charAt(i));
        }
    }

    private void assertAlphanumeric(String string) {
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            boolean isAlphanumeric = Character.isLetterOrDigit(c);
            if (!isAlphanumeric) {
                fail("Character at index " + i + " is not alphanumeric");
            }
        }
    }

    private void assertAscii(String string) {
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c > 126 || c < 32) {
                fail("Character at index " + i + " is not ASCII");
            }
        }
    }

    private void assertAlphabetic(String string) {
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            boolean isAlphabetic = Character.isAlphabetic(c);
            if (!isAlphabetic) {
                fail("Character at index " + i + " is not alphabetic");
            }
        }
    }

    private void assertNumeric(String string) {
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            boolean isNumeric = Character.isDigit(c);
            if (!isNumeric) {
                fail("Character at index " + i + " is not numeric");
            }
        }
    }
}