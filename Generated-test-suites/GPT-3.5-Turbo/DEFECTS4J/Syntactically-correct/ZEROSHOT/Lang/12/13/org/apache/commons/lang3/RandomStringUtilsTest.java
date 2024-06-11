import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());

        randomString = RandomStringUtils.random(10, true, true);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[a-zA-Z0-9]+"));

        randomString = RandomStringUtils.random(10, false, true);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[0-9]+"));

        randomString = RandomStringUtils.random(10, true, false);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[a-zA-Z]+"));

        randomString = RandomStringUtils.random(10, 'a', 'd', true, true);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[a-dA-D0-9]+"));

        randomString = RandomStringUtils.random(10, 'a', 'd', false, true);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[0-9]+"));

        randomString = RandomStringUtils.random(10, 'a', 'd', true, false);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[a-dA-D]+"));

        randomString = RandomStringUtils.random(10, "ABCD");
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[ABCD]+"));
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[ !-~]+"));
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[0-9]+"));
    }

}