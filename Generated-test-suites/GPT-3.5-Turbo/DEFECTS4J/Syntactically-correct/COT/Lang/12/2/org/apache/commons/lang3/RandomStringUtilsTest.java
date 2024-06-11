import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {
    
    @Test
    public void testRandom() {
        String random = RandomStringUtils.random(5);
        assertEquals(5, random.length());
    }
    
    @Test
    public void testRandomAscii() {
        String random = RandomStringUtils.randomAscii(5);
        assertEquals(5, random.length());
        for (char c : random.toCharArray()) {
            assertTrue((int) c >= 32 && (int) c <= 126);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        String random = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, random.length());
        for (char c : random.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        String random = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, random.length());
        for (char c : random.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        String random = RandomStringUtils.randomNumeric(5);
        assertEquals(5, random.length());
        for (char c : random.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }
    
    @Test
    public void testRandomWithLettersAndNumbers() {
        String random = RandomStringUtils.random(5, true, true);
        assertEquals(5, random.length());
        for (char c : random.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomWithStartAndEnd() {
        String random = RandomStringUtils.random(5, 65, 90, true, false);
        assertEquals(5, random.length());
        for (char c : random.toCharArray()) {
            assertTrue((int) c >= 65 && (int) c <= 90);
        }
    }
    
    @Test
    public void testRandomWithChars() {
        char[] chars = {'A', 'B', 'C'};
        String random = RandomStringUtils.random(5, chars);
        assertEquals(5, random.length());
        for (char c : random.toCharArray()) {
            assertTrue(c == 'A' || c == 'B' || c == 'C');
        }
    }
    
}