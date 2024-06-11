import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[ -~]+"));
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

    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomWithStartAndEnd() {
        String randomString = RandomStringUtils.random(10, 65, 90, true, false);
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[A-Z]+"));
    }

    @Test
    public void testRandomWithCharArray() {
        String randomString = RandomStringUtils.random(10, new char[] {'a', 'b', 'c', 'd'});
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[abcd]+"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        RandomStringUtils.random(10, new char[] {});
    }

    @Test
    public void testRandomWithNullCharsArray() {
        String randomString = RandomStringUtils.random(10, (char[]) null);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomWithStringChars() {
        String randomString = RandomStringUtils.random(10, "abcd");
        assertEquals(10, randomString.length());
        assertTrue(randomString.matches("[abcd]+"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyStringChars() {
        RandomStringUtils.random(10, "");
    }

    @Test
    public void testRandomWithNullStringChars() {
        String randomString = RandomStringUtils.random(10, (String) null);
        assertEquals(10, randomString.length());
    }
}