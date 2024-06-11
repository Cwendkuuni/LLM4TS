package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        // typical use case
        assertTrue(randomString.matches("[0-9a-zA-Z]"));

        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        // typical use case
        assertTrue(randomAsciiString.matches("[\\x00-\\x7F]"));

        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        // typical use case
        assertTrue(randomAlphabeticString.matches("[a-zA-Z]"));

        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        // typical use case
        assertTrue(randomAlphanumericString.matches("[0-9a-zA-Z]"));

        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        // typical use case
        assertTrue(randomNumericString.matches("[0-9]"));

        String randomStringWithRangeAndOptions = RandomStringUtils.random(10, 65, 90, true, false);
        assertNotNull(randomStringWithRangeAndOptions);
        assertEquals(10, randomStringWithRangeAndOptions.length());
        // typical use case
        assertTrue(randomStringWithRangeAndOptions.matches("[A-Z]"));

        String randomStringWithChars = RandomStringUtils.random(10, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray());
        assertNotNull(randomStringWithChars);
        assertEquals(10, randomStringWithChars.length());
        // typical use case
        assertTrue(randomStringWithChars.matches("[a-zA-Z0-9]"));

        String randomStringWithEmptyChars = RandomStringUtils.random(10, (char[])null);
        assertNotNull(randomStringWithEmptyChars);
        assertEquals(10, randomStringWithEmptyChars.length());
        // edge case
        assertFalse(randomStringWithEmptyChars.isEmpty());

        String randomStringWithInvalidLength = RandomStringUtils.random(-1);
        assertNull(randomStringWithInvalidLength);

        String randomStringWithInvalidCharacterSet = RandomStringUtils.random(1, 'a', 'z', false, false, null);
        assertNull(randomStringWithInvalidCharacterSet);
    }

    @Test
    public void testRandomStringWithSurrogates() {
        String randomString = RandomStringUtils.random(10, false, false);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        // typical use case
        assertTrue(randomString.matches("[\\p{JavaScriptIdentifierStart}\\p{JavaScriptIdentifierPart}\\p{JavaScriptUnicodeScape}]"));

        String randomSurrogateString = RandomStringUtils.random(2);
        assertNotNull(randomSurrogateString);
        assertEquals(2, randomSurrogateString.length());
        // edge case
        assertTrue(randomSurrogateString.matches("([\\uD800]|[\\uDC00-\\uDFFF])([\\uD800]|[\\uDC00-\\uDFFF])?"));
    }
}