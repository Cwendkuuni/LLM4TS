package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Test random method with default settings
        String str = RandomStringUtils.random(10);
        assertTrue(str.length() == 10);
        // Test randomAscii method
        str = RandomStringUtils.randomAscii(10);
        assertTrue(str.length() == 10);
        for (char c : str.toCharArray()) {
            assertTrue(' ' <= c && c <= '~');
        }
        // Test randomAlphabetic method
        str = RandomStringUtils.randomAlphabetic(10);
        assertTrue(str.length() == 10);
        for (char c : str.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
        // Test randomAlphanumeric method
        str = RandomStringUtils.randomAlphanumeric(10);
        assertTrue(str.length() == 10);
        for (char c : str.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
        // Test randomNumeric method
        str = RandomStringUtils.randomNumeric(10);
        assertTrue(str.length() == 10);
        for (char c : str.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
        // Test random method with custom settings
        str = RandomStringUtils.random(10, true, true, 'A', 'Z');
        assertTrue(str.length() == 10);
        for (char c : str.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) && 'A' <= c && c <= 'Z');
        }
        str = RandomStringUtils.random(10, true, true, 'a', 'z');
        assertTrue(str.length() == 10);
        for (char c : str.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) && 'a' <= c && c <= 'z');
        }
        str = RandomStringUtils.random(10, true, true, '0', '9');
        assertTrue(str.length() == 10);
        for (char c : str.toCharArray()) {
            assertTrue(Character.isDigit(c) && '0' <= c && c <= '9');
        }
        str = RandomStringUtils.random(10, false, true, '0', '9');
        assertTrue(str.length() == 10);
        for (char c : str.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
        str = RandomStringUtils.random(10, true, false, 'A', 'Z', 'a', 'z');
        assertTrue(str.length() == 10);
        for (char c : str.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
            if (Character.isUpperCase(c)) {
                assertTrue('A' <= c && c <= 'Z');
            } else {
                assertTrue('a' <= c && c <= 'z');
            }
        }
        str = RandomStringUtils.random(10, true, false, '0', '9', 'a', 'z');
        assertTrue(str.length() == 10);
        for (char c : str.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
            if (Character.isDigit(c)) {
                assertTrue('0' <= c && c <= '9');
            }
        }
        str = RandomStringUtils.random(10, false, false, null, null, null, null);
        assertTrue(str.length() > 0);
        // Test random method with custom character array
        char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        str = RandomStringUtils.random(10, chars);
        assertTrue(str.length() == 10);
        for (char c : str.toCharArray()) {
            assertTrue(Arrays.asList(chars).contains(c));
        }
        // Test random method with custom character array and custom random instance
        Random random = new Random(1);
        str = RandomStringUtils.random(10, chars, random);
        assertTrue(str.length() == 10);
        for (char c : str.toCharArray()) {
            assertTrue(Arrays.asList(chars).contains(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharsArray() {
        RandomStringUtils.random(1, new char[0]);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutOfRangeStartAndEnd() {
        char[] chars = {'A', 'B', 'C', 'D', 'E'};
        RandomStringUtils.random(-1, 5, 3, true, false, chars, new Random());
    }

    @Test
    public void testHighAndLowSurrogates() {
        String str = RandomStringUtils.random(1, true, true, null, null, null, new Random());
        assertNotNull(str);
        int indexOfSurrogatePair = -1;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isHighSurrogate(c)) {
                int indexOfNextChar = i + 1;
                if (indexOfNextChar < str.length() && Character.isLowSurrogate(str.charAt(indexOfNextChar))) {
                    indexOfSurrogatePair = i;
                    break;
                }
            }
        }
        assertTrue(indexOfSurrogatePair >= 0);
        assertEquals(str.substring(0, indexOfSurrogatePair) + new String(Character.toChars(RandomStringUtils.random(2))),
                str.substring(0, indexOfSurrogatePair) + new String(Character.toChars(RandomStringUtils.random(2))));
    }
}