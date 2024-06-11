import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String result = RandomStringUtils.random(5);
        assertEquals(5, result.length());

        result = RandomStringUtils.random(-5);
        assertEquals("", result);
    }

    @Test
    public void testRandomAscii() {
        String result = RandomStringUtils.randomAscii(5);
        assertEquals(5, result.length());

        result = RandomStringUtils.randomAscii(-5);
        assertEquals("", result);
    }

    @Test
    public void testRandomAlphabetic() {
        String result = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, result.length());

        result = RandomStringUtils.randomAlphabetic(-5);
        assertEquals("", result);
    }

    @Test
    public void testRandomAlphanumeric() {
        String result = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, result.length());

        result = RandomStringUtils.randomAlphanumeric(-5);
        assertEquals("", result);
    }

    @Test
    public void testRandomNumeric() {
        String result = RandomStringUtils.randomNumeric(5);
        assertEquals(5, result.length());

        result = RandomStringUtils.randomNumeric(-5);
        assertEquals("", result);
    }

    @Test
    public void testRandomCustom() {
        String result = RandomStringUtils.random(5, true, true);
        assertEquals(5, result.length());

        result = RandomStringUtils.random(-5, true, true);
        assertEquals("", result);
    }

    @Test
    public void testRandomCustomRange() {
        String result = RandomStringUtils.random(5, 65, 90, true, false);
        for (char c : result.toCharArray()) {
            assertTrue(c >= 65 && c <= 90);
        }

        result = RandomStringUtils.random(-5, 65, 90, true, false);
        assertEquals("", result);
    }

    @Test
    public void testRandomCustomChars() {
        String result = RandomStringUtils.random(5, new char[]{'a', 'b', 'c'});
        assertEquals(5, result.length());

        result = RandomStringUtils.random(-5, new char[]{'a', 'b', 'c'});
        assertEquals("", result);
    }

    @Test
    public void testRandomCustomCharsRange() {
        String result = RandomStringUtils.random(5, 1, 3, new char[]{'a', 'b', 'c'});
        assertEquals(5, result.length());

        result = RandomStringUtils.random(-5, 1, 3, new char[]{'a', 'b', 'c'});
        assertEquals("", result);
    }
}