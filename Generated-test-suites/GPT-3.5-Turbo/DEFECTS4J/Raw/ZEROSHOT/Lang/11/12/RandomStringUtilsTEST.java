import org.junit.Test;

import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String result = RandomStringUtils.random(5);

        assertNotNull(result);
        assertEquals(5, result.length());
    }

    @Test
    public void testRandomAscii() {
        String result = RandomStringUtils.randomAscii(5);

        assertNotNull(result);
        assertEquals(5, result.length());
        assertTrue(result.matches("[\\x20-\\x7E]+"));
    }

    @Test
    public void testRandomAlphabetic() {
        String result = RandomStringUtils.randomAlphabetic(5);

        assertNotNull(result);
        assertEquals(5, result.length());
        assertTrue(result.matches("[A-Za-z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String result = RandomStringUtils.randomAlphanumeric(5);

        assertNotNull(result);
        assertEquals(5, result.length());
        assertTrue(result.matches("[A-Za-z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        String result = RandomStringUtils.randomNumeric(5);

        assertNotNull(result);
        assertEquals(5, result.length());
        assertTrue(result.matches("[0-9]+"));
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String result = RandomStringUtils.random(5, true, true);

        assertNotNull(result);
        assertEquals(5, result.length());
        assertTrue(result.matches("[A-Za-z0-9]+"));
    }

    @Test
    public void testRandomWithStartAndEnd() {
        String result = RandomStringUtils.random(5, 65, 90, true, false);

        assertNotNull(result);
        assertEquals(5, result.length());
        assertTrue(result.matches("[A-Z]+"));
    }

    @Test
    public void testRandomWithCharsArray() {
        char[] chars = {'A', 'B', 'C', 'D', 'E'};
        String result = RandomStringUtils.random(5, chars);

        assertNotNull(result);
        assertEquals(5, result.length());
        assertTrue(result.matches("[ABCDE]+"));
    }

    @Test
    public void testRandomWithNullCharsArray() {
        String result = RandomStringUtils.random(5, (char[]) null);

        assertNotNull(result);
        assertEquals(5, result.length());
    }

    @Test
    public void testRandomWithEmptyCharsArray() {
        char[] chars = {};
        try {
            RandomStringUtils.random(5, chars);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Do nothing, expected exception
        }
    }

    @Test
    public void testRandomWithRandomInstance() {
        String result = RandomStringUtils.random(5, 65, 90, true, false, new Random());

        assertNotNull(result);
        assertEquals(5, result.length());
        assertTrue(result.matches("[A-Z]+"));
    }

    @Test
    public void testRandomWithCountLessThanZero() {
        try {
            RandomStringUtils.random(-1, true, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Do nothing, expected exception
        }
    }

    @Test
    public void testRandomWithNullOrEmptyString() {
        String result1 = RandomStringUtils.random(5, (String) null);
        assertNotNull(result1);
        assertEquals(5, result1.length());

        String result2 = RandomStringUtils.random(5, "");
        assertNotNull(result2);
        assertEquals(5, result2.length());
    }

}