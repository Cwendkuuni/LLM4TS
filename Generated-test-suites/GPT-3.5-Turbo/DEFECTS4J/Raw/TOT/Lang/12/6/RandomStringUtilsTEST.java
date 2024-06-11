import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        Assertions.assertNotNull(randomString);
        Assertions.assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        Assertions.assertNotNull(randomAsciiString);
        Assertions.assertEquals(10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            Assertions.assertTrue((int) c >= 32 && (int) c <= 127);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        Assertions.assertNotNull(randomAlphabeticString);
        Assertions.assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            Assertions.assertTrue(Character.isLetter(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        Assertions.assertNotNull(randomAlphanumericString);
        Assertions.assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            Assertions.assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        Assertions.assertNotNull(randomNumericString);
        Assertions.assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            Assertions.assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithInvalidCount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RandomStringUtils.random(-10);
        });
    }

    @Test
    public void testRandomWithNullChars() {
        String randomString = RandomStringUtils.random(10, null);
        Assertions.assertNotNull(randomString);
        Assertions.assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomWithEmptyChars() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RandomStringUtils.random(10, "");
        });
    }

    @Test
    public void testRandomWithValidChars() {
        String randomString = RandomStringUtils.random(10, "ABCabc123");
        Assertions.assertNotNull(randomString);
        Assertions.assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assertions.assertTrue("ABCabc123".indexOf(c) != -1);
        }
    }

}