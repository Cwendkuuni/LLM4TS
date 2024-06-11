import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());
        for(char c : randomString.toCharArray()){
            assertTrue((int)c >= 32 && (int)c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomString.length());
        for(char c : randomString.toCharArray()){
            assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomString.length());
        for(char c : randomString.toCharArray()){
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomString.length());
        for(char c : randomString.toCharArray()){
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(50, true, true);
        assertEquals(50, randomString.length());
        for(char c : randomString.toCharArray()){
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomWithStartAndEnd() {
        String randomString = RandomStringUtils.random(50, 65, 90, true, false);
        assertEquals(50, randomString.length());
        for(char c : randomString.toCharArray()){
            assertTrue((int)c >= 65 && (int)c <= 90);
        }
    }

    @Test
    public void testRandomWithChars() {
        char[] chars = {'A', 'B', 'C'};
        String randomString = RandomStringUtils.random(50, chars);
        assertEquals(50, randomString.length());
        for(char c : randomString.toCharArray()){
            assertTrue((int)c >= 65 && (int)c <= 67);
        }
    }

    @Test
    public void testRandomWithInvalidCount() {
        try {
            String randomString = RandomStringUtils.random(-1);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("Requested random string length -1 is less than 0.", e.getMessage());
        }
    }

    @Test
    public void testRandomWithEmptyCharsArray() {
        try {
            String randomString = RandomStringUtils.random(10, new char[]{});
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("Requested random string length 10 is less than 0.", e.getMessage());
        }
    }

    @Test
    public void testRandomWithNullCharsArray() {
        String randomString = RandomStringUtils.random(10, null);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomWithNullCharsString() {
        String randomString = RandomStringUtils.random(10, (String)null);
        assertEquals(10, randomString.length());
    }

}