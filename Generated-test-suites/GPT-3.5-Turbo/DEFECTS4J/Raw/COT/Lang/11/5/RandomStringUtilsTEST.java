import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    // random(int count)
    @Test
    public void testRandomCount() {
        String result = RandomStringUtils.random(5);
        assertNotNull(result);
        assertEquals(5, result.length());
    }
    
    // randomAscii(int count)
    @Test
    public void testRandomAsciiCount() {
        String result = RandomStringUtils.randomAscii(5);
        assertNotNull(result);
        assertEquals(5, result.length());
        for(char c : result.toCharArray()) {
            assertTrue((int) c >= 32 && (int) c <= 127);
        }
    }
    
    // randomAlphabetic(int count)
    @Test
    public void testRandomAlphabeticCount() {
        String result = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(result);
        assertEquals(5, result.length());
        for(char c : result.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }
    
    // randomAlphanumeric(int count)
    @Test
    public void testRandomAlphanumericCount() {
        String result = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(result);
        assertEquals(5, result.length());
        for(char c : result.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    // randomNumeric(int count)
    @Test
    public void testRandomNumericCount() {
        String result = RandomStringUtils.randomNumeric(5);
        assertNotNull(result);
        assertEquals(5, result.length());
        for(char c : result.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }
    
    // random(int count, boolean letters, boolean numbers)
    @Test
    public void testRandomCountLettersNumbers() {
        String result = RandomStringUtils.random(5, true, true);
        assertNotNull(result);
        assertEquals(5, result.length());
        for(char c : result.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    // random(int count, int start, int end, boolean letters, boolean numbers, char... chars)
    @Test
    public void testRandomCountStartEndLettersNumbersChars() {
        String result = RandomStringUtils.random(5, 65, 90, true, false, 'A', 'B', 'C');
        assertNotNull(result);
        assertEquals(5, result.length());
        for(char c : result.toCharArray()) {
            assertTrue(c >= 65 && c <= 90);
        }
    }
    
    // random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random)
    @Test
    public void testRandomCountStartEndLettersNumbersCharsRandom() {
        String result = RandomStringUtils.random(5, 65, 90, true, false, new char[]{'A', 'B', 'C'}, new Random());
        assertNotNull(result);
        assertEquals(5, result.length());
        for(char c : result.toCharArray()) {
            assertTrue(c >= 65 && c <= 90);
        }
    }
    
    // random(int count, String chars)
    @Test
    public void testRandomCountCharsString() {
        String result = RandomStringUtils.random(5, "ABC");
        assertNotNull(result);
        assertEquals(5, result.length());
        for(char c : result.toCharArray()) {
            assertTrue(c == 'A' || c == 'B' || c == 'C');
        }
    }
    
    // random(int count, char... chars)
    @Test
    public void testRandomCountChars() {
        String result = RandomStringUtils.random(5, 'A', 'B', 'C');
        assertNotNull(result);
        assertEquals(5, result.length());
        for(char c : result.toCharArray()) {
            assertTrue(c == 'A' || c == 'B' || c == 'C');
        }
    }

}