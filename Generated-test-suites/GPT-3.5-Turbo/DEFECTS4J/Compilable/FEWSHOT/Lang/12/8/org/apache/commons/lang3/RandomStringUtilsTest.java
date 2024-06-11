package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RandomStringUtilsTest {
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());
        for (char ch : randomString.toCharArray()) {
            assertTrue(ch >= 32 && ch <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomString.length());
        for (char ch : randomString.toCharArray()) {
            assertTrue(Character.isLetter(ch));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomString.length());
        for (char ch : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(ch));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomString.length());
        for (char ch : randomString.toCharArray()) {
            assertTrue(Character.isDigit(ch));
        }
    }

    @Test
    public void testRandomWithChars() {
        String randomString = RandomStringUtils.random(10, "abc");
        assertEquals(10, randomString.length());
        for (char ch : randomString.toCharArray()) {
            assertTrue(ch == 'a' || ch == 'b' || ch == 'c');
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyChars() {
        RandomStringUtils.random(10, "");
    }

    @Test
    public void testRandomWithStartAndEnd() {
        String randomString = RandomStringUtils.random(10, 65, 90, true, false);
        assertEquals(10, randomString.length());
        for (char ch : randomString.toCharArray()) {
            assertTrue(ch >= 65 && ch <= 90);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1);
    }
}