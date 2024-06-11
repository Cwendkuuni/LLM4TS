package org.apache.commons.lang3;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        int count = 5;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertNotNull(randomAsciiString);
        assertEquals(count, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        int count = 8;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertNotNull(randomAlphabeticString);
        assertEquals(count, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        int count = 7;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertNotNull(randomAlphanumericString);
        assertEquals(count, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumericString() {
        int count = 6;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertNotNull(randomNumericString);
        assertEquals(count, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomStringWithCustomCharacterSet() {
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int count = 12;
        String randomString = RandomStringUtils.random(count, characterSet.toCharArray());
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(characterSet.indexOf(c) != -1);
        }
    }

    @Test
    public void testRandomStringWithEmptyCharacterSet() {
        String emptyCharacterSet = "";
        int count = 15;
        try {
            RandomStringUtils.random(count, emptyCharacterSet.toCharArray());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testRandomStringWithInvalidCharacterSet() {
        String invalidCharacterSet = "!@#$%&*()_+=[]{}|;:<>,.?/\\";
        int count = 11;
        try {
            RandomStringUtils.random(count, invalidCharacterSet.toCharArray());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testRandomStringWithSurrogateCharacters() {
        String randomString = RandomStringUtils.random(2, "𠀀𠀁𠀂𠀃𠀄𠀅𠀆𠀇𠀈𠀉𠀋𠀌𠀍𠀎𠀏");
        assertNotNull(randomString);
        assertEquals(2, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (c >= 55296 && c <= 56191) {
                assertTrue(randomString.indexOf("𠀀") != -1 || randomString.indexOf("𠀁") != -1);
                assertTrue(randomString.lastIndexOf("𠀎") != -1 || randomString.lastIndexOf("𠀏") != -1);
            } else {
                assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '\uD800' || c == '\uDC00');
            }
        }
    }

    @Test
    public void testRandomStringWithNoSurrogateCharacters() {
        String randomString = RandomStringUtils.random(3, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
        assertNotNull(randomString);
        assertEquals(3, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

}