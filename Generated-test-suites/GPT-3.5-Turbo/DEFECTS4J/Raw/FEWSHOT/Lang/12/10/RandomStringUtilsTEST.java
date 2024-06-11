
import org.junit.Test;

import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
        assertTrue(isValidRandomString(randomString));
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());
        assertTrue(isValidRandomString(randomString));

        randomString = RandomStringUtils.randomAscii(0);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[a-zA-Z]+"));

        randomString = RandomStringUtils.randomAlphabetic(0);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[a-zA-Z0-9]+"));

        randomString = RandomStringUtils.randomAlphanumeric(0);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[0-9]+"));

        randomString = RandomStringUtils.randomNumeric(0);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomWithChars() {
        String randomString = RandomStringUtils.random(10, "ABCD");
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[ABCD]+"));
    }

    @Test
    public void testRandomWithNullChars() {
        String randomString = RandomStringUtils.random(10, (String) null);
        assertEquals(10, randomString.length());
        assertTrue(isValidRandomString(randomString));
    }

    @Test
    public void testRandomWithEmptyChars() {
        String randomString = RandomStringUtils.random(10, "");
        assertEquals("", randomString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomInvalidCount() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomWithStartAndEnd() {
        String randomString = RandomStringUtils.random(10, 65, 90, true, false);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[A-Z]+"));

        randomString = RandomStringUtils.random(10, 97, 122, true, false);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[a-z]+"));
    }
    
    private boolean isValidRandomString(String randomString) {
        return randomString.matches("[a-zA-Z0-9]+");
    }
}
