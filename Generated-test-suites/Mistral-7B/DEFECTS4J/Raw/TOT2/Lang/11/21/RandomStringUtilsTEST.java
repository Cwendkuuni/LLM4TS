import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789",
                RandomStringUtils.charsToString(RandomStringUtils.random(10, 0, 127, true, true)));
        Expert1.testRandomString(randomString); // Test from Expert 1
    }

    @Test
    public void testRandomStringWithRange() {
        String randomString = RandomStringUtils.random(10, 'a', 'z');
        assertEquals(10, randomString.length());
        assertEquals("abcdefghijklmnopqrstuvwxyz", randomString);
        Expert1.testRandomStringWithRange(randomString); // Test from Expert 1
    }

    @Test
    public void testRandomStringWithCharsArray() {
        char[] chars = {'a', 'b', 'c', 'd'};
        String randomString = RandomStringUtils.random(10, chars);
        assertEquals(10, randomString.length());
        assertEquals(new String(chars), randomString.substring(0, 4) + new String(RandomStringUtils.random(6, chars)) + randomString.substring(randomString.length() - 4));
        Expert1.testRandomStringWithCharsArray(randomString); // Test from Expert 1
    }

    @Test
    public void testRandomStringWithString() {
        String chars = "abc";
        String randomString = RandomStringUtils.random(10, chars);
        assertEquals(10, randomString.length());
        assertEquals(chars, randomString.substring(0, 3) + new String(RandomStringUtils.random(7, chars.toCharArray())) + randomString.substring(randomString.length() - 3));
        Expert1.testRandomStringWithString(randomString); // Test from Expert 1
    }

    @Test
    public void testRandomStringLengthZero() {
        String randomString = RandomStringUtils.random(0);
        assertEquals("", randomString);
        Expert2.testRandomStringLengthZero(randomString); // Test from Expert 2
    }

    @Test
    public void testRandomStringLengthNegative() {
        try {
            RandomStringUtils.random(-1);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Requested random string length -1 is less than 0.", e.getMessage());
        }
        Expert2.testRandomStringLengthNegative(); // Test from Expert 2
    }

    @Test
    public void testRandomStringWithEmptyCharsArray() {
        String randomString = RandomStringUtils.random(10, (char[]) null);
        assertEquals("", randomString);
        Expert2.testRandomStringWithEmptyCharsArray(randomString); // Test from Expert 2
    }

    @Test
    public void testRandomStringWithInvalidCharacterRange() {
        try {
            RandomStringUtils.random(10, -1, 0);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("start must be non-negative", e.getMessage());
        }
        Expert2.testRandomStringWithInvalidCharacterRange(); // Test from Expert 2

        try {
            RandomStringUtils.random(10, Integer.MAX_VALUE, 0);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("end must be non-negative", e.getMessage());
        }
        Expert2.testRandomStringWithInvalidCharacterRange(); // Test from Expert 2

        try {
            RandomStringUtils.random(10, 0, Integer.MIN_VALUE);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("start must be less than end", e.getMessage());
        }
        Expert2.testRandomStringWithInvalidCharacterRange(); // Test from Expert 2
    }

    @Test
    public void testRandomStringWithEmptyString() {
        String randomString = RandomStringUtils.random(10, "");
        assertEquals("", randomString);
        Expert2.testRandomStringWithEmptyString(randomString); // Test from Expert 2
    }

    @Test
    public void testRandomStringWithSpecialCharacter() {
        String randomString = RandomStringUtils.random(1, "!@#$%^&*()_+{}|:\"<>?");
        assertTrue(randomString.matches(".*[!@#$%^&*()_+{}|:\"<>?].*"));
        Expert2.testRandomStringWithSpecialCharacter(randomString); // Test from Expert 2
    }

    @Test
    public void testRandomStringWithHighSurrogate() {
        String randomString = RandomStringUtils.random(2, "\uD800");
        assertTrue(randomString.matches(".*[\uD800][\\p{JavaLowerCase}|\\p{JavaUpperCase}]*"));
        Expert2.testRandomStringWithHighSurrogate(randomString); // Test from Expert 2

        randomString = RandomStringUtils.random(2, "\uDC00");
        assertTrue(randomString.matches(".*[\uDC00][\\p{JavaLowerCase}|\\p{JavaUpperCase}]*"));
        Expert2.testRandomStringWithLowSurrogate(randomString); // Test from Expert 2
    }

    @Test
    public void testRandomStringWithLowSurrogate() {
        String randomString = RandomStringUtils.random(2, "\uDC00");
        assertTrue(randomString.matches(".*[\uDC00][\\p{JavaLowerCase}|\\p{JavaUpperCase}]*"));
        Expert2.testRandomStringWithLowSurrogate(randomString); // Test from Expert 2
    }
}