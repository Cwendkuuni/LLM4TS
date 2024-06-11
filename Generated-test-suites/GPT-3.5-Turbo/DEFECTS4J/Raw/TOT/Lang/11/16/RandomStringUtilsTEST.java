import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        assertEquals(5, RandomStringUtils.random(5).length());
    }

    @Test
    public void testRandomAscii() {
        int length = RandomStringUtils.randomAscii(10).length();
        assertTrue(length >= 10 && length <= 10);
    }

    @Test
    public void testRandomAlphabetic() {
        int length = RandomStringUtils.randomAlphabetic(15).length();
        assertTrue(length >= 15 && length <= 15);
    }

    @Test
    public void testRandomAlphanumeric() {
        int length = RandomStringUtils.randomAlphanumeric(20).length();
        assertTrue(length >= 20 && length <= 20);
    }

    @Test
    public void testRandomNumeric() {
        int length = RandomStringUtils.randomNumeric(8).length();
        assertTrue(length >= 8 && length <= 8);
    }

    @Test
    public void testRandomCustom() {
        String customChars = "abc123";
        String result = RandomStringUtils.random(10, customChars);
        assertTrue(result.length() == 10);
        for(char c : result.toCharArray()) {
            assertTrue(customChars.contains(String.valueOf(c)));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyChars() {
        RandomStringUtils.random(5, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharsArray() {
        char[] chars = {};
        RandomStringUtils.random(5, chars);
    }
}