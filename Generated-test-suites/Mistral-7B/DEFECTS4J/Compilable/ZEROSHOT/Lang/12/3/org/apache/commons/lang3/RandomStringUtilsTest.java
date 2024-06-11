package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        // Test random string of specified length
        int length = 10;
        String randomString = RandomStringUtils.random(length);
        assertEquals("Length of generated random string should match", length, randomString.length());

        // Test random string of specified length with ascii characters only
        String randomAsciiString = RandomStringUtils.randomAscii(length);
        assertEquals("Length of generated random ascii string should match", length, randomAsciiString.length());

        // Test random string of specified length with alphabetic characters only
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(length);
        assertEquals("Length of generated random alphabetic string should match", length, randomAlphabeticString.length());

        // Test random string of specified length with alphanumeric characters only
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(length);
        assertEquals("Length of generated random alphanumeric string should match", length, randomAlphanumericString.length());

        // Test random string of specified length with numeric characters only
        String randomNumericString = RandomStringUtils.randomNumeric(length);
        assertEquals("Length of generated random numeric string should match", length, randomNumericString.length());
    }

    @Test
    public void testRandomStringWithOptions() {
        // Test random string of specified length with specified character options
        int length = 10;
        boolean onlyLetters = true;
        boolean onlyNumbers = false;
        String randomString = RandomStringUtils.random(length, onlyLetters, onlyNumbers);
        assertEquals("Length of generated random string should match", length, randomString.length());

        // Test random string of specified length with start and end character options
        int start = 32;
        int end = 126;
        String randomAsciiString = RandomStringUtils.random(length, start, end, false, false);
        assertEquals("Length of generated random ascii string should match", length, randomAsciiString.length());

        // Test random string of specified length with character array options
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
        String randomStringWithChars = RandomStringUtils.random(length, chars);
        assertEquals("Length of generated random string with chars should match", length, randomStringWithChars.length());

        // Test random string of specified length with string character options
        String charsString = "abcdefghij";
        String randomStringWithStringChars = RandomStringUtils.random(length, charsString);
        assertEquals("Length of generated random string with string chars should match", length, randomStringWithStringChars.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithNegativeLength() {
        // Test exception when generating random string with negative length
        int length = -1;
        RandomStringUtils.random(length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptyCharacterArray() {
        // Test exception when generating random string with empty character array
        char[] emptyChars = new char[0];
        RandomStringUtils.random(1, emptyChars);
    }
}