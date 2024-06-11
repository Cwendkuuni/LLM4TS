package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

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
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomAlphabeticString);
        assertEquals(5, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomAlphanumericString);
        assertEquals(5, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomNumericString);
        assertEquals(5, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithEdgeCases() {
        // Testing edge cases for random method
        String randomStringWithEdge = RandomStringUtils.random(0);
        assertEquals("", randomStringWithEdge);

        randomStringWithEdge = RandomStringUtils.random(-5);
        assertEquals("", randomStringWithEdge);
    }

    @Test
    public void testRandomAsciiWithEdgeCases() {
        // Testing edge cases for randomAscii method
        String randomAsciiStringWithEdge = RandomStringUtils.randomAscii(0);
        assertEquals("", randomAsciiStringWithEdge);

        randomAsciiStringWithEdge = RandomStringUtils.randomAscii(-5);
        assertEquals("", randomAsciiStringWithEdge);
    }

    @Test
    public void testRandomAlphabeticWithEdgeCases() {
        // Testing edge cases for randomAlphabetic method
        String randomAlphabeticStringWithEdge = RandomStringUtils.randomAlphabetic(0);
        assertEquals("", randomAlphabeticStringWithEdge);

        randomAlphabeticStringWithEdge = RandomStringUtils.randomAlphabetic(-5);
        assertEquals("", randomAlphabeticStringWithEdge);
    }

    @Test
    public void testRandomAlphanumericWithEdgeCases() {
        // Testing edge cases for randomAlphanumeric method
        String randomAlphanumericStringWithEdge = RandomStringUtils.randomAlphanumeric(0);
        assertEquals("", randomAlphanumericStringWithEdge);

        randomAlphanumericStringWithEdge = RandomStringUtils.randomAlphanumeric(-5);
        assertEquals("", randomAlphanumericStringWithEdge);
    }

    @Test
    public void testRandomNumericWithEdgeCases() {
        // Testing edge cases for randomNumeric method
        String randomNumericStringWithEdge = RandomStringUtils.randomNumeric(0);
        assertEquals("", randomNumericStringWithEdge);

        randomNumericStringWithEdge = RandomStringUtils.randomNumeric(-5);
        assertEquals("", randomNumericStringWithEdge);
    }

}