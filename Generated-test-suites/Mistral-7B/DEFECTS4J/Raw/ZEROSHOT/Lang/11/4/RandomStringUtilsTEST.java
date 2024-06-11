package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RandomStringUtilsTest {

    @Test
    public void testRandomStringLength() {
        // Test random string length
        int length = 10;
        String randomString = RandomStringUtils.random(length);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        // Test random string with ASCII characters only
        int length = 10;
        String randomString = RandomStringUtils.randomAscii(length);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            int randomChar = randomString.charAt(i);
            assertTrue("Random character is an ASCII character", (randomChar >= 32 && randomChar <= 126));
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        // Test random string with alphabetic characters only
        int length = 10;
        String randomString = RandomStringUtils.randomAlphabetic(length);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            int randomChar = randomString.charAt(i);
            assertTrue("Random character is an alphabetic character", Character.isAlphabetic((char) randomChar));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        // Test random string with alphanumeric characters only
        int length = 10;
        String randomString = RandomStringUtils.randomAlphanumeric(length);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            int randomChar = randomString.charAt(i);
            assertTrue("Random character is an alphanumeric character", Character.isAlphabetic((char) randomChar) || Character.isDigit((char) randomChar));
        }
    }

    @Test
    public void testRandomNumericString() {
        // Test random string with numeric characters only
        int length = 10;
        String randomString = RandomStringUtils.randomNumeric(length);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            int randomChar = randomString.charAt(i);
            assertTrue("Random character is a numeric character", Character.isDigit((char) randomChar));
        }
    }

    @Test
    public void testRandomStringWithCustomSet() {
        // Test random string with custom character set
        int length = 10;
        String characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(length, characterSet.toCharArray());
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            int randomCharIndex = i % characterSet.length();
            int randomChar = characterSet.charAt(randomCharIndex);
            assertTrue("Random character is in the set", randomString.indexOf(randomChar) >= 0);
        }
    }

    @Test
    public void testRandomStringWithStartAndEnd() {
        // Test random string with start and end characters
        int length = 10;
        char start = 'a';
        char end = 'e';
        String randomString = RandomStringUtils.random(length, start, end, false, false, null, RANDOM);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            int randomChar = randomString.charAt(i);
            assertTrue("Random character is between start and end characters", (start <= randomChar && randomChar <= end));
        }
    }

    @Test
    public void testRandomStringWithLettersAndNumbers() {
        // Test random string with letters and numbers
        int length = 10;
        String randomString = RandomStringUtils.random(length, true, true);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            int randomChar = randomString.charAt(i);
            assertTrue("Random character is an alphanumeric character", Character.isAlphabetic((char) randomChar) || Character.isDigit((char) randomChar));
        }
    }

    @Test
    public void testRandomStringWithStringSet() {
        // Test random string with a string set
        int length = 10;
        String characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(length, characterSet);
        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        for (int i = 0; i < length; i++) {
            int randomCharIndex = i % characterSet.length();
            int randomChar = characterSet.charAt(randomCharIndex);
            assertTrue("Random character is in the set", randomString.indexOf(randomChar) >= 0);
        }
    }

}