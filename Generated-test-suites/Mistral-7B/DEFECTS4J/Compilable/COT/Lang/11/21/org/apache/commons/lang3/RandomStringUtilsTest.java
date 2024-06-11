package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomAsciiString);
        assertEquals(5, randomAsciiString.length());
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomAlphabeticString);
        assertEquals(5, randomAlphabeticString.length());
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomAlphanumericString);
        assertEquals(5, randomAlphanumericString.length());
    }

    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomNumericString);
        assertEquals(5, randomNumericString.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLengthString() {
        RandomStringUtils.random(-5);
    }

    @Test
    public void testRandomStringWithLettersAndNumbers() {
        String randomStringWithLettersAndNumbers = RandomStringUtils.random(5, true, true);
        assertNotNull(randomStringWithLettersAndNumbers);
        assertEquals(5, randomStringWithLettersAndNumbers.length());
    }

    @Test
    public void testRandomStringWithStartAndEnd() {
        String randomStringWithStartAndEnd = RandomStringUtils.random(5, 'a', 'z');
        assertNotNull(randomStringWithStartAndEnd);
        assertEquals(5, randomStringWithStartAndEnd.length());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringWithInvalidStartAndEnd() {
        RandomStringUtils.random(5, 'z', 'a');
    }

    @Test
    public void testRandomStringWithCustomChars() {
        String customChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomStringWithCustomChars = RandomStringUtils.random(5, customChars.toCharArray());
        assertNotNull(randomStringWithCustomChars);
        assertEquals(5, randomStringWithCustomChars.length());
    }

    @Test
    public void testRandomStringWithEmptyCharsArray() {
        RandomStringUtils.random(5, (char[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptyCustomChars() {
        String customChars = "";
        RandomStringUtils.random(5, customChars.toCharArray());
    }
}