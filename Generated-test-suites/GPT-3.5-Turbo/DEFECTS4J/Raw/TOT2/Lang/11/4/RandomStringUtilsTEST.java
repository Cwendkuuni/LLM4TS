import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertTrue(randomAsciiString.matches("\\p{Print}+"));
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertTrue(randomAlphabeticString.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertTrue(randomAlphanumericString.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertTrue(randomNumericString.matches("[0-9]+"));
    }

    @Test
    public void testRandomWithNullCharsArray() {
        String randomString = RandomStringUtils.random(10, 0, 0, true, true, null, new Random());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        RandomStringUtils.random(10, new char[] {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomStringWithCustomChars() {
        String customChars = "abc123";
        String randomString = RandomStringUtils.random(10, customChars.toCharArray());
        assertTrue(randomString.matches("[abc123]+"));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithInvalidCharsRange() {
        RandomStringUtils.random(10, 100, 200, true, true, new char[] {'a', 'b', 'c'});
    }

}