import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    // Test for random(int count)
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    // Test for randomAscii(int count)
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(15);
        assertNotNull(randomAsciiString);
        assertEquals(15, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }

    // Test for randomAlphabetic(int count)
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(20);
        assertNotNull(randomAlphabeticString);
        assertEquals(20, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    // Test for randomAlphanumeric(int count)
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(25);
        assertNotNull(randomAlphanumericString);
        assertEquals(25, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    // Test for randomNumeric(int count)
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(30);
        assertNotNull(randomNumericString);
        assertEquals(30, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    // Test for random(int count, boolean letters, boolean numbers)
    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(20, true, true);
        assertNotNull(randomString);
        assertEquals(20, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    // Test for random(int count, int start, int end, boolean letters, boolean numbers)
    @Test
    public void testRandomWithCustomRange() {
        String randomString = RandomStringUtils.random(15, 65, 90, true, false);
        assertNotNull(randomString);
        assertEquals(15, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 65 && c <= 90);
        }
    }

    // Test for random(int count, int start, int end, boolean letters, boolean numbers, char... chars)
    @Test
    public void testRandomWithCustomChars() {
        char[] customChars = {'a', 'b', 'c', '1', '2', '3'};
        String randomString = RandomStringUtils.random(10, 0, 0, false, false, customChars);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(new String(customChars).contains(String.valueOf(c)));
        }
    }

    // Test for random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random)
    @Test
    public void testRandomWithCustomRandom() {
        String randomString = RandomStringUtils.random(10, 65, 90, true, false, null, new Random(5));
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 65 && c <= 90);
        }
    }

    // Test for random(int count, String chars)
    @Test
    public void testRandomWithCustomCharsString() {
        String customChars = "abc123";
        String randomString = RandomStringUtils.random(10, customChars);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(customChars.contains(String.valueOf(c)));
        }
    }

    // Test for random(int count, char... chars)
    @Test
    public void testRandomWithCustomCharSet() {
        char[] customChars = {'x', 'y', 'z', '4', '5', '6'};
        String randomString = RandomStringUtils.random(8, customChars);
        assertNotNull(randomString);
        assertEquals(8, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(new String(customChars).contains(String.valueOf(c)));
        }
    }

}