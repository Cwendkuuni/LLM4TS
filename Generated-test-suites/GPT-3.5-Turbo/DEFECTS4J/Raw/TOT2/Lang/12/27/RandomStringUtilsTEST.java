import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(7);
        for(char c : randomAsciiString.toCharArray()) {
            assertTrue((int)c >= 32 && (int)c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        for(char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(8);
        for(char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(6);
        for(char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomSpecific() {
        String randomString = RandomStringUtils.random(5, true, true);
        assertEquals(5, randomString.length());
        for(char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithSpecificRange() {
        String randomString = RandomStringUtils.random(5, 65, 90, true, false);
        assertEquals(5, randomString.length());
        for(char c : randomString.toCharArray()) {
            assertTrue((int)c >= 65 && (int)c <= 90);
        }
    }

}