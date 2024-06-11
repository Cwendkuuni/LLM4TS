
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    // random(int count)
    @Test
    public void testRandomCount() {
        String randomString = RandomStringUtils.random(10);
        assertEquals("The length of the random string should be 10", 10, randomString.length());
    }
    
    // randomAscii(int count)
    @Test
    public void testRandomAsciiCount() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertEquals("The length of the random ASCII string should be 10", 10, randomAsciiString.length());
    }
    
    // randomAlphabetic(int count)
    @Test
    public void testRandomAlphabeticCount() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertEquals("The length of the random alphabetic string should be 10", 10, randomAlphabeticString.length());
        assertTrue("The random alphabetic string should contain only alphabetic characters", randomAlphabeticString.matches("[a-zA-Z]+"));
    }
    
    // randomAlphanumeric(int count)
    @Test
    public void testRandomAlphanumericCount() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals("The length of the random alphanumeric string should be 10", 10, randomAlphanumericString.length());
        assertTrue("The random alphanumeric string should contain only alphanumeric characters", randomAlphanumericString.matches("[a-zA-Z0-9]+"));
    }
    
    // randomNumeric(int count)
    @Test
    public void testRandomNumericCount() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertEquals("The length of the random numeric string should be 10", 10, randomNumericString.length());
        assertTrue("The random numeric string should contain only numeric characters", randomNumericString.matches("[0-9]+"));
    }
    
    // random(int count, boolean letters, boolean numbers)
    @Test
    public void testRandomCountLettersNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertEquals("The length of the random string should be 10", 10, randomString.length());
        assertTrue("The random string should contain only letters and numbers", randomString.matches("[a-zA-Z0-9]+"));
    }
    
    // random(int count, int start, int end, boolean letters, boolean numbers)
    @Test
    public void testRandomStartEndLettersNumbers() {
        String randomString = RandomStringUtils.random(10, 65, 90, true, false);
        assertEquals("The length of the random string should be 10", 10, randomString.length());
        assertTrue("The random string should contain only uppercase letters", randomString.matches("[A-Z]+"));
    }
    
    // random(int count, int start, int end, boolean letters, boolean numbers, char[] chars)
    @Test
    public void testRandomStartEndLettersNumbersChars() {
        String randomString = RandomStringUtils.random(10, 65, 90, true, false, new char[] {'A', 'B', 'C'});
        assertEquals("The length of the random string should be 10", 10, randomString.length());
        assertTrue("The random string should contain only the allowed characters", randomString.matches("[ABC]+"));
    }
    
    // random(int count, String chars)
    @Test
    public void testRandomCountChars() {
        String randomString = RandomStringUtils.random(10, "ABC");
        assertEquals("The length of the random string should be 10", 10, randomString.length());
        assertTrue("The random string should contain only the allowed characters", randomString.matches("[ABC]+"));
    }
    
    // random(int count, char... chars)
    @Test
    public void testRandomCountChar() {
        String randomString = RandomStringUtils.random(10, 'A', 'B', 'C');
        assertEquals("The length of the random string should be 10", 10, randomString.length());
        assertTrue("The random string should contain only the allowed characters", randomString.matches("[ABC]+"));
    }

}

