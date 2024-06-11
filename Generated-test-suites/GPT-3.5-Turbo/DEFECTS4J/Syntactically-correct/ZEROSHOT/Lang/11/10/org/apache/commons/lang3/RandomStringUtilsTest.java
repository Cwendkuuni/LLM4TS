import org.junit.Test;

import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 5;
        String randomString = RandomStringUtils.random(count);
        assertEquals(count, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        int count = 5;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals(count, randomAsciiString.length());

        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 5;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertEquals(count, randomAlphabeticString.length());

        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 5;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals(count, randomAlphanumericString.length());

        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        int count = 5;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertEquals(count, randomNumericString.length());

        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithCharacters() {
        int count = 5;
        char[] chars = {'a', 'b', 'c', 'd', 'e'};
        String randomString = RandomStringUtils.random(count, chars);
        assertEquals(count, randomString.length());

        for (char c : randomString.toCharArray()) {
            assertTrue(contains(chars, c));
        }
    }

    private boolean contains(char[] chars, char c) {
        for (char ch : chars) {
            if (ch == c) {
                return true;
            }
        }
        return false;
    }
}