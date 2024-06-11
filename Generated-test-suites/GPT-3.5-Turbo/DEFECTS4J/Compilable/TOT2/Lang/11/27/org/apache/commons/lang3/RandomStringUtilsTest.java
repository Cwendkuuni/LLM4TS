package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue((int) c >= 32 && (int) c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testInvalidRandomLength() {
        try {
            RandomStringUtils.random(-1);
            fail("Expected IllegalArgumentException for negative length");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testInvalidCharsArray() {
        try {
            RandomStringUtils.random(10, new char[]{});
            fail("Expected IllegalArgumentException for empty chars array");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRandomWithCustomChars() {
        String customChars = "abc123";
        String randomString = RandomStringUtils.random(10, customChars);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(customChars.contains(String.valueOf(c)));
        }
    }

    @Test
    public void testRandomChars() {
        char[] customChars = { 'a', 'b', 'c', '1', '2', '3' };
        String randomString = RandomStringUtils.random(10, customChars);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(new String(customChars).contains(String.valueOf(c)));
        }
    }

    @Test
    public void testRandomWithInvalidCharArray() {
        try {
            RandomStringUtils.random(10, 0, 0, false, false, null);
            fail("Expected IllegalArgumentException for empty chars array");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRandomWithInvalidCount() {
        try {
            RandomStringUtils.random(-1, 0, 0, false, false, new char[]{ 'a' });
            fail("Expected IllegalArgumentException for negative count");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRandomWithInvalidStartAndEnd() {
        try {
            RandomStringUtils.random(10, 5, 3, false, false, new char[]{ 'a', 'b' });
            fail("Expected ArrayIndexOutOfBoundsException for invalid start and end range");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testRandomWithInvalidStringInput() {
        try {
            RandomStringUtils.random(10, "");
            fail("Expected IllegalArgumentException for empty string input");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}