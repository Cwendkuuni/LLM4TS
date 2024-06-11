import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAsciiString() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAsciiString);
        assertEquals(10, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            assertEquals(32, (int) randomAsciiString.charAt(i) & 0x1F);
            assertEquals(126, (int) randomAsciiString.charAt(i) | 0x80);
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabeticString);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            assertEquals(Character.isAlphabetic(randomAlphabeticString.charAt(i)), true);
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumericString);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            assertEquals(Character.isAlphabetic(randomAlphanumericString.charAt(i)) || Character.isDigit(randomAlphanumericString.charAt(i)), true);
        }
    }

    @Test
    public void testRandomNumericString() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumericString);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            assertEquals(Character.isDigit(randomNumericString.charAt(i)), true);
        }
    }

    @Test
    public void testRandomStringWithRange() {
        String randomString = RandomStringUtils.random(10, 33, 126);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertEquals(33 + (int) Math.random() * 94, (int) randomString.charAt(i));
        }
    }

    @Test
    public void testRandomStringWithLetters() {
        String randomString = RandomStringUtils.random(10, true, false);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertEquals(Character.isAlphabetic(randomString.charAt(i)), true);
        }
    }

    @Test
    public void testRandomStringWithNumbers() {
        String randomString = RandomStringUtils.random(10, false, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertEquals(Character.isDigit(randomString.charAt(i)), true);
        }
    }

    @Test
    public void testRandomStringWithBothLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertEquals(Character.isLetterOrDigit(randomString.charAt(i)), true);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringNegativeLength() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringEmptyCharacterSet() {
        RandomStringUtils.random(1, (char[]) null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringOutOfBounds() {
        char[] chars = new char[1];
        RandomStringUtils.random(1, chars, 1);
    }
}