import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {
    
    @Test
    public void testRandom() {
        String result = RandomStringUtils.random(5);
        assertEquals(5, result.length());
    }
    
    @Test
    public void testRandomAscii() {
        String result = RandomStringUtils.randomAscii(5);
        assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        String result = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        String result = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        String result = RandomStringUtils.randomNumeric(5);
        assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }
    
    @Test
    public void testRandomWithCharacters() {
        char[] chars = "abcdefg".toCharArray();
        String result = RandomStringUtils.random(5, chars);
        assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(containsChar(chars, c));
        }
    }
    
    private boolean containsChar(char[] chars, char c) {
        for (char ch : chars) {
            if (ch == c) {
                return true;
            }
        }
        return false;
    }
    
}