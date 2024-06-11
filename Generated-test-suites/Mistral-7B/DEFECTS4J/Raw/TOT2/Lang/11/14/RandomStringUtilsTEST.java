import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RandomStringUtilsTest {
    // Test for random method with default parameters
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        int length = randomString.length();
        assertEquals(10, length);
        for (int i = 0; i < length; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue("Randomly generated character is a valid ASCII character.", (randomChar >= 0 && randomChar <= 127));
        }
    }

    // Test for randomAscii method with default parameters
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        int length = randomAsciiString.length();
        assertEquals(10, length);
        for (int i = 0; i < length; i++) {
            char randomAsciiChar = randomAsciiString.charAt(i);
            assertTrue("Randomly generated character is an ASCII character.", (randomAsciiChar >= 32 && randomAsciiChar <= 126));
        }
    }

    // Test for randomAlphabetic method with default parameters
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        int length = randomAlphabeticString.length();
        assertEquals(10, length);
        for (int i = 0; i < length; i++) {
            char randomAlphabeticChar = randomAlphabeticString.charAt(i);
            assertTrue("Randomly generated character is an alphabetic character.", (Character.isAlphabetic(randomAlphabeticChar)));
        }
    }

    // Test for randomAlphanumeric method with default parameters
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        int length = randomAlphanumericString.length();
        assertEquals(10, length);
        for (int i = 0; i < length; i++) {
            char randomAlphanumericChar = randomAlphanumericString.charAt(i);
            assertTrue("Randomly generated character is an alphanumeric character.", (Character.isAlphabetic(randomAlphanumericChar) || Character.isDigit(randomAlphanumericChar)));
        }
    }

    // Test for randomNumeric method with default parameters
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        int length = randomNumericString.length();
        assertEquals(10, length);
        for (int i = 0; i < length; i++) {
            char randomNumericChar = randomNumericString.charAt(i);
            assertTrue("Randomly generated character is a numeric character.", (Character.isDigit(randomNumericChar)));
        }
    }

    // Test for random method with specified length, letters and numbers
    @Test
    public void testRandomWithSpecifiedParams() {
        String randomString = RandomStringUtils.random(10, true, true);
        int length = randomString.length();
        assertEquals(10, length);
        for (int i = 0; i < length; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue("Randomly generated character is an alphanumeric character.", (Character.isAlphabetic(randomChar) || Character.isDigit(randomChar)));
        }
    }

    // Test for random method with specified length, start, end, letters and numbers
    @Test
    public void testRandomWithSpecifiedParams2() {
        String randomString = RandomStringUtils.random(10, 65, 90, true, true);
        int length = randomString.length();
        assertEquals(10, length);
        for (int i = 0; i < length; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue("Randomly generated character is a letter.", (Character.isAlphabetic(randomChar)));
        }
    }
}