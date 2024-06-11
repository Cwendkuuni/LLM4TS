import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {
    
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
    }
    
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomAsciiString.length());
        for(char c : randomAsciiString.toCharArray()) {
            assertTrue((int)c >= 32 && (int)c <= 126);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomAlphabeticString.length());
        for(char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomAlphanumericString.length());
        for(char c : randomAlphanumericString.toCharArray()){
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomNumericString.length());
        for(char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }
    
    @Test
    public void testRandomRange() {
        String randomRangeString = RandomStringUtils.random(10, 65, 90, true, false);
        assertEquals(10, randomRangeString.length());
        for(char c : randomRangeString.toCharArray()) {
            assertTrue((int)c >= 65 && (int)c <= 90); // ASCII range for uppercase letters
        }
    }
    
    @Test
    public void testRandomWithChars() {
        String randomWithChars = RandomStringUtils.random(10, "abc123");
        assertEquals(10, randomWithChars.length());
        for(char c : randomWithChars.toCharArray()) {
            assertTrue(c == 'a' || c == 'b' || c == 'c' || c == '1' || c == '2' || c == '3');
        }
    }
    
    @Test
    public void testRandomWithCustomRandom() {
        Random customRandom = new Random(987654321); // fixed seed for predictability
        String randomString = RandomStringUtils.random(10, 48, 57, false, true, null, customRandom);
        assertEquals(10, randomString.length());
        for(char c : randomString.toCharArray()) {
            assertTrue((int)c >= 48 && (int)c <= 57); // ASCII range for numeric characters
        }
    }
}