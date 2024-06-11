import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomStringWithDefaultOptions() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomAsciiString() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue((int) randomString.charAt(i) >= 32 && (int) randomString.charAt(i) <= 126);
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomNumericString() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isDigit(randomString.charAt(i)));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRandomStringLength() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyRandomStringCharArray() {
        RandomStringUtils.random(0, new char[0]);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringOutOfBoundCharacterSet() {
        char[] chars = new char[2];
        chars[0] = 'a';
        chars[1] = 'z';
        RandomStringUtils.random(1, chars);
        RandomStringUtils.random(1, chars, 2);
    }

    @Test
    public void testRandomStringWithCustomCharacterSet() {
        String randomString = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890", 10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue((randomString.charAt(i) >= 'A' && randomString.charAt(i) <= 'Z')
                    || (randomString.charAt(i) >= '0' && randomString.charAt(i) <= '9'));
        }
    }
}