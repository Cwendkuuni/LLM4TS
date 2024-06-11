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
        String randomString = RandomStringUtils.randomAscii(5);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, randomString.length());
        assertTrue(randomString.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, randomString.length());
        assertTrue(randomString.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertEquals(5, randomString.length());
        assertTrue(randomString.matches("[0-9]+"));
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(5, true, true);
        assertEquals(5, randomString.length());
        assertTrue(randomString.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomWithSpecificChars() {
        String randomString = RandomStringUtils.random(5, new char[]{'a', 'b', 'c'});
        assertEquals(5, randomString.length());
        assertTrue(randomString.matches("[abc]+"));
    }

    @Test
    public void testRandomWithInvalidCount() {
        try {
            RandomStringUtils.random(-5, false, false);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
    }

}