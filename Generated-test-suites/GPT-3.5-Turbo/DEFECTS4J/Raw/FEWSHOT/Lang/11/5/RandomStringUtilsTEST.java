import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RandomStringUtilsTest {
    @Test
    public void testRandom() {
        assertEquals("", RandomStringUtils.random(0));
        assertEquals("abc", RandomStringUtils.random(3));
        assertEquals("abc12!@", RandomStringUtils.random(8));
    }

    @Test
    public void testRandomAscii() {
        assertEquals("", RandomStringUtils.randomAscii(0));
        assertEquals("abcd", RandomStringUtils.randomAscii(4));
        assertEquals("abcd123!!", RandomStringUtils.randomAscii(10));
    }

    @Test
    public void testRandomAlphabetic() {
        assertEquals("", RandomStringUtils.randomAlphabetic(0));
        assertEquals("abcd", RandomStringUtils.randomAlphabetic(4));
        assertEquals("zyxwvutsrqpo", RandomStringUtils.randomAlphabetic(13));
    }

    @Test
    public void testRandomAlphanumeric() {
        assertEquals("", RandomStringUtils.randomAlphanumeric(0));
        assertEquals("abcd123", RandomStringUtils.randomAlphanumeric(8));
        assertEquals("zyxwvutsr1245", RandomStringUtils.randomAlphanumeric(13));
    }

    @Test
    public void testRandomNumeric() {
        assertEquals("", RandomStringUtils.randomNumeric(0));
        assertEquals("12345", RandomStringUtils.randomNumeric(5));
        assertEquals("987654321", RandomStringUtils.randomNumeric(9));
    }

    @Test
    public void testRandomWithChars() {
        assertEquals("", RandomStringUtils.random(0, "abc"));
        assertEquals("aabbcc", RandomStringUtils.random(6, "abc"));
        assertEquals("112233ab", RandomStringUtils.random(8, "123abc"));
    }

    @Test
    public void testRandomWithinRange() {
        assertEquals("", RandomStringUtils.random(0, 65, 90, true, false));
        assertEquals("AAAA", RandomStringUtils.random(4, 65, 65, true, false));
        assertEquals("ABCD", RandomStringUtils.random(4, 65, 68, true, false));
        assertEquals("ABCDE2", RandomStringUtils.random(6, 65, 69, true, true));
        assertEquals("12345", RandomStringUtils.random(5, 48, 57, false, true));
        assertEquals("99999", RandomStringUtils.random(5, 57, 57, false, true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharsArray() {
        char[] chars = new char[0];
        RandomStringUtils.random(5, chars);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyString() {
        RandomStringUtils.random(5, "");
    }
}