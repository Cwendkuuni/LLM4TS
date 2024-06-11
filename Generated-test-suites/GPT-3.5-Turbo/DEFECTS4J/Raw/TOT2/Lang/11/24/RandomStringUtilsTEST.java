import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomAsciiString.length());
        for(char c : randomAsciiString.toCharArray()) {
            assertTrue((int)c >= 32 && (int)c <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomAlphabeticString.length());
        for(char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomAlphanumericString.length());
        for(char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomNumericString.length());
        for(char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithCustomCharacterRange() {
        String customCharRangeString = RandomStringUtils.random(10, 65, 90, false, true);
        assertEquals(10, customCharRangeString.length());
        for(char c : customCharRangeString.toCharArray()) {
            assertTrue((int)c >= 65 && (int)c <= 90);
        }
    }

    @Test
    public void testRandomWithCustomCharacterArray() {
        String customCharArrayString = RandomStringUtils.random(10, new char[]{'a', 'b', 'c', 'd'});
        assertEquals(10, customCharArrayString.length());
        for(char c : customCharArrayString.toCharArray()) {
            assertTrue(c == 'a' || c == 'b' || c == 'c' || c == 'd');
        }
    }

    @Test
    public void testRandomWithNullCharSet() {
        String randomString = RandomStringUtils.random(10, null);
        assertEquals(10, randomString.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharSet() {
        RandomStringUtils.random(10, "");
    }

}