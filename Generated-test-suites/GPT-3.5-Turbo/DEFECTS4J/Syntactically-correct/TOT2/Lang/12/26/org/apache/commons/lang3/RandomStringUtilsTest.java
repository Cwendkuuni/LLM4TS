import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Typical use case
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        // Typical use case
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomAsciiString.length());
    }

    @Test
    public void testRandomAlphabetic() {
        // Typical use case
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomAlphabeticString.length());
    }

    @Test
    public void testRandomAlphanumeric() {
        // Typical use case
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomAlphanumericString.length());
    }

    @Test
    public void testRandomNumeric() {
        // Typical use case
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomNumericString.length());
    }

    @Test
    public void testRandomWithCustomCharSet() {
        // Edge case - testing with custom character set
        String customChars = "ABC123";
        String randomCustomString = RandomStringUtils.random(10, customChars.toCharArray());
        assertTrue(randomCustomString.matches("[ABC123]{10}"));
    }

    @Test
    public void testRandomWithEmptyCustomCharSet() {
        // Error scenario - testing with empty character set
        try {
            RandomStringUtils.random(10, new char[]{});
            fail("Expected IllegalArgumentException for empty custom character set");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testRandomWithNegativeCount() {
        // Error scenario - testing with negative count
        try {
            RandomStringUtils.random(-10);
            fail("Expected IllegalArgumentException for negative count");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testRandomWithInvalidChars() {
        // Error scenario - testing with invalid char range
        try {
            RandomStringUtils.random(10, 100, 200, false, false);
            fail("Expected ArrayIndexOutOfBoundsException for invalid char range");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

}