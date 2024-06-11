import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RandomStringUtilsTest {

    private RandomStringUtils randomStringUtils;

    @BeforeEach
    public void setUp() {
        randomStringUtils = new RandomStringUtils();
    }

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        Assertions.assertNotNull(randomString);
        Assertions.assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        Assertions.assertNotNull(randomAsciiString);
        Assertions.assertEquals(5, randomAsciiString.length());
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        Assertions.assertNotNull(randomAlphabeticString);
        Assertions.assertEquals(5, randomAlphabeticString.length());
        Assertions.assertTrue(randomAlphabeticString.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        Assertions.assertNotNull(randomAlphanumericString);
        Assertions.assertEquals(5, randomAlphanumericString.length());
        Assertions.assertTrue(randomAlphanumericString.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        Assertions.assertNotNull(randomNumericString);
        Assertions.assertEquals(5, randomNumericString.length());
        Assertions.assertTrue(randomNumericString.matches("[0-9]+"));
    }

    @Test
    public void testRandomWithAdditionalChars() {
        String additionalChars = "!@#$%^&*()";
        String randomString = RandomStringUtils.random(5, additionalChars);
        Assertions.assertNotNull(randomString);
        Assertions.assertEquals(5, randomString.length());
        Assertions.assertTrue(randomString.matches("[a-zA-Z0-9" + additionalChars + "]+"));
    }

    @Test
    public void testRandomWithEmptyAdditionalChars() {
        String additionalChars = "";
        String randomString = RandomStringUtils.random(5, additionalChars);
        Assertions.assertNotNull(randomString);
        Assertions.assertEquals(5, randomString.length());
        Assertions.assertTrue(randomString.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomWithNullAdditionalChars() {
        String randomString = RandomStringUtils.random(5, null);
        Assertions.assertNotNull(randomString);
        Assertions.assertEquals(5, randomString.length());
        Assertions.assertTrue(randomString.matches("[a-zA-Z0-9]+"));
    }

}