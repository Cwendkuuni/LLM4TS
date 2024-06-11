import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 5;
        String actual = RandomStringUtils.random(count);
        assertEquals("Length should match", count, actual.length());
    }

    @Test
    public void testRandomAscii() {
        int count = 5;
        String actual = RandomStringUtils.randomAscii(count);
        assertEquals("Length should match", count, actual.length());
        for (char c : actual.toCharArray()) {
            assertTrue("All characters should be ASCII", (c >= 32 && c <= 126));
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 5;
        String actual = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Length should match", count, actual.length());
        for (char c : actual.toCharArray()) {
            assertTrue("All characters should be alphabetic", Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 5;
        String actual = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Length should match", count, actual.length());
        for (char c : actual.toCharArray()) {
            assertTrue("All characters should be alphanumeric", Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        int count = 5;
        String actual = RandomStringUtils.randomNumeric(count);
        assertEquals("Length should match", count, actual.length());
        for (char c : actual.toCharArray()) {
            assertTrue("All characters should be numeric", Character.isDigit(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyChars() {
        RandomStringUtils.random(0, (char[]) null);
    }

    @Test
    public void testRandomCustomChars() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int count = 5;
        String actual = RandomStringUtils.random(count, chars);
        assertEquals("Length should match", count, actual.length());
        for (char c : actual.toCharArray()) {
            assertTrue("All characters should be in the set", chars.indexOf(c) != -1);
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomOutOfBounds() {
        int start = 26;
        int end = 0;
        RandomStringUtils.random(10, start, end, true, false, null, RANDOM);
    }

    @Test
    public void testRandomSurrogate() {
        int count = 2;
        String actual = RandomStringUtils.random(count, 55296, 56191, true, false, null, RANDOM);
        assertEquals("Length should match", count * 2, actual.length());
        for (int i = 0; i < actual.length(); i += 2) {
            assertTrue("All surrogates should be valid", Character.isHighSurrogate(actual.charAt(i)) && Character.isLowSurrogate(actual.charAt(i + 1)));
        }
    }

    @Test
    public void testRandomEdgeCases() {
        int count = 1000;
        String actual = RandomStringUtils.random(count, '!', '~', true, true, null, RANDOM);
        assertEquals("Length should match", count, actual.length());
    }

}