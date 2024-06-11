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
        for(char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, randomAlphabeticString.length());
        for(char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, randomAlphanumericString.length());
        for(char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertEquals(5, randomNumericString.length());
        for(char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithCustomChars() {
        String randomString = RandomStringUtils.random(5, new char[]{'a', 'b', 'c', '1', '2'});
        assertEquals(5, randomString.length());
        for(char c : randomString.toCharArray()) {
            assertTrue(c == 'a' || c == 'b' || c == 'c' || c == '1' || c == '2');
        }
    }

    @Test
    public void testRandomWithEmptyCustomChars() {
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.random(5, new char[]{}));
    }
  
    @Test
    public void testRandomWithNegativeCount() {
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.random(-5));
    }

}