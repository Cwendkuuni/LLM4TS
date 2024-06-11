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
        assertTrue(randomAsciiString.matches("[ -~]+"));
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertTrue(randomAlphabeticString.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertTrue(randomAlphanumericString.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertTrue(randomNumericString.matches("[0-9]+"));
    }

    @Test
    public void testRandomLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertTrue(randomString.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomWithCustomChars() {
        String randomString = RandomStringUtils.random(10, new char[]{'a', 'b', 'c'});
        assertTrue(randomString.matches("[abc]+"));
    }

    @Test
    public void testRandomWithEmptyCustomChars() {
        try {
            RandomStringUtils.random(10, new char[]{});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testRandomNegativeCount() {
        try {
            RandomStringUtils.random(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testRandomNegativeCountWithCustomChars() {
        try {
            RandomStringUtils.random(-1, new char[]{'a', 'b', 'c'});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

}