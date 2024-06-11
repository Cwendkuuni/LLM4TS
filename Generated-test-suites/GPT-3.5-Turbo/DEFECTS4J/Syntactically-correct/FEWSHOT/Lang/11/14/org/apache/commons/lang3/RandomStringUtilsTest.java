import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RandomStringUtilsTest {
    @Test
    public void testRandom() {
        assertEquals(5, RandomStringUtils.random(5).length());
    }

    @Test
    public void testRandomAscii() {
        assertEquals(5, RandomStringUtils.randomAscii(5).length());
        assertTrue(RandomStringUtils.randomAscii(5).matches("[ -~]*"));
    }

    @Test
    public void testRandomAlphabetic() {
        assertEquals(5, RandomStringUtils.randomAlphabetic(5).length());
        assertTrue(RandomStringUtils.randomAlphabetic(5).matches("[a-zA-Z]*"));
    }

    @Test
    public void testRandomAlphanumeric() {
        assertEquals(5, RandomStringUtils.randomAlphanumeric(5).length());
        assertTrue(RandomStringUtils.randomAlphanumeric(5).matches("[a-zA-Z0-9]*"));
    }

    @Test
    public void testRandomNumeric() {
        assertEquals(5, RandomStringUtils.randomNumeric(5).length());
        assertTrue(RandomStringUtils.randomNumeric(5).matches("[0-9]*"));
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        assertEquals(5, RandomStringUtils.random(5, true, true).length());
        assertTrue(RandomStringUtils.random(5, true, true).matches("[a-zA-Z0-9]*"));
    }

    @Test
    public void testRandomWithLettersOnly() {
        assertEquals(5, RandomStringUtils.random(5, true, false).length());
        assertTrue(RandomStringUtils.random(5, true, false).matches("[a-zA-Z]*"));
    }

    @Test
    public void testRandomWithNumbersOnly() {
        assertEquals(5, RandomStringUtils.random(5, false, true).length());
        assertTrue(RandomStringUtils.random(5, false, true).matches("[0-9]*"));
    }

    @Test
    public void testRandomWithEmptyChars() {
        try {
            RandomStringUtils.random(5, null);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("The chars array must not be empty", e.getMessage());
        }
    }

    @Test
    public void testRandomWithSpecificChars() {
        assertEquals(5, RandomStringUtils.random(5, "abc").length());
        assertTrue(RandomStringUtils.random(5, "abc").matches("[abc]*"));
    }
}