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
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue("Character " + c + " is not an ASCII character", c >= 32 && c <= 126);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue("Character " + c + " is not an alphabetic character", Character.isLetter(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue("Character " + c + " is neither alphabetic nor numeric", Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue("Character " + c + " is not a numeric character", Character.isDigit(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }
    
    @Test
    public void testRandomSpecificChars() {
        String randomString = RandomStringUtils.random(10, "abc123");
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue("Character " + c + " is not one of the specified characters", c == 'a' || c == 'b' || c == 'c' || c == '1' || c == '2' || c == '3');
        }
    }

    @Test
    public void testRandomChars() {
        String randomString = RandomStringUtils.random(10, 'a', 'z', false, false);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue("Character " + c + " is not a lower case letter", c >= 'a' && c <= 'z');
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomEmptyChars() {
        RandomStringUtils.random(10, null);
    }

    @Test
    public void testRandomStartEndChars() {
        String randomString = RandomStringUtils.random(10, 'A', 'D', false, false);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue("Character " + c + " is not in the specified range", c >= 'A' && c <= 'D');
        }
    }

    @Test
    public void testRandomWithRandom() {
        char[] chars = {'a', 'b', 'c'};
        Random random = new Random(123);
        String randomString1 = RandomStringUtils.random(10, 0, 2, false, false, chars, random);
        random.setSeed(123);
        String randomString2 = RandomStringUtils.random(10, 0, 2, false, false, chars, random);
        assertEquals(randomString1, randomString2);
    }

    @Test
    public void testRandomWithSurrogates() {
        String randomString = RandomStringUtils.random(10, 0, 0, true, true);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length() - 1; i++) {
            char c1 = randomString.charAt(i);
            char c2 = randomString.charAt(i + 1);
            assertTrue("Character " + c1 + " is not a alphanumeric character", Character.isLetterOrDigit(c1));
            if (Character.isHighSurrogate(c1)) {
                assertTrue("Character " + c2 + " is not a low surrogate", Character.isLowSurrogate(c2));
            }
        }
    }
}