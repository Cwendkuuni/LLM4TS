import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length should match", count, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        int count = 5;
        String randomString = RandomStringUtils.randomAscii(count);
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be ASCII", (c >= 32 && c <= 126));
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be alphabetic", Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        int count = 15;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be alphanumeric", Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumericString() {
        int count = 8;
        String randomString = RandomStringUtils.randomNumeric(count);
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be numeric", Character.isDigit(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomStringWithRange() {
        int count = 10;
        int start = 65;
        int end = 90;
        String randomString = RandomStringUtils.random(count, start, end, false, false);
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be in range", (c >= start && c <= end));
        }
    }

    @Test
    public void testRandomStringWithCustomChars() {
        int count = 5;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String randomString = RandomStringUtils.random(count, chars.toCharArray());
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            assertTrue("Character should be in set", chars.indexOf(c) != -1);
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringEmptySet() {
        RandomStringUtils.random(1, new char[0]);
    }

    @Test
    public void testRandomStringWithSurrogateCharacters() {
        int count = 2;
        String randomString = RandomStringUtils.random(count, 55296, 57343, false, false);
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            if (i == 0) {
                assertTrue("First character should be high surrogate", (c >= 55296 && c <= 56191));
            } else {
                assertTrue("Second character should be low surrogate", (c >= 56320 && c <= 57343));
            }
        }
    }
}