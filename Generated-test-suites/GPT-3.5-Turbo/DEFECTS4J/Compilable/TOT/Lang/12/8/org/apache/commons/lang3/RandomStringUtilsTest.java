package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Typical Use Case
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        // Edge Cases
        randomString = RandomStringUtils.random(0);
        assertNotNull(randomString);
        assertEquals(0, randomString.length());

        // Error Scenarios
        try {
            randomString = RandomStringUtils.random(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testRandomAscii() {
        // Typical Use Case
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        // Edge Cases
        randomString = RandomStringUtils.randomAscii(0);
        assertNotNull(randomString);
        assertEquals(0, randomString.length());

        // Error Scenarios
        try {
            randomString = RandomStringUtils.randomAscii(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testRandomAlphabetic() {
        // Typical Use Case
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[a-zA-Z]+"));

        // Edge Cases
        randomString = RandomStringUtils.randomAlphabetic(0);
        assertNotNull(randomString);
        assertEquals(0, randomString.length());

        // Error Scenarios
        try {
            randomString = RandomStringUtils.randomAlphabetic(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        // Typical Use Case
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[a-zA-Z0-9]+"));

        // Edge Cases
        randomString = RandomStringUtils.randomAlphanumeric(0);
        assertNotNull(randomString);
        assertEquals(0, randomString.length());

        // Error Scenarios
        try {
            randomString = RandomStringUtils.randomAlphanumeric(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testRandomNumeric() {
        // Typical Use Case
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[0-9]+"));

        // Edge Cases
        randomString = RandomStringUtils.randomNumeric(0);
        assertNotNull(randomString);
        assertEquals(0, randomString.length());

        // Error Scenarios
        try {
            randomString = RandomStringUtils.randomNumeric(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testRandomWithCharsArray() {
        // Typical Use Case
        String randomString = RandomStringUtils.random(10, new char[]{'a', 'b', 'c'});
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[abc]+"));

        // Edge Cases
        randomString = RandomStringUtils.random(0, new char[]{'a', 'b', 'c'});
        assertNotNull(randomString);
        assertEquals(0, randomString.length());

        // Error Scenarios
        try {
            randomString = RandomStringUtils.random(10, new char[0]);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
        try {
            randomString = RandomStringUtils.random(-1, new char[]{'a', 'b', 'c'});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testRandomWithString() {
        // Typical Use Case
        String randomString = RandomStringUtils.random(10, "abc");
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[abc]+"));

        // Edge Cases
        randomString = RandomStringUtils.random(0, "abc");
        assertNotNull(randomString);
        assertEquals(0, randomString.length());

        // Error Scenarios
        try {
            randomString = RandomStringUtils.random(10, "");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
        try {
            randomString = RandomStringUtils.random(-1, "abc");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
}