import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomAsciiString);
        assertEquals(5, randomAsciiString.length());

        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomAlphabeticString);
        assertEquals(5, randomAlphabeticString.length());

        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomAlphanumericString);
        assertEquals(5, randomAlphanumericString.length());

        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomNumericString);
        assertEquals(5, randomNumericString.length());

        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(5, true, true);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomWithStartEndChars() {
        String randomString = RandomStringUtils.random(5, 'A', 'Z', true, false);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 'A' && c <= 'Z');
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        RandomStringUtils.random(5, new char[0]);
    }

    @Test
    public void testRandomWithStringChars() {
        String randomString = RandomStringUtils.random(5, "ABCDEF");
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 'A' && c <= 'F');
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyStringChars() {
        RandomStringUtils.random(5, "");
    }

    @Test
    public void testRandomWithRandomObject() {
        String randomString = RandomStringUtils.random(5, 'A', 'Z', true, false, null, new Random());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 'A' && c <= 'Z');
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1);
    }
}