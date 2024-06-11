import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals("The length of the random string should be 10", 10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertEquals("The length of the random ASCII string should be 10", 10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue("Each character in the random ASCII string should have ASCII value between 32 and 127",
                    c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertEquals("The length of the random alphabetic string should be 10", 10, randomAlphabeticString.length());
        assertTrue("The random alphabetic string should only contain alphabetic characters",
                randomAlphabeticString.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals("The length of the random alphanumeric string should be 10", 10, randomAlphanumericString.length());
        assertTrue("The random alphanumeric string should only contain alphanumeric characters",
                randomAlphanumericString.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertEquals("The length of the random numeric string should be 10", 10, randomNumericString.length());
        assertTrue("The random numeric string should only contain numeric characters",
                randomNumericString.matches("[0-9]+"));
    }

    // Additional test cases for edge cases and error scenarios can be added here

}