import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        for (char ch : randomAsciiString.toCharArray()) {
            assertTrue(ch >= 32 && ch <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomAlphabeticString.length());

        for (char ch : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isLetter(ch));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomAlphanumericString.length());

        for (char ch : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(ch));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomNumericString.length());

        for (char ch : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(ch));
        }
    }

    @Test
    public void testRandomWithCustomChars() {
        String chars = "ABC123";
        String randomString = RandomStringUtils.random(10, chars);
        assertEquals(10, randomString.length());

        for (char ch : randomString.toCharArray()) {
            assertTrue(chars.contains(String.valueOf(ch)));
        }
    }

    @Test
    public void testRandomWithCustomCharsRange() {
        String randomString = RandomStringUtils.random(10, 65, 90, true, false);
        assertEquals(10, randomString.length());

        for (char ch : randomString.toCharArray()) {
            assertTrue(ch >= 65 && ch <= 90);
        }
    }

    @Test
    public void testRandomWithCustomCharsAndRandomInstance() {
        String chars = "ABC123";
        String randomString = RandomStringUtils.random(10, chars.toCharArray(), new Random());
        assertEquals(10, randomString.length());

        for (char ch : randomString.toCharArray()) {
            assertTrue(chars.contains(String.valueOf(ch)));
        }
    }
}