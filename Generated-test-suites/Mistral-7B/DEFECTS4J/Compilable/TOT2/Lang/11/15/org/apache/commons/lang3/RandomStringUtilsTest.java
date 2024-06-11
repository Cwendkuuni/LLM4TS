package org.apache.commons.lang3;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Expected a string of length " + count, count, randomString.length());
        assertNotNull("Expected a non-empty string", randomString);
    }

    @Test
    public void testRandomAsciiString() {
        int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals("Expected a string of length " + count, count, randomAsciiString.length());
        assertNotNull("Expected a non-empty string", randomAsciiString);
    }

    @Test
    public void testRandomAlphabeticString() {
        int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Expected a string of length " + count, count, randomAlphabeticString.length());
        assertNotNull("Expected a non-empty string", randomAlphabeticString);
    }

    @Test
    public void testRandomAlphanumericString() {
        int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Expected a string of length " + count, count, randomAlphanumericString.length());
        assertNotNull("Expected a non-empty string", randomAlphanumericString);
    }

    @Test
    public void testRandomNumericString() {
        int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertEquals("Expected a string of length " + count, count, randomNumericString.length());
        assertNotNull("Expected a non-empty string", randomNumericString);
    }

    @Test
    public void testRandomStringWithRange() {
        int count = 10;
        boolean letters = true;
        boolean numbers = true;
        String randomString = RandomStringUtils.random(count, letters, numbers);
        assertEquals("Expected a string of length " + count, count, randomString.length());
        assertNotNull("Expected a non-empty string", randomString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() {
        int count = -10;
        String randomString = RandomStringUtils.random(count);
    }

    @Test
    public void testEmptyCharsArray() {
        int count = 10;
        char[] emptyCharsArray = new char[0];
        String randomString = RandomStringUtils.random(count, emptyCharsArray);
        assertEquals("Expected a string of length " + count, count, randomString.length());
        assertNotNull("Expected a non-empty string", randomString);
    }
}