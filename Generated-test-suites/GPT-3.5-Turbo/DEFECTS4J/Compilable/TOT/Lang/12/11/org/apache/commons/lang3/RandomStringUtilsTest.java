package org.apache.commons.lang3;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(5, true, true);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomWithStartAndEnd() {
        String randomString = RandomStringUtils.random(5, 65, 70, true, false);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 65 && c <= 70);
            assertTrue(Character.isLetter(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-5);
    }

    @Test
    public void testRandomWithEmptyChars() {
        String randomString = RandomStringUtils.random(5, "");
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomWithStringChars() {
        String randomString = RandomStringUtils.random(5, "abc");
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c == 'a' || c == 'b' || c == 'c');
        }
    }

}