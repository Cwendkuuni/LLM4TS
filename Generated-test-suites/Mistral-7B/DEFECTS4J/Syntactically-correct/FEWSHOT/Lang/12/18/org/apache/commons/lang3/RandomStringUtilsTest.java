package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertTrue(randomString.length() == 10);
        assertEquals(true, randomString.matches("[\u0000-\u007F\u0080-\u00FF]+"));

        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertTrue(randomAsciiString.length() == 10);
        assertEquals(true, randomAsciiString.matches("[\u0020-\u007E]+"));

        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertTrue(randomAlphabeticString.length() == 10);
        assertEquals(true, randomAlphabeticString.matches("[a-zA-Z]+"));

        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertTrue(randomAlphanumericString.length() == 10);
        assertEquals(true, randomAlphanumericString.matches("[a-zA-Z0-9]+"));

        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertTrue(randomNumericString.length() == 10);
        assertEquals(true, randomNumericString.matches("[0-9]+"));

        String randomStringWithRange = RandomStringUtils.random(10, 48, 57);
        assertTrue(randomStringWithRange.length() == 10);
        assertEquals(true, randomStringWithRange.matches("[0-9]+"));

        String randomStringWithCustomChars = RandomStringUtils.random(10, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        assertTrue(randomStringWithCustomChars.length() == 10);
        assertEquals(true, randomStringWithCustomChars.matches("[a-zA-Z0-9]+"));

        String randomStringWithCustomCharsAndLengthConstraints = RandomStringUtils.random(1, 3, 5, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        assertTrue(randomStringWithCustomCharsAndLengthConstraints.length() >= 1 && randomStringWithCustomCharsAndLengthConstraints.length() <= 5);
        assertEquals(true, randomStringWithCustomCharsAndLengthConstraints.matches("[a-zA-Z0-9]+"));

        String randomEmptyString = RandomStringUtils.random(0);
        assertEquals("", randomEmptyString);

        String randomNegativeLengthString = RandomStringUtils.random(-1);
        assertNull(randomNegativeLengthString);

        String randomNullChars = RandomStringUtils.random(10, null);
        assertNull(randomNullChars);
    }

    @Test
    public void testRandomWithCustomRandom() {
        Random customRandom = new Random(12345);
        String randomString = RandomStringUtils.random(10, customRandom);
        assertNotNull(randomString);
        assertEquals(true, randomString.matches("[\u0000-\u007F\u0080-\u00FF]+"));

        RandomStringUtils.seedRandom(customRandom);
        String randomStringSeeded = RandomStringUtils.random(10);
        assertNotNull(randomStringSeeded);
        assertEquals(randomString, randomStringSeeded);
    }

    @Test
    public void testRandomWithSurrogateCharacters() {
        String randomStringWithSurrogate = RandomStringUtils.random(10, 0x20000, 0x3FFFF, false, false, null, RANDOM);
        assertTrue(randomStringWithSurrogate.length() >= 2);
        assertFalse(randomStringWithSurrogate.matches("[\\p{ASCII}]+"));
    }
}