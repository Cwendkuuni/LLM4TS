package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for RandomStringUtils class.
 */
public class RandomStringUtilsTest {

    /**
     * Test method for {@link RandomStringUtils#random(int)} .
     */
    @Test
    public void testRandomString() {
        int length = 10;
        String randomString = RandomStringUtils.random(length);
        assertEquals("Length should be equal to the specified value", length, randomString.length());
        // Check if all characters in the string are printable
        for (int i = 0; i < length; i++) {
            assert(Character.isISOControl(randomString.charAt(i)) == false);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAscii(int)} .
     */
    @Test
    public void testRandomAsciiString() {
        int length = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(length);
        assertEquals("Length should be equal to the specified value", length, randomAsciiString.length());
        // Check if all characters in the string are printable ASCII characters
        for (int i = 0; i < length; i++) {
            assert(Character.isISOControl(randomAsciiString.charAt(i)) == false);
            assert((int) randomAsciiString.charAt(i) >= 32 && (int) randomAsciiString.charAt(i) <= 126);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphabetic(int)} .
     */
    @Test
    public void testRandomAlphabeticString() {
        int length = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(length);
        assertEquals("Length should be equal to the specified value", length, randomAlphabeticString.length());
        // Check if all characters in the string are alphabetic
        for (int i = 0; i < length; i++) {
            assert(Character.isLetter(randomAlphabeticString.charAt(i)) == true);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#randomAlphanumeric(int)} .
     */
    @Test
    public void testRandomAlphanumericString() {
        int length = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(length);
        assertEquals("Length should be equal to the specified value", length, randomAlphanumericString.length());
        // Check if all characters in the string are alphanumeric
        for (int i = 0; i < length; i++) {
            assert(Character.isLetter(randomAlphanumericString.charAt(i)) || Character.isDigit(randomAlphanumericString.charAt(i)));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean)} .
     */
    @Test
    public void testRandomStringWithRangeAndTypes() {
        int length = 10;
        int start = 65;
        int end = 90;
        boolean letters = true;
        boolean numbers = false;
        String randomString = RandomStringUtils.random(length, start, end, letters, numbers);
        assertEquals("Length should be equal to the specified value", length, randomString.length());
        // Check if all characters in the string are alphabetic
        for (int i = 0; i < length; i++) {
            assert(Character.isLetter(randomString.charAt(i)) == true);
            assert((int) randomString.charAt(i) >= start && (int) randomString.charAt(i) <= end);
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} .
     */
    @Test
    public void testRandomStringWithCustomCharset() {
        int length = 10;
        int start = 33;
        int end = 47;
        boolean letters = false;
        boolean numbers = false;
        char[] customCharset = {'!', '(', '_', '+', '-', '.'};
        String randomString = RandomStringUtils.random(length, start, end, letters, numbers, customCharset, new Random());
        assertEquals("Length should be equal to the specified value", length, randomString.length());
        // Check if all characters in the string are in the custom charset
        for (int i = 0; i < length; i++) {
            assert(Character.toString(randomString.charAt(i)).equals(Character.toString(customCharset[new Random().nextInt(customCharset.length)])) || (start <= randomString.charAt(i) && randomString.charAt(i) <= end));
        }
    }

    /**
     * Test method for {@link RandomStringUtils#random(int, String)} .
     */
    @Test
    public void testRandomStringWithCustomCharsetFromString() {
        int length = 10;
        String customCharset = "!()_+-.";
        String randomString = RandomStringUtils.random(length, customCharset);
        assertEquals("Length should be equal to the specified value", length, randomString.length());
        // Check if all characters in the string are in the custom charset
        for (int i = 0; i < length; i++) {
            assert(customCharset.indexOf(String.valueOf(randomString.charAt(i))) > -1);
        }
    }
}