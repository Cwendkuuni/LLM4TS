import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Typical use case: count = 5
        String randomString = RandomStringUtils.random(5);
        assertEquals(5, randomString.length());

        // Edge case: count = 0
        randomString = RandomStringUtils.random(0);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomAscii() {
        // Typical use case: count = 5
        String randomString = RandomStringUtils.randomAscii(5);
        assertEquals(5, randomString.length());

        // Edge case: count = 0
        randomString = RandomStringUtils.randomAscii(0);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomAlphabetic() {
        // Typical use case: count = 5
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, randomString.length());

        // Edge case: count = 0
        randomString = RandomStringUtils.randomAlphabetic(0);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomAlphanumeric() {
        // Typical use case: count = 5
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, randomString.length());

        // Edge case: count = 0
        randomString = RandomStringUtils.randomAlphanumeric(0);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomNumeric() {
        // Typical use case: count = 5
        String randomString = RandomStringUtils.randomNumeric(5);
        assertEquals(5, randomString.length());

        // Edge case: count = 0
        randomString = RandomStringUtils.randomNumeric(0);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomCustomChars() {
        // Typical use case: count = 5, chars = "abc"
        char[] chars = {'a', 'b', 'c'};
        String randomString = RandomStringUtils.random(5, chars);
        assertEquals(5, randomString.length());
        assertTrue(randomString.matches("[abc]+"));

        // Error scenario: count = 5, empty chars array
        chars = new char[]{};
        try {
            randomString = RandomStringUtils.random(5, chars);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The chars array must not be empty", e.getMessage());
        }
    }

    @Test
    public void testRandomStartAndEnd() {
        // Typical use case: count = 5, start = 65, end = 70
        String randomString = RandomStringUtils.random(5, 65, 70, false, false);
        assertEquals(5, randomString.length());
        assertTrue(randomString.matches("[A-F]+"));

        // Edge case: count = 0, start = 65, end = 70
        randomString = RandomStringUtils.random(0, 65, 70, false, false);
        assertEquals("", randomString);

        // Error scenario: count = 5, start = 70, end = 65
        try {
            randomString = RandomStringUtils.random(5, 70, 65, false, false);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Requested random string length 5 is less than 0.", e.getMessage());
        }
    }
}