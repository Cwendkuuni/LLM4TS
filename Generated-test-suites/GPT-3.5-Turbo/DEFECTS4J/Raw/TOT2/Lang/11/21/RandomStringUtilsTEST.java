import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        assertTrue(randomAsciiString.matches("\\p{ASCII}+"));
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        assertTrue(randomAlphabeticString.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        assertTrue(randomAlphanumericString.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        assertTrue(randomNumericString.matches("[0-9]+"));
    }

    @Test
    public void testRandomWithCustomChars() {
        String customChars = "abcdef123456";
        String randomString = RandomStringUtils.random(10, customChars.toCharArray());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[a-f1-6]+"));
    }

    @Test
    public void testRandomWithEmptyCustomChars() {
        try {
            RandomStringUtils.random(10, "");
            fail("Expected IllegalArgumentException for empty custom chars");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testRandomWithNegativeLength() {
        try {
            RandomStringUtils.random(-5);
            fail("Expected IllegalArgumentException for negative length");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testRandomWithNullCustomChars() {
        String randomString = RandomStringUtils.random(10, null);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }
}