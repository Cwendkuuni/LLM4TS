import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for(char c : randomString.toCharArray()){
            assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for(char c : randomString.toCharArray()){
            assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for(char c : randomString.toCharArray()){
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for(char c : randomString.toCharArray()){
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for(char c : randomString.toCharArray()){
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithChars() {
        String randomString = RandomStringUtils.random(5, new char[]{'a', 'b', 'c', 'd', 'e'});
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for(char c : randomString.toCharArray()){
            assertTrue(c >= 'a' && c <= 'e');
        }
    }

    @Test
    public void testRandomWithEmptyChars() {
        try {
            RandomStringUtils.random(5, new char[]{});
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // As expected
        }
    }

    @Test
    public void testRandomWithNegativeCount() {
        try {
            RandomStringUtils.random(-5);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // As expected
        }
    }
}