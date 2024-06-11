import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length should match", count, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        int count = 5;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        for (int i = 0; i < count; i++) {
            char ch = randomAsciiString.charAt(i);
            assertTrue("Character should be an ASCII character", (ch >= 32 && ch <= 126));
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        int count = 8;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        for (int i = 0; i < count; i++) {
            char ch = randomAlphabeticString.charAt(i);
            assertTrue("Character should be an alphabetic character", Character.isAlphabetic(ch));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        int count = 12;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        for (int i = 0; i < count; i++) {
            char ch = randomAlphanumericString.charAt(i);
            assertTrue("Character should be an alphanumeric character", Character.isAlphabetic(ch) || Character.isDigit(ch));
        }
    }

    @Test
    public void testRandomNumericString() {
        int count = 15;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        for (int i = 0; i < count; i++) {
            char ch = randomNumericString.charAt(i);
            assertTrue("Character should be a numeric character", Character.isDigit(ch));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLengthRandomString() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomStringWithRange() {
        int count = 10;
        int start = 65;
        int end = 90;
        String randomString = RandomStringUtils.random(count, start, end, false, false);
        for (int i = 0; i < count; i++) {
            char ch = randomString.charAt(i);
            assertTrue("Character should be within range", (ch >= start && ch <= end));
        }
    }

    @Test
    public void testRandomStringWithLetters() {
        int count = 10;
        String randomString = RandomStringUtils.random(count, true, false);
        for (int i = 0; i < count; i++) {
            char ch = randomString.charAt(i);
            assertTrue("Character should be an alphabetic character", Character.isAlphabetic(ch));
        }
    }

    @Test
    public void testRandomStringWithNumbers() {
        int count = 10;
        String randomString = RandomStringUtils.random(count, false, true);
        for (int i = 0; i < count; i++) {
            char ch = randomString.charAt(i);
            assertTrue("Character should be a numeric character", Character.isDigit(ch));
        }
    }

    @Test
    public void testRandomStringWithLettersAndNumbers() {
        int count = 10;
        String randomString = RandomStringUtils.random(count, true, true);
        for (int i = 0; i < count; i++) {
            char ch = randomString.charAt(i);
            assertTrue("Character should be an alphanumeric character", Character.isAlphabetic(ch) || Character.isDigit(ch));
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringWithInvalidRange() {
        int count = 10;
        int start = 0;
        int end = -1;
        RandomStringUtils.random(count, start, end, false, false);
    }

    @Test
    public void testRandomStringWithEmptySet() {
        int count = 10;
        char[] emptySet = {};
        try {
            RandomStringUtils.random(count, emptySet, null, false, false, emptySet, RANDOM);
            fail("Should throw IllegalArgumentException for empty set");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}