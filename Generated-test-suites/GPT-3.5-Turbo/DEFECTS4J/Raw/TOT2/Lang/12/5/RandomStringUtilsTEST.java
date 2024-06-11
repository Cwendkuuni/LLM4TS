import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomCustom() {
        String randomString = RandomStringUtils.random(5, 65, 90, true, false, null);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 65 && c <= 90);
        }
    }

    @Test
    public void testRandomFromArray() {
        String randomString = RandomStringUtils.random(5, new char[]{'A', 'B', 'C', 'D', 'E'});
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c == 'A' || c == 'B' || c == 'C' || c == 'D' || c == 'E');
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRandomLength() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRandomCustomChars() {
        RandomStringUtils.random(5, "");
    }

    @Test
    public void testRandomCustomCharsNotNull() {
        String randomString = RandomStringUtils.random(5, "abc123");
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testUnicodeSurrogateCases() {
        // Add test cases for edge cases regarding Unicode surrogates
    }
}