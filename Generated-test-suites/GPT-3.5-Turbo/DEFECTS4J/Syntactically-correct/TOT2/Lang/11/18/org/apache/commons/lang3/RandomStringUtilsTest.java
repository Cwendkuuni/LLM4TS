import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {
    
    @Before
    public void setUp() {
        // setup code
    }
    
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
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        assertTrue(randomAlphabeticString.matches("[a-zA-Z]+"));
    }
    
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        assertTrue(randomAlphanumericString.matches("[a-zA-Z0-9]+"));
    }
    
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        assertTrue(randomNumericString.matches("[0-9]+"));
    }
    
    @Test
    public void testRandomWithSpecialCharacters() {
        String specialCharacters = "!@#$%^&*()";
        String randomStringWithSpecialCharacters = RandomStringUtils.random(10, specialCharacters);
        assertNotNull(randomStringWithSpecialCharacters);
        assertEquals(10, randomStringWithSpecialCharacters.length());
        for (char c : randomStringWithSpecialCharacters.toCharArray()) {
            assertTrue(specialCharacters.indexOf(c) != -1);
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeLength() {
        RandomStringUtils.random(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterArray() {
        RandomStringUtils.random(10, new char[]{});
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyString() {
        RandomStringUtils.random(10, "");
    }
}