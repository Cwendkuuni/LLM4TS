package org.apache.commons.lang3;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String random = RandomStringUtils.random(5);
        assertTrue(random.length() == 5);
    }

    @Test
    public void testRandomAscii() {
        String randomAscii = RandomStringUtils.randomAscii(5);
        assertTrue(randomAscii.length() == 5);
        assertTrue(randomAscii.matches("[\\x20-\\x7E]+"));
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabetic = RandomStringUtils.randomAlphabetic(5);
        assertTrue(randomAlphabetic.length() == 5);
        assertTrue(randomAlphabetic.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(5);
        assertTrue(randomAlphanumeric.length() == 5);
        assertTrue(randomAlphanumeric.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        String randomNumeric = RandomStringUtils.randomNumeric(5);
        assertTrue(randomNumeric.length() == 5);
        assertTrue(randomNumeric.matches("[0-9]+"));
    }

    @Test
    public void testRandomWithChars() {
        String random = RandomStringUtils.random(5, "ABCDEF");
        assertTrue(random.length() == 5);
        assertTrue(random.matches("[ABCDEF]+"));
    }

    @Test
    public void testRandomWithCharArray() {
        String random = RandomStringUtils.random(5, new char[]{'A', 'B', 'C', 'D', 'E', 'F'});
        assertTrue(random.length() == 5);
        assertTrue(random.matches("[ABCDEF]+"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidCount() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidCountAndChars() {
        RandomStringUtils.random(-5, "ABCDEF");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyChars() {
        RandomStringUtils.random(5, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharArray() {
        RandomStringUtils.random(5, new char[]{});
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String random = RandomStringUtils.random(5, true, true);
        assertTrue(random.length() == 5);
        assertTrue(random.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomWithOnlyLetters() {
        String random = RandomStringUtils.random(5, true, false);
        assertTrue(random.length() == 5);
        assertTrue(random.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomWithOnlyNumbers() {
        String random = RandomStringUtils.random(5, false, true);
        assertTrue(random.length() == 5);
        assertTrue(random.matches("[0-9]+"));
    }

}