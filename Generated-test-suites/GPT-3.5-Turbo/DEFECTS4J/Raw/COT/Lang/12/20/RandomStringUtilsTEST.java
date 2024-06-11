import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Test random string of length 5
        String randomString = RandomStringUtils.random(5);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        // Test random ASCII string of length 5
        String randomString = RandomStringUtils.randomAscii(5);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        // Test random alphabetic string of length 5
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        // Test random alphanumeric string of length 5
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        // Test random numeric string of length 5
        String randomString = RandomStringUtils.randomNumeric(5);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithStartAndEnd() {
        // Test random string of length 5 with start and end specified
        String randomString = RandomStringUtils.random(5, 'A', 'Z', true, false);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 'A' && c <= 'Z');
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        // Test random string with negative length
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyChars() {
        // Test random string with empty chars
        RandomStringUtils.random(5, "");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithInvalidCharsArray() {
        // Test random string with invalid chars array
        RandomStringUtils.random(5, 0, 0, true, false, null);
    }

    @Test
    public void testRandomWithChars() {
        // Test random string with specified chars
        String randomString = RandomStringUtils.random(5, "ABC123");
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c == 'A' || c == 'B' || c == 'C' || c == '1' || c == '2' || c == '3');
        }
    }

}