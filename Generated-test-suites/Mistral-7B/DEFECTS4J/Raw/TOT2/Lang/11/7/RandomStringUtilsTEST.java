// RandomStringUtilsTest.java

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        // Test random string with default settings
        String randomString = RandomStringUtils.random(10);
        assertEquals("Length should be 10", 10, randomString.length());
        // Test random string with alphabetic characters only
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertEquals("Length should be 10 and contain only alphabetic characters", 10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertEquals("Should contain only alphabetic characters", Character.isAlphabetic(c), true);
        }
        // Test random string with numeric characters only
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertEquals("Length should be 10 and contain only numeric characters", 10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertEquals("Should contain only numeric characters", Character.isDigit(c), true);
        }
        // Test random string with alphanumeric characters
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals("Length should be 10 and contain only alphanumeric characters", 10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertEquals("Should contain only alphanumeric characters", Character.isAlphabetic(c) || Character.isDigit(c), true);
        }
    }

    @Test
    public void testRandomStringWithCustomLength() {
        // Test random string with custom length and default character set
        String randomString = RandomStringUtils.random(5);
        assertEquals("Length should be 5", 5, randomString.length());
        // Test random string with custom length and specific character set
        String randomStringWithSet = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10);
        assertEquals("Length should be 10", 10, randomStringWithSet.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() {
        RandomStringUtils.random(-5);
    }

    @Test
    public void testEdgeCases() {
        // Test edge cases with minimum and maximum length
        String randomMinLength = RandomStringUtils.random(0);
        assertEquals("Length should be 0", 0, randomMinLength.length());
        String randomMaxLength = RandomStringUtils.random(Integer.MAX_VALUE);
        assertEquals("Length should be less than or equal to Integer.MAX_VALUE", Integer.MAX_VALUE, randomMaxLength.length());
        // Test edge cases with empty character set
        String randomEmptySet = RandomStringUtils.random(5, (char[]) null);
        assertEquals("Length should be 5 and contain at least one character", 5, randomEmptySet.length());
        // Test edge cases with invalid character positions
        String randomInvalidStartEnd = RandomStringUtils.random(5, 10, -5, true, false);
        assertEquals("Length should be between 0 and the size of the character set", 5, randomInvalidStartEnd.length());
    }

    @Test
    public void testRandomStringWithSurrogateCharacters() {
        String randomString = RandomStringUtils.random(1, true, true);
        assertEquals("Length should be 1", 1, randomString.length());
        char firstChar = randomString.charAt(0);
        if (firstChar > Character.MIN_HIGH_SURROGATE && firstChar < Character.MAX_HIGH_SURROGATE) {
            String secondString = RandomStringUtils.random(1, true, true);
            char secondChar = secondString.charAt(0);
            assertEquals("Should form a valid surrogate pair", Character.isHighSurrogate(firstChar) && Character.isLowSurrogate(secondChar), true);
        }
    }

    // 
}