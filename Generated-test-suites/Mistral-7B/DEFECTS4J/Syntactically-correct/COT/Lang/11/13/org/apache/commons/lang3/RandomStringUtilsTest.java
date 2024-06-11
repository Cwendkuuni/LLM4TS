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
        int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals("Length should match", count, randomAsciiString.length());
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Length should match", count, randomAlphabeticString.length());
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Length should match", count, randomAlphanumericString.length());
    }

    @Test
    public void testRandomNumeric() {
        int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertEquals("Length should match", count, randomNumericString.length());
    }

    @Test
    public void testRandomWithRangeAndLetters() {
        int count = 10;
        int start = 65;
        int end = 90;
        boolean letters = true;
        String randomString = RandomStringUtils.random(count, start, end, letters, false);
        for (int i = 0; i < count; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue("All characters should be within the specified range", randomChar >= start && randomChar <= end);
            if (letters) {
                assertTrue("All characters should be alphabetic", Character.isAlphabetic(randomChar));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        RandomStringUtils.random(1, new char[0]);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithInvalidCharRange() {
        int count = 10;
        int start = 56321;
        int end = 56320;
        boolean letters = false;
        RandomStringUtils.random(count, start, end, letters, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNullChars() {
        RandomStringUtils.random(1, null);
    }
}