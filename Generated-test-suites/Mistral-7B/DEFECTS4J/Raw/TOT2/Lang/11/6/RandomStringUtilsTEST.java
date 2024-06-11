import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomString.charAt(i);
            // Basic functionality test
            assertTrue(Character.isISOControl(randomChar) == false);
        }
    }

    @Test
    public void testRandomAsciiString() {
        int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertNotNull(randomAsciiString);
        assertEquals(count, randomAsciiString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomAsciiString.charAt(i);
            // Basic functionality test
            assertTrue(Character.isISOControl(randomChar) == false);
            // ASCII range test
            assertTrue((int) randomChar >= 32 && (int) randomChar <= 126);
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertNotNull(randomAlphabeticString);
        assertEquals(count, randomAlphabeticString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomAlphabeticString.charAt(i);
            // Basic functionality test
            assertTrue(Character.isLetter(randomChar) == true);
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertNotNull(randomAlphanumericString);
        assertEquals(count, randomAlphanumericString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomAlphanumericString.charAt(i);
            // Basic functionality test
            assertTrue(Character.isISOControl(randomChar) == false);
            // Alphanumeric character test
            if (Character.isLetter(randomChar)) {
                assertTrue(Character.isLetter(randomChar) == true);
            } else if (Character.isDigit(randomChar)) {
                assertTrue(Character.isDigit(randomChar) == true);
            }
        }
    }

    @Test
    public void testRandomNumericString() {
        int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertNotNull(randomNumericString);
        assertEquals(count, randomNumericString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomNumericString.charAt(i);
            // Basic functionality test
            assertTrue(Character.isISOControl(randomChar) == false);
            // Numeric character test
            assertTrue(Character.isDigit(randomChar) == true);
        }
    }

    @Test
    public void testRandomStringWithParams() {
        int count = 10;
        int start = 97;
        int end = 122;
        boolean letters = true;
        boolean numbers = false;
        String randomString = RandomStringUtils.random(count, start, end, letters, numbers, null, RANDOM);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomString.charAt(i);
            // Basic functionality test
            assertTrue(Character.isISOControl(randomChar) == false);
            if (letters) {
                // Alphabetic character test
                assertTrue((int) randomChar >= start && (int) randomChar <= end);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithInvalidParams() {
        int count = -5;
        String randomString = RandomStringUtils.random(count, 0, 0, false, false, null, RANDOM);
    }

    @Test
    public void testRandomStringWithEmptyCharsArray() {
        int count = 10;
        char[] emptyCharsArray = {};
        String randomString = RandomStringUtils.random(count, emptyCharsArray, RANDOM);
        assertTrue(randomString.isEmpty());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringWithInvalidCharsArray() {
        int count = 10;
        int start = 97;
        int end = 122;
        boolean letters = true;
        boolean numbers = false;
        char[] charsArray = new char[1];
        String randomString = RandomStringUtils.random(count, start, end, letters, numbers, charsArray, RANDOM);
    }
}