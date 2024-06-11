package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        // Test random string creation with default settings
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(randomString.length(), 10);

        // Test random string creation with specific character set and length
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String randomStringWithSet = RandomStringUtils.random(5, characterSet.toCharArray());
        assertNotNull(randomStringWithSet);
        assertEquals(randomStringWithSet.length(), 5);
    }

    @Test
    public void testRandomAsciiString() {
        // Test random string creation with ASCII only characters
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(randomAsciiString.length(), 10);
    }

    @Test
    public void testRandomAlphabeticString() {
        // Test random string creation with alphabetic characters only
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(randomAlphabeticString.length(), 10);
    }

    @Test
    public void testRandomAlphanumericString() {
        // Test random string creation with alphanumeric characters only
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(randomAlphanumericString.length(), 10);
    }

    @Test
    public void testRandomNumericString() {
        // Test random string creation with numeric characters only
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(randomNumericString.length(), 10);
    }

    @Test
    public void testRandomStringWithCustomSettings() {
        // Test random string creation with custom character set, length, letters, and numbers
        String characterSet = "0123456789ABCDEF";
        String randomStringWithCustomSettings = RandomStringUtils.random(5, true, true, characterSet.toCharArray());
        assertNotNull(randomStringWithCustomSettings);
        assertEquals(randomStringWithCustomSettings.length(), 5);
    }

    @Test
    public void testRandomStringWithStartAndEnd() {
        // Test random string creation with custom character set, length, start, and end
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String randomStringWithStartAndEnd = RandomStringUtils.random(5, 3, 8, characterSet.toCharArray());
        assertNotNull(randomStringWithStartAndEnd);
        assertEquals(randomStringWithStartAndEnd.length(), 5);
    }

    @Test
    public void testRandomStringWithCustomSettingsAndChars() {
        // Test random string creation with custom character set, length, start, end, letters, and numbers
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomStringWithCustomSettingsAndChars = RandomStringUtils.random(5, 3, 8, true, true, characterSet.toCharArray());
        assertNotNull(randomStringWithCustomSettingsAndChars);
        assertEquals(randomStringWithCustomSettingsAndChars.length(), 5);
    }

    @Test
    public void testRandomStringWithCustomSourceOfRandomness() {
        // Test random string creation with custom character set, length, start, end, letters, numbers, and a provided Random instance
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random customRandom = new Random(12345L);
        String randomStringWithCustomSourceOfRandomness = RandomStringUtils.random(5, 3, 8, true, true, characterSet.toCharArray(), customRandom);
        assertNotNull(randomStringWithCustomSourceOfRandomness);
        assertEquals(randomStringWithCustomSourceOfRandomness.length(), 5);
    }
}