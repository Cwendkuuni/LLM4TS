import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {
    
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }
    
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        
        for(char c : randomAsciiString.toCharArray()) {
            assertTrue((int)c >= 32 && (int)c <= 127);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        
        for(char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        
        for(char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        
        for(char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }
    
    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        
        for(char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomWithStartAndEnd() {
        String randomString = RandomStringUtils.random(10, 65, 90, true, false);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        
        for(char c : randomString.toCharArray()) {
            assertTrue((int)c >= 65 && (int)c <= 90);
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        String randomString = RandomStringUtils.random(-10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        String randomString = RandomStringUtils.random(10, new char[0]);
    }
    
    @Test
    public void testRandomWithCharsArray() {
        char[] chars = "abcABC123".toCharArray();
        String randomString = RandomStringUtils.random(10, chars);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        
        for(char c : randomString.toCharArray()) {
            assertTrue(new String(chars).contains(String.valueOf(c)));
        }
    }
    
    @Test
    public void testRandomWithEmptyString() {
        String randomString = RandomStringUtils.random(10, "");
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }
    
    @Test
    public void testRandomWithNullString() {
        String randomString = RandomStringUtils.random(10, null);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }
    
}