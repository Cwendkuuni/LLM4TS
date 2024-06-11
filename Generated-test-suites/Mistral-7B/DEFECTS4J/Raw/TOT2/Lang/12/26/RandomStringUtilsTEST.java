import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {
    // Test for random method with default options
    @Test
    public void testRandomString() {
        String randomStr = RandomStringUtils.random(10);
        assertNotNull(randomStr);
        assertEquals(10, randomStr.length());
    }

    // Test for randomAscii method
    @Test
    public void testRandomAsciiString() {
        String randomStr = RandomStringUtils.randomAscii(10);
        assertNotNull(randomStr);
        assertEquals(10, randomStr.length());
        for (char c : randomStr.toCharArray()) {
            assertTrue(Character.isISOControl(c) || (Character.isDigit(c) || Character.isLetter(c) || Character.isWhitespace(c) || Character.isUpperCase(c) || Character.isLowerCase(c) || Character.isGraphic(c)));
        }
    }

    // Test for randomAlphabetic method
    @Test
    public void testRandomAlphabeticString() {
        String randomStr = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomStr);
        assertEquals(10, randomStr.length());
        for (char c : randomStr.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    // Test for randomAlphanumeric method
    @Test
    public void testRandomAlphanumericString() {
        String randomStr = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomStr);
        assertEquals(10, randomStr.length());
        for (char c : randomStr.toCharArray()) {
            assertTrue(Character.isLetter(c) || Character.isDigit(c) || Character.isWhitespace(c) || Character.isGraphic(c));
        }
    }

    // Test for randomNumeric method
    @Test
    public void testRandomNumericString() {
        String randomStr = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomStr);
        assertEquals(10, randomStr.length());
        for (char c : randomStr.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    // Test for random method with custom length, letters, and numbers
    @Test
    public void testRandomCustomString() {
        String randomStr = RandomStringUtils.random(5, true, true);
        assertNotNull(randomStr);
        assertEquals(5, randomStr.length());
        for (char c : randomStr.toCharArray()) {
            assertTrue(Character.isLetter(c) || Character.isDigit(c) || Character.isWhitespace(c) || Character.isGraphic(c));
        }
    }

    // Test for random method with custom length, start, end, letters, and numbers
    @Test
    public void testRandomCustomWithSetString() {
        String randomStr = RandomStringUtils.random(5, 'a', 'z', true, true);
        assertNotNull(randomStr);
        assertEquals(5, randomStr.length());
        for (char c : randomStr.toCharArray()) {
            assertTrue(Character.isLetter(c) || Character.isDigit(c));
            if (Character.isLetter(c)) {
                assertTrue((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
            }
        }
    }

    // Test for random method with negative length
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-5);
    }

    // Test for random method with empty set
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptySet() {
        String[] emptySet = {};
        RandomStringUtils.random(5, emptySet, new Random());
    }
}