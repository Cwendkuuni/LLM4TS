import static org.junit.Assert.*;
import org.junit.Test;

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
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomEdgeCases() {
        String randomEdgeCaseString = RandomStringUtils.random(0);
        assertNotNull(randomEdgeCaseString);
        assertEquals("", randomEdgeCaseString);

        randomEdgeCaseString = RandomStringUtils.randomNumeric(0);
        assertNotNull(randomEdgeCaseString);
        assertEquals("", randomEdgeCaseString);

        randomEdgeCaseString = RandomStringUtils.randomAlphabetic(0);
        assertNotNull(randomEdgeCaseString);
        assertEquals("", randomEdgeCaseString);

        randomEdgeCaseString = RandomStringUtils.randomAlphanumeric(0);
        assertNotNull(randomEdgeCaseString);
        assertEquals("", randomEdgeCaseString);

        randomEdgeCaseString = RandomStringUtils.randomAscii(0);
        assertNotNull(randomEdgeCaseString);
        assertEquals("", randomEdgeCaseString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomChars() {
        String randomCharsString = RandomStringUtils.random(10, "abc");
        assertNotNull(randomCharsString);
        assertEquals(10, randomCharsString.length());
        for (char c : randomCharsString.toCharArray()) {
            assertTrue(c == 'a' || c == 'b' || c == 'c');
        }
    }

}