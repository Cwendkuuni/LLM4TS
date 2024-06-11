
import org.junit.Test;
import org.apache.commons.lang3.RandomStringUtils;

import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String result = RandomStringUtils.random(10);
        assertEquals(10, result.length());
    }

    @Test
    public void testRandomAscii() {
        String result = RandomStringUtils.randomAscii(10);
        assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String result = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String result = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String result = RandomStringUtils.randomNumeric(10);
        assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithCustomRange() {
        String result = RandomStringUtils.random(10, 'A', 'Z', true, false);
        assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetter(c));
            assertTrue(c >= 'A' && c <= 'Z');
        }
    }

    @Test
    public void testRandomWithCustomChars() {
        String result = RandomStringUtils.random(10, "ABC123");
        assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(c == 'A' || c == 'B' || c == 'C' || c == '1' || c == '2' || c == '3');
        }
    }

    @Test
    public void testRandomWithInvalidLength() {
        try {
            RandomStringUtils.random(-10);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testRandomWithEmptyCharsArray() {
        try {
            RandomStringUtils.random(10, new char[]{});
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testRandomWithNullCharsArray() {
        String result = RandomStringUtils.random(10, (char[]) null);
        assertEquals(10, result.length());
    }

    @Test
    public void testRandomWithNullCharsString() {
        String result = RandomStringUtils.random(10, (String) null);
        assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
}

