import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RandomStringUtilsTest {
    // Test for random method
    @Test
    public void testRandomString() {
        int count = 10;
        String result = RandomStringUtils.random(count);
        assertEquals("Expected random string of length " + count, count, result.length());
        for (int i = 0; i < count; i++) {
            char randomChar = result.charAt(i);
            assertTrue("Expected random character at index " + i, Character.isAlphabetic(randomChar) || Character.isDigit(randomChar) || Character.isWhitespace(randomChar));
        }
    }

    // Test for randomAscii method
    @Test
    public void testRandomAsciiString() {
        int count = 10;
        String result = RandomStringUtils.randomAscii(count);
        assertEquals("Expected random string of length " + count, count, result.length());
        for (int i = 0; i < count; i++) {
            char randomChar = result.charAt(i);
            assertTrue("Expected ASCII character at index " + i, (char) (32 <= randomChar && randomChar <= 126));
        }
    }

    // Test for randomAlphabetic method
    @Test
    public void testRandomAlphabeticString() {
        int count = 10;
        String result = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Expected random string of length " + count, count, result.length());
        for (int i = 0; i < count; i++) {
            char randomChar = result.charAt(i);
            assertTrue("Expected alphabetic character at index " + i, Character.isAlphabetic(randomChar));
        }
    }

    // Test for randomAlphanumeric method
    @Test
    public void testRandomAlphanumericString() {
        int count = 10;
        String result = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Expected random string of length " + count, count, result.length());
        for (int i = 0; i < count; i++) {
            char randomChar = result.charAt(i);
            assertTrue("Expected alphanumeric character at index " + i, Character.isAlphabetic(randomChar) || Character.isDigit(randomChar) || Character.isWhitespace(randomChar));
        }
    }

    // Test for randomNumeric method
    @Test
    public void testRandomNumericString() {
        int count = 10;
        String result = RandomStringUtils.randomNumeric(count);
        assertEquals("Expected random string of length " + count, count, result.length());
        for (int i = 0; i < count; i++) {
            char randomChar = result.charAt(i);
            assertTrue("Expected numeric character at index " + i, Character.isDigit(randomChar));
        }
    }

    // Test for random method with specified length and character set
    @Test
    public void testRandomStringWithSet() {
        int count = 10;
        char[] chars = {'a', 'b', 'c', 'd'};
        String result = RandomStringUtils.random(count, chars);
        assertEquals("Expected random string of length " + count, count, result.length());
        for (int i = 0; i < count; i++) {
            char randomChar = result.charAt(i);
            assertTrue("Expected set character at index " + i, (chars[0] <= randomChar && randomChar <= chars[3]));
        }
    }

    // Test for random method with negative length
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringNegativeLength() {
        String result = RandomStringUtils.random(-1);
    }

    // Test for random method with null character set
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringNullCharacterSet() {
        String result = RandomStringUtils.random(1, (char[]) null);
    }

    // Test for random method with empty character set
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringEmptyCharacterSet() {
        String result = RandomStringUtils.random(1, new char[0]);
    }
}