import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertFalse(randomString.isEmpty());
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertFalse(randomAsciiString.isEmpty());
        assertEquals(10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertFalse(randomAlphabeticString.isEmpty());
        assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertFalse(randomAlphanumericString.isEmpty());
        assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                assertTrue(Character.isLetterOrDigit(c));
            } else {
                assertTrue(Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertFalse(randomNumericString.isEmpty());
        assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRandomStringLength() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomStringWithRange() {
        String randomString = RandomStringUtils.random(10, 33, 124);
        assertNotNull(randomString);
        assertFalse(randomString.isEmpty());
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 33 && c <= 124);
        }
    }

    @Test
    public void testRandomStringWithFixedSeed() {
        String seededRandomString = new RandomStringUtils().random(10, 'A', 'Z');
        String sameRandomString = new RandomStringUtils().random(10, 'A', 'Z');
        assertEquals(seededRandomString, sameRandomString);
    }

    @Test
    public void testRandomStringWithNullChars() {
        String randomString = RandomStringUtils.random(10, null);
        assertNotNull(randomString);
        assertFalse(randomString.isEmpty());
        assertEquals(10, randomString.length());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringWithNegativeStart() {
        RandomStringUtils.random(10, -1, 1, true, true, null, RANDOM);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringWithEndBeyondStart() {
        RandomStringUtils.random(10, 1, 100, true, true, null, RANDOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptyCharsArray() {
        RandomStringUtils.random(10, new char[0], null, true, true, null, RANDOM);
    }

    @Test
    public void testRandomStringWithCharsArray() {
        char[] chars = {'A', 'B', 'C', 'D', 'E'};
        String randomString = RandomStringUtils.random(10, chars, null, true, true, null, RANDOM);
        assertNotNull(randomString);
        assertFalse(randomString.isEmpty());
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Arrays.binarySearch(chars, c) >= 0);
        }
    }
}