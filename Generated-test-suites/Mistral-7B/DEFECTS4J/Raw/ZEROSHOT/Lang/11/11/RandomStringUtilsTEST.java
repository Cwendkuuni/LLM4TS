import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
    }

    @Test
    public void testRandomAsciiString() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
    }

    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
    }

    @Test
    public void testRandomStringWithRange() {
        String randomStringWithRange = RandomStringUtils.random(10, 33, 126);
        assertNotNull(randomStringWithRange);
    }

    @Test
    public void testRandomStringWithLettersAndNumbers() {
        String randomStringWithLettersAndNumbers = RandomStringUtils.random(10, true, true);
        assertNotNull(randomStringWithLettersAndNumbers);
    }

    @Test
    public void testRandomStringWithCustomChars() {
        String randomStringWithCustomChars = RandomStringUtils.random(10, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
        assertNotNull(randomStringWithCustomChars);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStringLength() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharsArray() {
        RandomStringUtils.random(1, (char[]) null);
    }

}