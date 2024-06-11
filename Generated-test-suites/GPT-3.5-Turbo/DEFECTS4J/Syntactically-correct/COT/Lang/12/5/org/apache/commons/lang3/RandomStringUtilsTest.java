import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.RandomStringUtils;


public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Test basic functionality
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());

        // Test edge cases
        randomString = RandomStringUtils.random(0);
        assertEquals("", randomString);

        randomString = RandomStringUtils.random(-10);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomAscii() {
        // Test basic functionality
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());

        // Test edge cases
        randomString = RandomStringUtils.randomAscii(0);
        assertEquals("", randomString);

        randomString = RandomStringUtils.randomAscii(-10);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomAlphabetic() {
        // Test basic functionality
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomString.length());

        // Test edge cases
        randomString = RandomStringUtils.randomAlphabetic(0);
        assertEquals("", randomString);

        randomString = RandomStringUtils.randomAlphabetic(-10);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomAlphanumeric() {
        // Test basic functionality
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomString.length());

        // Test edge cases
        randomString = RandomStringUtils.randomAlphanumeric(0);
        assertEquals("", randomString);

        randomString = RandomStringUtils.randomAlphanumeric(-10);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomNumeric() {
        // Test basic functionality
        String randomString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomString.length());

        // Test edge cases
        randomString = RandomStringUtils.randomNumeric(0);
        assertEquals("", randomString);

        randomString = RandomStringUtils.randomNumeric(-10);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomWithParameters() {
        // Test basic functionality
        String randomString = RandomStringUtils.random(10, true, true);
        assertEquals(10, randomString.length());

        // Test edge cases
        randomString = RandomStringUtils.random(0, true, true);
        assertEquals("", randomString);

        randomString = RandomStringUtils.random(-10, true, true);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomWithChars() {
        // Test basic functionality
        String randomString = RandomStringUtils.random(10, "abcdefghijklmnopqrstuvwxyz");
        assertEquals(10, randomString.length());

        // Test edge cases
        randomString = RandomStringUtils.random(0, "abcdefghijklmnopqrstuvwxyz");
        assertEquals("", randomString);

        randomString = RandomStringUtils.random(-10, "abcdefghijklmnopqrstuvwxyz");
        assertEquals("", randomString);
    }
}