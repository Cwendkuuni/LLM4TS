import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            assertTrue((int) c >= 32 && (int) c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithInvalidCount() {
        try {
            String randomString = RandomStringUtils.random(-1);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testRandomAlphabeticWithInvalidCount() {
        try {
            String randomAlphabeticString = RandomStringUtils.randomAlphabetic(-1);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testRandomAlphanumericWithEmptyChars() {
        try {
            String randomAlphanumericString = RandomStringUtils.random(10, 32, 127, false, true, new char[]{});
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testRandomWithNullChars() {
        try {
            String randomString = RandomStringUtils.random(10, null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

}