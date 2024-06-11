package org.apache.commons.lang3;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        for (int i = 1; i <= 10; i++) {
            String randomString = RandomStringUtils.random(i);
            int length = randomString.length();
            assertEquals("Random string of length " + i, length, i);
        }

        String randomString = RandomStringUtils.random(0);
        assertEquals("Random string of length 0", "", randomString);

        String randomStringAscii = RandomStringUtils.randomAscii(10);
        int lengthAscii = randomStringAscii.length();
        for (int i = 0; i < lengthAscii; i++) {
            char c = randomStringAscii.charAt(i);
            assertTrue("Random character is an ASCII character", (c >= 32 && c <= 126));
        }

        String randomStringAlphabetic = RandomStringUtils.randomAlphabetic(10);
        int lengthAlphabetic = randomStringAlphabetic.length();
        for (int i = 0; i < lengthAlphabetic; i++) {
            char c = randomStringAlphabetic.charAt(i);
            assertTrue("Random character is an alphabetic character", Character.isAlphabetic(c));
        }

        String randomStringAlphanumeric = RandomStringUtils.randomAlphanumeric(10);
        int lengthAlphanumeric = randomStringAlphanumeric.length();
        for (int i = 0; i < lengthAlphanumeric; i++) {
            char c = randomStringAlphanumeric.charAt(i);
            assertTrue("Random character is an alphanumeric character", Character.isAlphabetic(c) || Character.isDigit(c));
        }

        String randomStringNumeric = RandomStringUtils.randomNumeric(10);
        int lengthNumeric = randomStringNumeric.length();
        for (int i = 0; i < lengthNumeric; i++) {
            char c = randomStringNumeric.charAt(i);
            assertTrue("Random character is a numeric character", Character.isDigit(c));
        }

        String randomStringCustom = RandomStringUtils.random(10, 'a', 'z');
        int lengthCustom = randomStringCustom.length();
        for (int i = 0; i < lengthCustom; i++) {
            char c = randomStringCustom.charAt(i);
            assertTrue("Random character is an alphabetic character", Character.isAlphabetic(c));
        }

        String randomStringCustomNumeric = RandomStringUtils.random(10, '0', '9');
        int lengthCustomNumeric = randomStringCustomNumeric.length();
        for (int i = 0; i < lengthCustomNumeric; i++) {
            char c = randomStringCustomNumeric.charAt(i);
            assertTrue("Random character is a numeric character", Character.isDigit(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomEmptyChars() {
        String randomString = RandomStringUtils.random(1, (char[]) null);
        assertEquals("Random string generated from empty char array", "", randomString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyChars2() {
        String randomString = RandomStringUtils.random(1, "");
        assertEquals("Random string generated from empty String", "", randomString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeStart() {
        String randomString = RandomStringUtils.random(10, -1, 1, true, false, null, RANDOM);
        assertEquals("Random string generated with negative start index", "", randomString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeEnd() {
        char[] chars = {'a', 'b', 'c'};
        String randomString = RandomStringUtils.random(10, 0, -1, true, false, chars, RANDOM);
        assertEquals("Random string generated with negative end index", "", randomString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeStartEnd() {
        char[] chars = {'a', 'b', 'c'};
        String randomString = RandomStringUtils.random(10, -1, -1, true, false, chars, RANDOM);
        assertEquals("Random string generated with negative start and end indices", "", randomString);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomArrayOutOfBounds() {
        char[] chars = {'a', 'b', 'c'};
        String randomString = RandomStringUtils.random(10, 3, chars.length + 1, true, false, chars, RANDOM);
        assertEquals("Random string generated with out-of-bounds array index", "", randomString);
    }

    @Test
    public void testRandomCustom() {
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
        String randomString = RandomStringUtils.random(10, chars);
        int length = randomString.length();
        for (int i = 0; i < length; i++) {
            char c = randomString.charAt(i);
            assertTrue("Random character is present in the given character array", Arrays.binarySearch(chars, c) >= 0);
        }
    }
}