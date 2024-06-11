import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Test with positive count
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        // Test with zero count
        String emptyString = RandomStringUtils.random(0);
        assertNotNull(emptyString);
        assertEquals(0, emptyString.length());
    }

    @Test
    public void testRandomAscii() {
        // Test with positive count
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomAsciiString);
        assertEquals(5, randomAsciiString.length());

        // Test with zero count
        String emptyAsciiString = RandomStringUtils.randomAscii(0);
        assertNotNull(emptyAsciiString);
        assertEquals(0, emptyAsciiString.length());
    }
    
    @Test
    public void testRandomAlphabetic() {
        // Test with positive count
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomAlphabeticString);
        assertEquals(5, randomAlphabeticString.length());

        // Test with zero count
        String emptyAlphabeticString = RandomStringUtils.randomAlphabetic(0);
        assertNotNull(emptyAlphabeticString);
        assertEquals(0, emptyAlphabeticString.length());
    }
    
    @Test
    public void testRandomAlphanumeric() {
        // Test with positive count
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomAlphanumericString);
        assertEquals(5, randomAlphanumericString.length());

        // Test with zero count
        String emptyAlphanumericString = RandomStringUtils.randomAlphanumeric(0);
        assertNotNull(emptyAlphanumericString);
        assertEquals(0, emptyAlphanumericString.length());
    }
    
    @Test
    public void testRandomNumeric() {
        // Test with positive count
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomNumericString);
        assertEquals(5, randomNumericString.length());

        // Test with zero count
        String emptyNumericString = RandomStringUtils.randomNumeric(0);
        assertNotNull(emptyNumericString);
        assertEquals(0, emptyNumericString.length());
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        // Test with positive count and both letters and numbers
        String randomString = RandomStringUtils.random(5, true, true);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        // Test with zero count and both letters and numbers
        String emptyString = RandomStringUtils.random(0, true, true);
        assertNotNull(emptyString);
        assertEquals(0, emptyString.length());
    }

    @Test
    public void testRandomWithStartAndEnd() {
        // Test with positive count, start, and end characters
        String randomString = RandomStringUtils.random(5, 65, 90, false, false);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        // Test with zero count, start, and end characters
        String emptyString = RandomStringUtils.random(0, 65, 90, false, false);
        assertNotNull(emptyString);
        assertEquals(0, emptyString.length());
    }

    @Test
    public void testRandomWithChars() {
        // Test with positive count and character array
        String randomString = RandomStringUtils.random(5, new char[]{'A', 'B', 'C'});
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        // Test with zero count and character array
        String emptyString = RandomStringUtils.random(0, new char[]{'A', 'B', 'C'});
        assertNotNull(emptyString);
        assertEquals(0, emptyString.length());
    }
    
    @Test
    public void testRandomWithCharsAndRandom() {
        // Test with positive count, character array, and custom random
        String randomString = RandomStringUtils.random(5, new char[]{'A', 'B', 'C'}, new Random());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());

        // Test with zero count, character array, and custom random
        String emptyString = RandomStringUtils.random(0, new char[]{'A', 'B', 'C'}, new Random());
        assertNotNull(emptyString);
        assertEquals(0, emptyString.length());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        // Test with negative count
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        // Test with empty character array
        RandomStringUtils.random(5, new char[]{});
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyString() {
        // Test with empty string
        RandomStringUtils.random(5, "");
    }

}