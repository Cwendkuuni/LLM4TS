import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    // Test for random method
    @Test
    public void testRandomStringGeneration() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertNotSame("", randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAsciiStringGeneration() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertNotSame("", randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue((c >= 32 && c <= 126));
        }
    }

    @Test
    public void testRandomAlphabeticStringGeneration() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertNotSame("", randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumericStringGeneration() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertNotSame("", randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue((Character.isAlphabetic(c) || Character.isDigit(c)));
        }
    }

    @Test
    public void testRandomNumericStringGeneration() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertNotSame("", randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    // Test for random method with specified length and character set
    @Test
    public void testRandomStringGenerationWithCustomCharacterSet() {
        String characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(10, characterSet.toCharArray());
        assertNotNull(randomString);
        assertNotSame("", randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(characterSet.indexOf(c) != -1);
        }
    }

    // Test for randomStringUtils with empty character set
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringGenerationWithEmptyCharacterSet() {
        RandomStringUtils.random(10, new char[0]);
    }

    // Test for randomStringUtils with negative length
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringGenerationWithNegativeLength() {
        RandomStringUtils.random(-1);
    }

    // Test for randomStringUtils with invalid character set start index
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringGenerationWithInvalidCharacterSetStartIndex() {
        String characterSet = "abc";
        RandomStringUtils.random(1, characterSet.toCharArray(), 3, true, false);
    }

    // Test for randomStringUtils with invalid character set end index
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringGenerationWithInvalidCharacterSetEndIndex() {
        String characterSet = "abc";
        RandomStringUtils.random(1, characterSet.toCharArray(), characterSet.length() + 1, true, false);
    }

    // Test for randomStringUtils with empty String character set
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringGenerationWithEmptyStringCharacterSet() {
        RandomStringUtils.random(1, "");
    }

    // Test for randomStringUtils with null String character set
    @Test(expected = NullPointerException.class)
    public void testRandomStringGenerationWithNullStringCharacterSet() {
        RandomStringUtils.random(1, null);
    }
}