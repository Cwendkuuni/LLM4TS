import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length of generated string should be equal to specified number", count, randomString.length());
        // Add more assertions based on the requirements
    }

    @Test
    public void testRandomAsciiString() {
        int count = 10;
        String randomString = RandomStringUtils.randomAscii(count);
        assertEquals("Length of generated string should be equal to specified number", count, randomString.length());
        // Add more assertions based on the requirements
    }

    @Test
    public void testRandomAlphabeticString() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Length of generated string should be equal to specified number", count, randomString.length());
        // Add more assertions based on the requirements
    }

    @Test
    public void testRandomAlphanumericString() {
        int count = 10;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Length of generated string should be equal to specified number", count, randomString.length());
        // Add more assertions based on the requirements
    }

    @Test
    public void testRandomNumericString() {
        int count = 10;
        String randomString = RandomStringUtils.randomNumeric(count);
        assertEquals("Length of generated string should be equal to specified number", count, randomString.length());
        // Add more assertions based on the requirements
    }

    @Test
    public void testRandomStringWithCustomCharacterSet() {
        int count = 10;
        String customCharacterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(count, customCharacterSet.toCharArray());
        assertEquals("Length of generated string should be equal to specified number", count, randomString.length());
        // Add more assertions based on the requirements
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRandomStringLength() {
        int count = -5;
        try {
            RandomStringUtils.random(count);
        } catch (IllegalArgumentException e) {
            assertEquals("Requested random string length is less than 0.", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCustomCharacterSet() {
        int count = 10;
        String customCharacterSet = "";
        try {
            RandomStringUtils.random(count, customCharacterSet.toCharArray());
        } catch (IllegalArgumentException e) {
            assertEquals("The chars array must not be empty", e.getMessage());
            throw e;
        }
    }
}