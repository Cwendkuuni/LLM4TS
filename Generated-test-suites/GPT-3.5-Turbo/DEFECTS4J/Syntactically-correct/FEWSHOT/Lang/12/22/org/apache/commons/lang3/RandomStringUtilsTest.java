package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RandomStringUtilsTest {
    @Test
    public void testRandom() {
        String result = RandomStringUtils.random(10);
        assertEquals(10, result.length());
    }

    @Test
    public void testRandomAscii() {
        String result = RandomStringUtils.randomAscii(10);
        assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue((int) c >= 32 && (int) c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String result = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, result.length());
        assertTrue(result.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String result = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, result.length());
        assertTrue(result.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        String result = RandomStringUtils.randomNumeric(10);
        assertEquals(10, result.length());
        assertTrue(result.matches("[0-9]+"));
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String result = RandomStringUtils.random(10, true, true);
        assertEquals(10, result.length());
        assertTrue(result.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomWithLettersOnly() {
        String result = RandomStringUtils.random(10, true, false);
        assertEquals(10, result.length());
        assertTrue(result.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomWithNumbersOnly() {
        String result = RandomStringUtils.random(10, false, true);
        assertEquals(10, result.length());
        assertTrue(result.matches("[0-9]+"));
    }

    @Test
    public void testRandomWithCustomChars() {
        String result = RandomStringUtils.random(10, 65, 90, true, false);
        assertEquals(10, result.length());
        assertTrue(result.matches("[A-Z]+"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyChars() {
        RandomStringUtils.random(10, "");
    }

    @Test
    public void testRandomWithNullChars() {
        String result = RandomStringUtils.random(10, null);
        assertEquals(10, result.length());
    }
}