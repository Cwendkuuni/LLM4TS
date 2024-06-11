package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    /**
     * Test for {@link RandomStringUtils#random(int)} method.
     */
    @Test
    public void testRandomString() {
        // Test generating random strings of different lengths
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(i);
            assertEquals("Random string of length " + i, i, randomString.length());
        }

        // Test generating random strings with alphabetic characters
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertTrue("Random alphabetic string should only contain alphabetic characters",
                allMatches(randomAlphabeticString, "[a-zA-Z]"));

        // Test generating random strings with numeric characters
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertTrue("Random numeric string should only contain numeric characters",
                allMatches(randomNumericString, "[0-9]"));

        // Test generating random strings with alphanumeric characters
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertTrue("Random alphanumeric string should only contain alphanumeric characters",
                allMatches(randomAlphanumericString, "[a-zA-Z0-9]"));

        // Test generating random strings with custom character set
        char[] customChars = {'a', 'b', 'c', 'd', 'e', 'f'};
        String randomCustomString = RandomStringUtils.random(10, customChars);
        assertTrue("Random custom string should only contain specified characters",
                allMatches(randomCustomString, new String(customChars)));
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean)} method.
     */
    @Test
    public void testRandomStringWithOptions() {
        // Test generating random strings with only alphabetic characters
        String randomAlphabeticString = RandomStringUtils.random(10, true, false);
        assertTrue("Random alphabetic string should only contain alphabetic characters",
                allMatches(randomAlphabeticString, "[a-zA-Z]"));

        // Test generating random strings with only numeric characters
        String randomNumericString = RandomStringUtils.random(10, false, true);
        assertTrue("Random numeric string should only contain numeric characters",
                allMatches(randomNumericString, "[0-9]"));

        // Test generating random strings with both alphabetic and numeric characters
        String randomAlphanumericString = RandomStringUtils.random(10, true, true);
        assertTrue("Random alphanumeric string should only contain alphanumeric characters",
                allMatches(randomAlphanumericString, "[a-zA-Z0-9]"));

        // Test generating random strings with custom character set and options
        char[] customChars = {'a', 'b', 'c', 'd', 'e', 'f'};
        String randomCustomString = RandomStringUtils.random(10, 0, 0, true, true, customChars);
        assertTrue("Random custom string should only contain specified characters",
                allMatches(randomCustomString, new String(customChars)));
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method.
     */
    @Test
    public void testRandomStringWithCustomRandom() {
        // Prepare custom random number generator
        Random customRandom = new Random();

        // Test generating random strings with custom character set and random number generator
        String randomCustomString = RandomStringUtils.random(10, 0, 0, false, false, null, customRandom);
        assertNotSame("Generated strings should be different each time",
                randomCustomString, RandomStringUtils.random(10, 0, 0, false, false, null, customRandom));
    }

    /**
     * Helper method for checking if all characters in a String match a given regular expression.
     *
     * @param str the String to test
     * @param regex the regular expression to match against
     * @return true if all characters in the String match the given regular expression
     */
    private boolean allMatches(String str, String regex) {
        for (char c : str.toCharArray()) {
            if (!Pattern.matches(regex, Character.toString(c))) {
                return false;
            }
        }
        return true;
    }

}