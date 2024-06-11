package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert((c >= 32 && c <= 126));
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithMinMaxBoundsAndLettersOnly() {
        String randomString = RandomStringUtils.random(10, 33, 34, true, false);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || c == 33 || c == 34);
        }
    }

    @Test
    public void testRandomWithMinMaxBoundsAndNumbersOnly() {
        String randomString = RandomStringUtils.random(10, 48, 49, false, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isDigit(c) || c == 48 || c == 49);
        }
    }

    @Test
    public void testRandomWithMinMaxBoundsAndLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, 33, 49, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert((Character.isAlphabetic(c) || Character.isDigit(c) || c == 33 || c == 34 || c == 48 || c == 49));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeLength() {
        String randomString = RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterArray() {
        String randomString = RandomStringUtils.random(10, (char[]) null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithInvalidCharacterSetBounds() {
        String randomString = RandomStringUtils.random(10, 0, -1, false, false, new char[]{'a', 'b'}, RANDOM);
    }
}