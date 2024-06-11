import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length should match", count, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        int count = 5;
        String randomString = RandomStringUtils.randomAscii(count);
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            assertTrue("Should be an ASCII character", (c >= 32 && c <= 126));
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 15;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            assertTrue("Should be an alphabetic character", Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            if (Character.isAlphabetic(c)) {
                assertTrue("Should be alphabetic or numeric", Character.isAlphabetic(c) || Character.isDigit(c));
            } else {
                assertTrue("Should be alphabetic or numeric", Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomNumeric() {
        int count = 15;
        String randomString = RandomStringUtils.randomNumeric(count);
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            assertTrue("Should be a numeric character", Character.isDigit(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomWithEmptyCharsArray() {
        int count = 5;
        String randomString = RandomStringUtils.random(count, (char[]) null);
        assertEquals("Should be empty string", "", randomString);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithInvalidRange() {
        int count = 10;
        String chars = "abc";
        String randomString = RandomStringUtils.random(count, chars, 0, 2, false, false);
        fail("Should throw ArrayIndexOutOfBoundsException");
    }

    @Test
    public void testRandomWithValidRange() {
        int count = 10;
        String chars = "abc";
        String randomString = RandomStringUtils.random(count, chars, 1, 2, false, false);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            assertTrue("Should be within range", (c == chars.charAt(1) || c == chars.charAt(2)));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        int count = 10;
        String randomString = RandomStringUtils.random(count, true, true);
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            if (Character.isLetter(c)) {
                assertTrue("Should be alphabetic character", Character.isAlphabetic(c));
            } else {
                assertTrue("Should be digit", Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomWithOnlyLetters() {
        int count = 10;
        String randomString = RandomStringUtils.random(count, true, false);
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            assertTrue("Should be alphabetic character", Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomWithOnlyNumbers() {
        int count = 10;
        String randomString = RandomStringUtils.random(count, false, true);
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            assertTrue("Should be digit", Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithCustomChars() {
        int count = 10;
        String customChars = "!@#$%^&*()_+-=[]{}|;:,.<>?/";
        String randomString = RandomStringUtils.random(count, customChars);
        for (int i = 0; i < count; i++) {
            char c = randomString.charAt(i);
            assertTrue("Should be a valid character", customChars.indexOf(c) != -1);
        }
    }
}