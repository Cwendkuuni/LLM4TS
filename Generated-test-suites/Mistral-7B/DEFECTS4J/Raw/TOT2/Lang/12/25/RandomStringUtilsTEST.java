// RandomStringUtilsTest.java

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        // Test generating a random string of a specified length
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assert (randomString.length() == count);

        // Test generating a random string with only alphabetic characters
        String alphabeticString = RandomStringUtils.randomAlphabetic(count);
        for (int i = 0; i < count; i++) {
            char c = alphabeticString.charAt(i);
            assertTrue(Character.isAlphabetic(c));
        }

        // Test generating a random string with only numeric characters
        String numericString = RandomStringUtils.randomNumeric(count);
        for (int i = 0; i < count; i++) {
            char c = numericString.charAt(i);
            assertTrue(Character.isDigit(c));
        }

        // Test generating a random string with alpha-numeric characters
        String alphanumericString = RandomStringUtils.randomAlphanumeric(count);
        for (int i = 0; i < count; i++) {
            char c = alphanumericString.charAt(i);
            assertTrue(Character.isLetterOrDigit(c));
        }

        // Test generating a random string with a specified start and end character set
        String customString = RandomStringUtils.random(count, 'a', 'z');
        for (int i = 0; i < count; i++) {
            char c = customString.charAt(i);
            assertTrue(c >= 'a' && c <= 'z');
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() {
        // Test exception when count is less than 0
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharArray() {
        // Test exception when char array is empty
        char[] emptyChars = {};
        RandomStringUtils.random(1, emptyChars);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStart() {
        // Test exception when start is less than 0
        int start = -1;
        int end = 'z' + 1;
        RandomStringUtils.random(1, start, end, false, false, null, RANDOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeEnd() {
        // Test exception when end is less than 0
        int start = ' ';
        int end = -1;
        RandomStringUtils.random(1, start, end, false, false, null, RANDOM);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testInvalidCharArrayLength() {
        // Test exception when there are not (end - start) + 1 characters in the set array
        char[] chars = new char[1];
        RandomStringUtils.random(1, 0, chars.length, false, false, chars, RANDOM);
    }
}