package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * RandomStringUtilsTest
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomAsciiString);
        assertEquals(5, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            int randomChar = randomAsciiString.charAt(i);
            assert(32 <= randomChar && randomChar <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomAlphabeticString);
        assertEquals(5, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            int randomChar = randomAlphabeticString.charAt(i);
            assert(97 <= randomChar && randomChar <= 122 || 65 <= randomChar && randomChar <= 90);
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomAlphanumericString);
        assertEquals(5, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            int randomChar = randomAlphanumericString.charAt(i);
            assert(48 <= randomChar && randomChar <= 57 || 97 <= randomChar && randomChar <= 122 || 65 <= randomChar && randomChar <= 90);
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomNumericString);
        assertEquals(5, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            int randomChar = randomNumericString.charAt(i);
            assert(48 <= randomChar && randomChar <= 57);
        }
    }

    @Test
    public void testRandomWithSpecifiedOptions() {
        String randomString = RandomStringUtils.random(5, true, true);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            int randomChar = randomString.charAt(i);
            assert(48 <= randomChar && randomChar <= 57 || 97 <= randomChar && randomChar <= 122 || 65 <= randomChar && randomChar <= 90);
        }

        String randomStringLettersOnly = RandomStringUtils.random(5, true, false);
        assertNotNull(randomStringLettersOnly);
        assertEquals(5, randomStringLettersOnly.length());
        for (int i = 0; i < randomStringLettersOnly.length(); i++) {
            int randomChar = randomStringLettersOnly.charAt(i);
            assert(97 <= randomChar && randomChar <= 122 || 65 <= randomChar && randomChar <= 90);
        }

        String randomStringNumbersOnly = RandomStringUtils.random(5, false, true);
        assertNotNull(randomStringNumbersOnly);
        assertEquals(5, randomStringNumbersOnly.length());
        for (int i = 0; i < randomStringNumbersOnly.length(); i++) {
            int randomChar = randomStringNumbersOnly.charAt(i);
            assert(48 <= randomChar && randomChar <= 57);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharacterSet() {
        RandomStringUtils.random(5, new char[0]);
    }

    @Test
    public void testRandomWithSpecifiedStartAndEnd() {
        char[] charSet = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        String randomString = RandomStringUtils.random(5, 1, 5, false, false, charSet, RANDOM);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char randomChar = randomString.charAt(i);
            assertTrue(1 <= randomChar && randomChar <= 5);
            assert(charSet[randomChar - 1] == randomChar);
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomArrayIndexOutOfBounds() {
        char[] charSet = {'a', 'b', 'c'};
        RandomStringUtils.random(5, -1, 1, false, false, charSet, RANDOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomInvalidCharacterSet() {
        String randomString = RandomStringUtils.random(5, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeStartAndEnd() {
        char[] charSet = {'a', 'b', 'c'};
        RandomStringUtils.random(5, -1, -1, false, false, charSet, RANDOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeStart() {
        char[] charSet = {'a', 'b', 'c'};
        RandomStringUtils.random(5, -1, 0, false, false, charSet, RANDOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeEnd() {
        char[] charSet = {'a', 'b', 'c'};
        RandomStringUtils.random(5, 0, -1, false, false, charSet, RANDOM);
    }

    @Test
    public void testRandomWithHighAndLowSurrogates() {
        String randomString = RandomStringUtils.random(5, 0, 0, false, false, null, RANDOM);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        boolean containsHighSurrogate = false;
        boolean containsLowSurrogate = false;
        for (int i = 0; i < randomString.length(); i++) {
            char randomChar = randomString.charAt(i);
            if (Character.isHighSurrogate(randomChar)) {
                containsHighSurrogate = true;
                assertEquals(55296 + RANDOM.nextInt(128), randomChar);
                if (i < randomString.length() - 1) {
                    char nextChar = randomString.charAt(i + 1);
                    assertEquals(Character.isLowSurrogate(nextChar), true);
                    assertEquals(56320 + RANDOM.nextInt(128), nextChar);
                }
            } else if (Character.isLowSurrogate(randomChar)) {
                containsLowSurrogate = true;
                assertEquals(56320 + RANDOM.nextInt(128), randomChar);
                assertEquals(55296 + RANDOM.nextInt(128), randomString.charAt(i - 1));
            } else {
                // normal character
                assert(Character.isAlphabetic(randomChar) || Character.isDigit(randomChar) || Character.isWhitespace(randomChar) || Character.isConnectorPunctuation(randomChar) || Character.isOtherSymbol(randomChar) || Character.isSpaceChar(randomChar) || Character.isControlCharacter(randomChar));
            }
        }
        assertEquals(containsHighSurrogate, true);
        assertEquals(containsLowSurrogate, true);
    }
}