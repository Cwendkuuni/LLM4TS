package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals(count, randomString.length());
        assertTrue(isValidRandomString(randomString));
    }

    @Test
    public void testRandomAscii() {
        int count = 10;
        String randomString = RandomStringUtils.randomAscii(count);
        assertEquals(count, randomString.length());
        assertTrue(isValidRandomString(randomString));
        assertTrue(isValidAsciiRandomString(randomString));
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        assertEquals(count, randomString.length());
        assertTrue(isValidRandomString(randomString));
        assertTrue(isValidAlphabeticRandomString(randomString));
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals(count, randomString.length());
        assertTrue(isValidRandomString(randomString));
        assertTrue(isValidAlphanumericRandomString(randomString));
    }

    @Test
    public void testRandomNumeric() {
        int count = 10;
        String randomString = RandomStringUtils.randomNumeric(count);
        assertEquals(count, randomString.length());
        assertTrue(isValidRandomString(randomString));
        assertTrue(isValidNumericRandomString(randomString));
    }

    private boolean isValidRandomString(String randomString) {
        for (char c : randomString.toCharArray()) {
            if (c >= 32 && c <= 126) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidAsciiRandomString(String randomString) {
        for (char c : randomString.toCharArray()) {
            if (c < 32 || c > 126) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidAlphabeticRandomString(String randomString) {
        for (char c : randomString.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidAlphanumericRandomString(String randomString) {
        for (char c : randomString.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidNumericRandomString(String randomString) {
        for (char c : randomString.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}