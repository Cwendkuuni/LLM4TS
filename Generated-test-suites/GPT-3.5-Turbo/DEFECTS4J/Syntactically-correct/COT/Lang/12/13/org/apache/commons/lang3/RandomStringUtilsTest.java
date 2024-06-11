
import org.junit.Test;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        // Verify that the length of the random string is 10
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        // Verify that the length of the random ASCII string is 10
        assertEquals(10, randomAsciiString.length());
        // Verify that all characters in the random ASCII string are within the ASCII range
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        // Verify that the length of the random alphabetic string is 10
        assertEquals(10, randomAlphabeticString.length());
        // Verify that all characters in the random alphabetic string are alphabetic
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        // Verify that the length of the random alphanumeric string is 10
        assertEquals(10, randomAlphanumericString.length());
        // Verify that all characters in the random alphanumeric string are either alphabetic or numeric
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        // Verify that the length of the random numeric string is 10
        assertEquals(10, randomNumericString.length());
        // Verify that all characters in the random numeric string are numeric
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithCustomChars() {
        String randomString = RandomStringUtils.random(10, "ABC123");
        // Verify that the length of the random string is 10
        assertEquals(10, randomString.length());
        // Verify that all characters in the random string are either "A", "B", "C", "1", "2" or "3"
        for (char c : randomString.toCharArray()) {
            assertTrue(c == 'A' || c == 'B' || c == 'C' || c == '1' || c == '2' || c == '3');
        }
    }

    @Test
    public void testRandomWithInvalidCount() {
        try {
            String randomString = RandomStringUtils.random(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testRandomWithEmptyChars() {
        try {
            String randomString = RandomStringUtils.random(10, "");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

}
