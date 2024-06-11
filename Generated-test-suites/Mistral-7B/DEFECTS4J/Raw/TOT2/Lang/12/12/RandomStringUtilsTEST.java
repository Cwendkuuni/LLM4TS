import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomStringLengthZero() {
        assertEquals("", RandomStringUtils.random(0));
    }

    @Test
    public void testRandomStringLengthOne() {
        assertEquals("a", RandomStringUtils.randomAlphabetic(1));
        assertEquals("0", RandomStringUtils.randomNumeric(1));
        assertEquals("a", RandomStringUtils.randomAlphabetic(1, 'a', 'z'));
        assertEquals("0", RandomStringUtils.randomNumeric(1, 0, 10));
    }

    @Test
    public void testRandomStringLengthGreaterThanOne() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char currentChar = randomString.charAt(i);
            boolean isLetter = Character.isLetter(currentChar);
            boolean isNumber = Character.isDigit(currentChar);
            if ((isLetter && RandomStringUtils.randomAlphabetic(1, true, true, null, null)[0] != currentChar)
                    || (isNumber && RandomStringUtils.randomNumeric(1, false, true, null, null)[0] != currentChar)
                    || (!isLetter && !isNumber && RandomStringUtils.random(1, null, null, false, false, null, null)[0] != currentChar)) {
                fail("Generated random strings should not contain duplicate characters.");
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringNegativeLength() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringNullChars() {
        RandomStringUtils.random(5, null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringInvalidCharacterRange() {
        RandomStringUtils.random(5, 56192, 56319, true, true);
    }
}