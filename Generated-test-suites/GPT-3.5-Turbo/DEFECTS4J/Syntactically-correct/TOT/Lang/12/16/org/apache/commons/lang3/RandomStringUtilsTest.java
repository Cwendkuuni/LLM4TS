import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {
  
    @Test
    public void testRandom() {
        // Typical use case
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());

        // Edge case
        randomString = RandomStringUtils.random(0);
        assertEquals("", randomString);
      
        // Error scenario
        try {
            randomString = RandomStringUtils.random(-5);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception caught, test passed
        }
    }
  
    @Test
    public void testRandomAscii() {
        // Typical use case
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());
        
        // Edge case
        randomString = RandomStringUtils.randomAscii(0);
        assertEquals("", randomString);
        
        // Error scenario
        try {
            randomString = RandomStringUtils.randomAscii(-5);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception caught, test passed
        }
    }
  
    @Test
    public void testRandomAlphabetic() {
        // Typical use case
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomString.length());
        
        // Edge case
        randomString = RandomStringUtils.randomAlphabetic(0);
        assertEquals("", randomString);
        
        // Error scenario
        try {
            randomString = RandomStringUtils.randomAlphabetic(-5);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception caught, test passed
        }
    }
  
    @Test
    public void testRandomAlphanumeric() {
        // Typical use case
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomString.length());
        
        // Edge case
        randomString = RandomStringUtils.randomAlphanumeric(0);
        assertEquals("", randomString);
        
        // Error scenario
        try {
            randomString = RandomStringUtils.randomAlphanumeric(-5);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception caught, test passed
        }
    }
  
    @Test
    public void testRandomNumeric() {
        // Typical use case
        String randomString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomString.length());
        
        // Edge case
        randomString = RandomStringUtils.randomNumeric(0);
        assertEquals("", randomString);
        
        // Error scenario
        try {
            randomString = RandomStringUtils.randomNumeric(-5);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception caught, test passed
        }
    }

    @Test
    public void testRandomWithCustomChars() {
        // Typical use case
        String randomString = RandomStringUtils.random(10, "abc123");
        assertEquals(10, randomString.length());

        // Edge case
        randomString = RandomStringUtils.random(0, "abc123");
        assertEquals("", randomString);
      
        // Error scenario
        try {
            randomString = RandomStringUtils.random(-5, "abc123");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception caught, test passed
        }
    }

    @Test
    public void testRandomWithCustomCharsArray() {
        // Typical use case
        String randomString = RandomStringUtils.random(10, new char[]{'a', 'b', 'c', '1', '2', '3'});
        assertEquals(10, randomString.length());

        // Edge case
        randomString = RandomStringUtils.random(0, new char[]{'a', 'b', 'c', '1', '2', '3'});
        assertEquals("", randomString);
      
        // Error scenario
        try {
            randomString = RandomStringUtils.random(-5, new char[]{'a', 'b', 'c', '1', '2', '3'});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception caught, test passed
        }
    }
}