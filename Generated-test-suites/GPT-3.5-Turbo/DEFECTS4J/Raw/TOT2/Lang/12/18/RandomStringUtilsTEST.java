import static org.junit.Assert.*;
import org.junit.Test;
import java.util.regex.Pattern;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String asciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(asciiString);
        assertTrue(Pattern.matches("[\\x20-\\x7E]+", asciiString));
        assertEquals(10, asciiString.length());
    }

    @Test
    public void testRandomAlphabetic() {
        String alphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(alphabeticString);
        assertTrue(alphabeticString.chars().allMatch(Character::isLetter));
        assertEquals(10, alphabeticString.length());
    }

    @Test
    public void testRandomAlphanumeric() {
        String alphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(alphanumericString);
        assertTrue(Pattern.matches("[a-zA-Z0-9]+", alphanumericString));
        assertEquals(10, alphanumericString.length());
    }

    @Test
    public void testRandomNumeric() {
        String numericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(numericString);
        assertTrue(numericString.chars().allMatch(Character::isDigit));
        assertEquals(10, numericString.length());
    }

    @Test
    public void testRandomWithCustomCharacterSet() {
        String customCharSet = "abc123";
        String customString = RandomStringUtils.random(10, customCharSet.toCharArray());
        assertNotNull(customString);
        assertTrue(customString.chars().allMatch(ch -> customCharSet.indexOf(ch) != -1));
        assertEquals(10, customString.length());
    }

    @Test
    public void testRandomExceptionHandling() {
        try {
            String emptyCharSet = "";
            RandomStringUtils.random(10, emptyCharSet.toCharArray());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
}