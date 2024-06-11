import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom_CountOnly() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }
    
    @Test
    public void testRandomAscii_CountOnly() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertTrue(isAscii(randomString));
    }
    
    @Test
    public void testRandomAlphabetic_CountOnly() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertTrue(isAlphabetic(randomString));
    }
    
    @Test
    public void testRandomAlphanumeric_CountOnly() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertTrue(isAlphanumeric(randomString));
    }
    
    @Test
    public void testRandomNumeric_CountOnly() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertTrue(isNumeric(randomString));
    }
    
    @Test
    public void testRandom_WithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertTrue(isAlphanumeric(randomString));
    }
    
    @Test
    public void testRandom_WithLettersOnly() {
        String randomString = RandomStringUtils.random(10, true, false);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertTrue(isAlphabetic(randomString));
    }
    
    @Test
    public void testRandom_WithNumbersOnly() {
        String randomString = RandomStringUtils.random(10, false, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertTrue(isNumeric(randomString));
    }
    
    @Test
    public void testRandom_WithCustomChars() {
        String randomString = RandomStringUtils.random(10, "abcde");
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertTrue(isInSet(randomString, "abcde"));
    }

    @Test
    public void testRandom_WithStartAndEnd() {
        String randomString = RandomStringUtils.random(10, 65, 90, true, false);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        assertTrue(isInRange(randomString, 65, 90));
    }

    private boolean isAscii(String input) {
        for (char c : input.toCharArray()) {
            if (c < 32 || c > 126) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isAlphabetic(String input) {
        for (char c : input.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isAlphanumeric(String input) {
        for (char c : input.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isNumeric(String input) {
        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isInSet(String input, String charset) {
        for (char c : input.toCharArray()) {
            if (charset.indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isInRange(String input, int start, int end) {
        for (char c : input.toCharArray()) {
            if (c < start || c > end) {
                return false;
            }
        }
        return true;
    }
}