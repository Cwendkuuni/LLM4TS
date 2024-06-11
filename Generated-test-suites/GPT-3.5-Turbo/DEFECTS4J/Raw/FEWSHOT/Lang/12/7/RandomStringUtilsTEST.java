import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        assertEquals(5, RandomStringUtils.random(5).length());
        assertTrue(RandomStringUtils.random(5).matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomAscii() {
        assertEquals(5, RandomStringUtils.randomAscii(5).length());
        assertTrue(RandomStringUtils.randomAscii(5).matches("[ -~]+"));
    }
    
    @Test
    public void testRandomAlphabetic() {
        assertEquals(5, RandomStringUtils.randomAlphabetic(5).length());
        assertTrue(RandomStringUtils.randomAlphabetic(5).matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        assertEquals(5, RandomStringUtils.randomAlphanumeric(5).length());
        assertTrue(RandomStringUtils.randomAlphanumeric(5).matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        assertEquals(5, RandomStringUtils.randomNumeric(5).length());
        assertTrue(RandomStringUtils.randomNumeric(5).matches("[0-9]+"));
    }

    @Test
    public void testRandomWithChars() {
        assertEquals(5, RandomStringUtils.random(5, "abc").length());
        assertTrue(RandomStringUtils.random(5, "abc").matches("[abc]+"));
    }

    @Test
    public void testRandomWithCharsArray() {
        assertEquals(5, RandomStringUtils.random(5, new char[] {'a', 'b', 'c'}).length());
        assertTrue(RandomStringUtils.random(5, new char[] {'a', 'b', 'c'}).matches("[abc]+"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyChars() {
        RandomStringUtils.random(5, "");
    }
    
    @Test
    public void testRandomStartEndAscii() {
        String result = RandomStringUtils.random(10, 32, 127, false, false);
        assertEquals(10, result.length());
        assertTrue(result.matches("[ -~]+"));
    }

    @Test
    public void testRandomStartEndAlphabetic() {
        String result = RandomStringUtils.random(10, 'a', 'z', true, false);
        assertEquals(10, result.length());
        assertTrue(result.matches("[a-z]+"));
    }

    @Test
    public void testRandomStartEndAlphanumeric() {
        String result = RandomStringUtils.random(10, '0', 'z', true, true);
        assertEquals(10, result.length());
        assertTrue(result.matches("[0-9a-zA-Z]+"));
    }

    @Test
    public void testRandomWithRandomInstance() {
        assertEquals(5, RandomStringUtils.random(5, 10, 20, true, false, null, new Random()).length());
    }
}