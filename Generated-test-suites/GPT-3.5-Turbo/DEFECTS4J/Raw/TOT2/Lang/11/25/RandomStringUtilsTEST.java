import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertEquals(5, randomString.length());
        assertEquals(String.class, randomString.getClass());
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
    public void testRandomCustom() {
        String randomString = RandomStringUtils.random(5, 65, 90, true, false);
        assertEquals(5, randomString.length());
        assertTrue(randomString.chars().allMatch(c -> c >= 65 && c <= 90));
    }

    @Test
    public void testRandomCustomWithChars() {
        String randomString = RandomStringUtils.random(5, new char[]{'a', 'b', 'c', 'd', 'e'});
        assertEquals(5, randomString.length());
        assertTrue(randomString.chars().allMatch(c -> c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharsArray() {
        RandomStringUtils.random(5, new char[]{});
    }

    @Test
    public void testRandomWithChars() {
        String randomString = RandomStringUtils.random(5, "abcde");
        assertEquals(5, randomString.length());
        assertTrue(randomString.chars().allMatch(c -> c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e'));
    }

}