import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomWithNumbersOnly() {
        String randomString = RandomStringUtils.random(5, false, true);
        assertEquals(5, randomString.length());
        assertTrue(randomString.matches("[0-9]+"));
    }

    @Test
    public void testRandomWithLettersOnly() {
        String randomString = RandomStringUtils.random(5, true, false);
        assertEquals(5, randomString.length());
        assertTrue(randomString.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomWithSpecificChars() {
        String randomString = RandomStringUtils.random(5, "ABCD1234");
        assertEquals(5, randomString.length());
        assertTrue(randomString.matches("[ABCD1234]+"));
    }

    // Add more test cases for edge cases and exception handling scenarios

}