package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        final int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals(count, randomString.length());
        for (int index = 0; index < count; index++) {
            char character = randomString.charAt(index);
            boolean isAlphaNumeric = Character.isAlphabetic(character) || Character.isDigit(character);
            assertTrue("Randomly generated string contains non-alphanumeric character at index " + index, isAlphaNumeric);
        }
    }

    @Test
    public void testRandomAscii() {
        final int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals(count, randomAsciiString.length());
        for (int index = 0; index < count; index++) {
            char character = randomAsciiString.charAt(index);
            assertTrue("Randomly generated ASCII string contains non-ASCII character at index " + index, (character >= 32 && character <= 126));
        }
    }

    @Test
    public void testRandomAlphabetic() {
        final int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertEquals(count, randomAlphabeticString.length());
        for (int index = 0; index < count; index++) {
            char character = randomAlphabeticString.charAt(index);
            assertTrue("Randomly generated alphabetic string contains non-alphabetic character at index " + index, Character.isAlphabetic(character));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        final int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals(count, randomAlphanumericString.length());
        for (int index = 0; index < count; index++) {
            char character = randomAlphanumericString.charAt(index);
            assertTrue("Randomly generated alphanumeric string contains non-alphanumeric character at index " + index, (Character.isAlphabetic(character) || Character.isDigit(character)));
        }
    }

    @Test
    public void testRandomNumeric() {
        final int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertEquals(count, randomNumericString.length());
        for (int index = 0; index < count; index++) {
            char character = randomNumericString.charAt(index);
            assertTrue("Randomly generated numeric string contains non-numeric character at index " + index, Character.isDigit(character));
        }
    }

    @Test
    public void testRandomLettersAndNumbers() {
        final int count = 10;
        String randomString = RandomStringUtils.random(count, true, true);
        assertEquals(count, randomString.length());
        for (int index = 0; index < count; index++) {
            char character = randomString.charAt(index);
            assertTrue("Randomly generated alphanumeric string contains non-alphanumeric character at index " + index, (Character.isAlphabetic(character) || Character.isDigit(character)));
        }
    }

    @Test
    public void testRandomWithRange() {
        final int count = 10;
        final int start = 65;
        final int end = 91;
        String randomString = RandomStringUtils.random(count, start, end, true, false);
        assertEquals(count, randomString.length());
        for (int index = 0; index < count; index++) {
            char character = randomString.charAt(index);
            assertTrue("Randomly generated string within range contains character out of range at index " + index, (start <= character && character <= end));
        }
    }

    @Test
    public void testRandomWithEmptyCharacterSet() {
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.random(1, (char[]) null));
    }

    @Test
    public void testRandomWithNegativeCount() {
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.random(-1));
    }

    @Test
    public void testRandomWithNullCharsArray() {
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.random(1, (char[]) null));
    }

    @Test
    public void testRandomWithEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.random(1, ""));
    }

    @Test
    public void testRandomWithRangeOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.random(1, -1, 0));
    }

    @Test
    public void testRandomWithInvalidLettersOrNumbers() {
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.random(1, false, true, null, null));
    }

    @Test
    public void testRandomWithCustomRandom() {
        final int count = 10;
        final char[] characters = new char[]{'A', 'B', 'C', 'D'};
        Random customRandom = new Random();
        String randomString = RandomStringUtils.random(count, 0, characters.length, true, true, characters, customRandom);
        assertEquals(count, randomString.length());
        for (int index = 0; index < count; index++) {
            char character = randomString.charAt(index);
            assertTrue("Randomly generated string with custom random contains non-alphanumeric character at index " + index, (Character.isAlphabetic(character) || Character.isDigit(character) || characters[index % characters.length] == character));
        }
    }
}