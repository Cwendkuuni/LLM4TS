import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertTrue(Pattern.matches("[\\x20-\\x7E]+", randomAsciiString));
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertTrue(Pattern.matches("[a-zA-Z]+", randomAlphabeticString));
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertTrue(Pattern.matches("[a-zA-Z0-9]+", randomAlphanumericString));
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertTrue(Pattern.matches("[0-9]+", randomNumericString));
    }

    @Test
    public void testRandomWithNumbersAndLetters() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertTrue(Pattern.matches("[a-zA-Z0-9]+", randomString));
    }

    @Test
    public void testRandomWithCustomCharacterSet() {
        String customCharSet = "ABC123";
        String randomString = RandomStringUtils.random(10, customCharSet.toCharArray());
        assertTrue(Pattern.matches("[ABC123]+", randomString));
    }

    @Test
    public void testRandomWithStartAndEnd() {
        String randomString = RandomStringUtils.random(10, 97, 122, true, false);
        assertTrue(Pattern.matches("[a-z]+", randomString));
    }

    @Test
    public void testRandomWithInvalidCount() {
        try {
            RandomStringUtils.random(-1);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRandomWithEmptyCharSet() {
        try {
            RandomStringUtils.random(10, "");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}