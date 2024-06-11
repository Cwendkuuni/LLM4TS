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
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomEdgeCases() {
        String randomString = RandomStringUtils.random(-1);
        assertEquals("", randomString);

        randomString = RandomStringUtils.random(5, 97, 97, false, false);
        assertEquals("", randomString);

        randomString = RandomStringUtils.random(5, null);
        assertEquals(5, randomString.length());

        randomString = RandomStringUtils.random(5, "");
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomIllegalArguments() {
        try {
            RandomStringUtils.random(-1, 32, 127, false, false);
            fail("IllegalArgumentException expected for negative count");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            RandomStringUtils.random(5, new char[]{});
            fail("IllegalArgumentException expected for empty char array");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}