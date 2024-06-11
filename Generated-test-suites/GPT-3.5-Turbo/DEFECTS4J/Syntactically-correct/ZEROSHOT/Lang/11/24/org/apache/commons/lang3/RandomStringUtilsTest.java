import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        
        assertNotNull(randomString);
        assertEquals(randomString.length(), 10);
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        
        assertNotNull(randomString);
        assertEquals(randomString.length(), 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        
        assertNotNull(randomString);
        assertEquals(randomString.length(), 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        
        assertNotNull(randomString);
        assertEquals(randomString.length(), 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        
        assertNotNull(randomString);
        assertEquals(randomString.length(), 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        
        assertNotNull(randomString);
        assertEquals(randomString.length(), 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomWithLetters() {
        String randomString = RandomStringUtils.random(10, true, false);
        
        assertNotNull(randomString);
        assertEquals(randomString.length(), 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }
    
    @Test
    public void testRandomWithNumbers() {
        String randomString = RandomStringUtils.random(10, false, true);
        
        assertNotNull(randomString);
        assertEquals(randomString.length(), 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithCharSet() {
        String randomString = RandomStringUtils.random(10, 'A', 'Z', false, false);
        
        assertNotNull(randomString);
        assertEquals(randomString.length(), 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 'A' && c <= 'Z');
        }
    }
    
    @Test
    public void testRandomWithEmptyCharSet() {
        try {
            String randomString = RandomStringUtils.random(10, new char[]{});
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "The chars array must not be empty");
        }
    }
    
    @Test
    public void testRandomWithCustomRandom() {
        String randomString = RandomStringUtils.random(10, 0, 9, false, true, null, new Random(123));
        
        assertNotNull(randomString);
        assertEquals(randomString.length(), 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }
    
    @Test
    public void testRandomWithInvalidLength() {
        try {
            String randomString = RandomStringUtils.random(-1);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Requested random string length -1 is less than 0.");
        }
    }

    @Test
    public void testRandomWithStringCharSet() {
        String randomString = RandomStringUtils.random(10, "ABCDEF");
        
        assertNotNull(randomString);
        assertEquals(randomString.length(), 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 'A' && c <= 'F');
        }
    }
    
    @Test
    public void testRandomWithEmptyStringCharSet() {
        try {
            String randomString = RandomStringUtils.random(10, "");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "The chars array must not be empty");
        }
    }
}