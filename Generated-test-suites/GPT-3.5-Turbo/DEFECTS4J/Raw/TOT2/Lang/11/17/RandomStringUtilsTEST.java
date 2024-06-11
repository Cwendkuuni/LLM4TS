import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {

    // Test for random(int count)
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
    }

    // Test for randomAscii(int count)
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomAsciiString.length());
        // Check if all characters are within ASCII range
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue((int) c >= 32 && (int) c <= 126);
        }
    }

    // Test for randomAlphabetic(int count)
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomAlphabeticString.length());
        // Check if all characters are alphabetic
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    // Test for randomAlphanumeric(int count)
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomAlphanumericString.length());
        // Check if all characters are alphanumeric
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    // Test for randomNumeric(int count)
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomNumericString.length());
        // Check if all characters are numeric
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    // Test for random(int count, boolean letters, boolean numbers)
    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertEquals(10, randomString.length());
        // Check if all characters are either letters or numbers
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    // Test for random(int count, int start, int end, boolean letters, boolean numbers)
    @Test
    public void testRandomWithCustomRange() {
        // Test generating random string with custom range of characters
        String randomString = RandomStringUtils.random(10, 65, 90, true, false);
        assertEquals(10, randomString.length());
        // Check if all characters are uppercase letters
        for (char c : randomString.toCharArray()) {
            assertTrue((int) c >= 65 && (int) c <= 90);
        }
    }

    // Test for random(int count, String chars)
    @Test
    public void testRandomWithCustomCharSet() {
        // Test generating random string with custom char set
        String randomString = RandomStringUtils.random(10, "ABC123");
        assertEquals(10, randomString.length());
        // Check if all characters are either A, B, C, 1, 2, or 3
        for (char c : randomString.toCharArray()) {
            assertTrue(c == 'A' || c == 'B' || c == 'C' || c == '1' || c == '2' || c == '3');
        }
    }

    // Test for random(int count, char... chars)
    @Test
    public void testRandomWithCustomCharSetArray() {
        // Test generating random string with custom char set array
        String randomString = RandomStringUtils.random(10, 'X', 'Y', 'Z');
        assertEquals(10, randomString.length());
        // Check if all characters are either X, Y, or Z
        for (char c : randomString.toCharArray()) {
            assertTrue(c == 'X' || c == 'Y' || c == 'Z');
        }
    }

    // Test for random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random)
    @Test
    public void testRandomWithCustomRangeAndRandomSource() {
        // Test generating random string with custom range of characters and custom Random source
        String randomString = RandomStringUtils.random(10, 97, 122, true, false, null, new Random(123));
        assertEquals(10, randomString.length());
        // Check if all characters are lowercase letters
        for (char c : randomString.toCharArray()) {
            assertTrue((int) c >= 97 && (int) c <= 122);
        }
    }

    // Testing exception handling for illegal arguments
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharArray() {
        RandomStringUtils.random(10, new char[]{});
    }

}