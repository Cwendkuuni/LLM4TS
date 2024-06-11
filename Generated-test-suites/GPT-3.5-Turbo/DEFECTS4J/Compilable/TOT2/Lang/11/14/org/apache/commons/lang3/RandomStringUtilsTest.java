package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Typical use case
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        // Edge case
        randomString = RandomStringUtils.random(0);
        assertNotNull(randomString);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomAscii() {
        // Typical use case
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());

        // Edge case
        randomAsciiString = RandomStringUtils.randomAscii(0);
        assertNotNull(randomAsciiString);
        assertEquals("", randomAsciiString);
    }

    @Test
    public void testRandomAlphabetic() {
        // Typical use case
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());

        // Edge case
        randomAlphabeticString = RandomStringUtils.randomAlphabetic(0);
        assertNotNull(randomAlphabeticString);
        assertEquals("", randomAlphabeticString);
    }

    @Test
    public void testRandomAlphanumeric() {
        // Typical use case
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());

        // Edge case
        randomAlphanumericString = RandomStringUtils.randomAlphanumeric(0);
        assertNotNull(randomAlphanumericString);
        assertEquals("", randomAlphanumericString);
    }

    @Test
    public void testRandomNumeric() {
        // Typical use case
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());

        // Edge case
        randomNumericString = RandomStringUtils.randomNumeric(0);
        assertNotNull(randomNumericString);
        assertEquals("", randomNumericString);
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        // Typical use case
        String randomString = RandomStringUtils.random(10, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        // Edge case
        randomString = RandomStringUtils.random(0, true, true);
        assertNotNull(randomString);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomWithCustomChars() {
        // Typical use case
        String randomString = RandomStringUtils.random(10, new char[] {'a', 'b', 'c'});
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        // Edge case
        randomString = RandomStringUtils.random(0, new char[] {'a', 'b', 'c'});
        assertNotNull(randomString);
        assertEquals("", randomString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

}