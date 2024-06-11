import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Test typical use case
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        // Test edge case
        randomString = RandomStringUtils.random(0);
        assertNotNull(randomString);
        assertEquals(0, randomString.length());

        // Test error scenario
        try {
            randomString = RandomStringUtils.random(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testRandomAscii() {
        // Test typical use case
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }

        // Test edge case
        randomAsciiString = RandomStringUtils.randomAscii(0);
        assertNotNull(randomAsciiString);
        assertEquals(0, randomAsciiString.length());

        // Test error scenario
        try {
            randomAsciiString = RandomStringUtils.randomAscii(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testRandomAlphabetic() {
        // Test typical use case
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }

        // Test edge case
        randomAlphabeticString = RandomStringUtils.randomAlphabetic(0);
        assertNotNull(randomAlphabeticString);
        assertEquals(0, randomAlphabeticString.length());

        // Test error scenario
        try {
            randomAlphabeticString = RandomStringUtils.randomAlphabetic(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        // Test typical use case
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }

        // Test edge case
        randomAlphanumericString = RandomStringUtils.randomAlphanumeric(0);
        assertNotNull(randomAlphanumericString);
        assertEquals(0, randomAlphanumericString.length());

        // Test error scenario
        try {
            randomAlphanumericString = RandomStringUtils.randomAlphanumeric(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testRandomNumeric() {
        // Test typical use case
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }

        // Test edge case
        randomNumericString = RandomStringUtils.randomNumeric(0);
        assertNotNull(randomNumericString);
        assertEquals(0, randomNumericString.length());

        // Test error scenario
        try {
            randomNumericString = RandomStringUtils.randomNumeric(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testRandomWithChars() {
        // Test typical use case
        char[] chars = {'A', 'B', 'C'};
        String randomString = RandomStringUtils.random(10, chars);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c == 'A' || c == 'B' || c == 'C');
        }

        // Test edge case
        randomString = RandomStringUtils.random(0, chars);
        assertNotNull(randomString);
        assertEquals(0, randomString.length());

        // Test error scenario
        try {
            randomString = RandomStringUtils.random(-1, chars);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Passed
        }

        // Test error scenario
        try {
            randomString = RandomStringUtils.random(10, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

}