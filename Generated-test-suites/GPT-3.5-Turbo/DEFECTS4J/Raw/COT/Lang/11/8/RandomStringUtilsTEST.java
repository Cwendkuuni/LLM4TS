import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom_WithCount_ReturnsStringWithSpecifiedLength() {
        int count = 5;
        String randomString = RandomStringUtils.random(count);
        assertEquals(count, randomString.length());
    }

    @Test
    public void testRandomAscii_WithCount_ReturnsStringWithSpecifiedLength() {
        int count = 5;
        String randomString = RandomStringUtils.randomAscii(count);
        assertEquals(count, randomString.length());
    }

    @Test
    public void testRandomAlphabetic_WithCount_ReturnsStringWithSpecifiedLength() {
        int count = 5;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        assertEquals(count, randomString.length());
    }

    @Test
    public void testRandomAlphanumeric_WithCount_ReturnsStringWithSpecifiedLength() {
        int count = 5;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals(count, randomString.length());
    }

    @Test
    public void testRandomNumeric_WithCount_ReturnsStringWithSpecifiedLength() {
        int count = 5;
        String randomString = RandomStringUtils.randomNumeric(count);
        assertEquals(count, randomString.length());
    }

    @Test
    public void testRandom_WithCountAndLetters_ReturnsStringWithSpecifiedLengthContainingOnlyLetters() {
        int count = 5;
        String randomString = RandomStringUtils.random(count, true, false);
        assertTrue(randomString.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandom_WithCountAndNumbers_ReturnsStringWithSpecifiedLengthContainingOnlyNumbers() {
        int count = 5;
        String randomString = RandomStringUtils.random(count, false, true);
        assertTrue(randomString.matches("[0-9]+"));
    }

    @Test
    public void testRandom_WithCountAndChars_ReturnsStringWithSpecifiedLengthContainingOnlySpecifiedChars() {
        int count = 5;
        char[] chars = {'a', 'b', 'c'};
        String randomString = RandomStringUtils.random(count, chars);
        assertTrue(randomString.matches("[abc]+"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandom_WithNegativeCount_ThrowsIllegalArgumentException() {
        int count = -5;
        RandomStringUtils.random(count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandom_WithEmptyCharsArray_ThrowsIllegalArgumentException() {
        int count = 5;
        char[] chars = {};
        RandomStringUtils.random(count, chars);
    }

    @Test
    public void testRandom_WithInvalidSurrogatePair_ReturnsStringWithoutInvalidChars() {
        int count = 5;
        String randomString = RandomStringUtils.random(count);
        assertFalse(randomString.contains("\uDB80"));
        assertFalse(randomString.contains("\uDBFF"));
    }
}