package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
        assertNotNull(randomString);
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());
        assertNotNull(randomString);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(randomString.charAt(i) >= 32 && randomString.charAt(i) <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomString.length());
        assertNotNull(randomString);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomString.length());
        assertNotNull(randomString);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomString.length());
        assertNotNull(randomString);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isDigit(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomWithSpecifiedCharset() {
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        assertEquals(10, randomString.length());
        assertNotNull(randomString);
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue(c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f' || c == 'g' || c == 'h' || c == 'i' || c == 'j'
                    || c == 'k' || c == 'l' || c == 'm' || c == 'n' || c == 'o' || c == 'p' || c == 'q' || c == 'r' || c == 's' || c == 't'
                    || c == 'u' || c == 'v' || c == 'w' || c == 'x' || c == 'y' || c == 'z' || c == 'A' || c == 'B' || c == 'C' || c == 'D'
                    || c == 'E' || c == 'F' || c == 'G' || c == 'H' || c == 'I' || c == 'J' || c == 'K' || c == 'L' || c == 'M' || c == 'N'
                    || c == 'O' || c == 'P' || c == 'Q' || c == 'R' || c == 'S' || c == 'T' || c == 'U' || c == 'V' || c == 'W' || c == 'X'
                    || c == 'Y' || c == 'Z' || c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7'
                    || c == '8' || c == '9');
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeLength() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharset() {
        RandomStringUtils.random("", 10);
    }

    @Test
    public void testRandomWithCustomRandom() {
        Random random = new Random(123456789);
        String randomString = RandomStringUtils.random(10, random);
        assertEquals(10, randomString.length());
        assertNotNull(randomString);
    }

    @Test
    public void testRandomWithSpecifiedStartAndEnd() {
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 5, 8);
        assertEquals(4, randomString.length());
        assertNotNull(randomString);
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            boolean isInRange = (c >= 'e' && c <= 'i') || (c >= 'A' && c <= 'F') || (c >= '5' && c <= '8') || (c >= '0' && c <= '5');
            assertTrue(isInRange);
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithInvalidCharset() {
        RandomStringUtils.random(new char[]{'a', 'b'}, 10);
    }

}