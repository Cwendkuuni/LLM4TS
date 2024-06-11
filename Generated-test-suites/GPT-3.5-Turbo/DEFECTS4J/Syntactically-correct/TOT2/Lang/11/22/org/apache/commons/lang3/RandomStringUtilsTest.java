import org.junit.Test;
import static org.junit.Assert.*;

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
        assertTrue(randomAsciiString.matches("[ -~]+"));
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        assertTrue(randomAlphabeticString.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        assertTrue(randomAlphanumericString.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        assertTrue(randomNumericString.matches("[0-9]+"));
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomWithCustomChars() {
        String randomString = RandomStringUtils.random(10, "abc123");
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[abc123]+"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        RandomStringUtils.random(10, new char[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyStringChars() {
        RandomStringUtils.random(10, "");
    }

    @Test
    public void testRandomWithStartAndEndBounds() {
        String randomString = RandomStringUtils.random(10, 65, 90, true, false);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[A-Z]+"));
    }

}