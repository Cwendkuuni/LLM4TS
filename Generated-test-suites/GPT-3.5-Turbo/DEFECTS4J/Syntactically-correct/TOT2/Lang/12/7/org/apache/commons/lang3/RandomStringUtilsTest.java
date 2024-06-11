import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertEquals(5, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, randomAlphabeticString.length());
        assertTrue(randomAlphabeticString.chars().allMatch(Character::isLetter));
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, randomAlphanumericString.length());
        assertTrue(randomAlphanumericString.chars().allMatch(Character::isLetterOrDigit));
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertEquals(5, randomNumericString.length());
        assertTrue(randomNumericString.chars().allMatch(Character::isDigit));
    }

    @Test
    public void testRandomCustomChars() {
        String randomCustomCharsString = RandomStringUtils.random(5, "abc123");
        assertEquals(5, randomCustomCharsString.length());
        assertTrue(randomCustomCharsString.chars().allMatch(c -> "abc123".contains(String.valueOf((char) c))));
    }

    @Test
    public void testRandomWithCustomRange() {
        String randomCustomRangeString = RandomStringUtils.random(5, 65, 90, true, false);
        assertEquals(5, randomCustomRangeString.length());
        assertTrue(randomCustomRangeString.chars().allMatch(c -> c >= 65 && c <= 90));
    }

    @Test
    public void testRandomWithNullChars() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            RandomStringUtils.random(5, null);
        });
        assertEquals("Requested random string length 5 is less than 0.", exception.getMessage());
    }

}