import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertNotSame("Same character repeated", randomString.charAt(i), randomString.charAt(i + 1));
        }
    }

    @Test
    public void testRandomAscii() {
        int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals("Length should match", count, randomAsciiString.length());
        for (int i = 0; i < count; i++) {
            int randomChar = randomAsciiString.charAt(i);
            assertTrue("Character should be ASCII", (randomChar >= 0 && randomChar <= 126));
            assertNotSame("Same character repeated", randomAsciiString.charAt(i), randomAsciiString.charAt(i + 1));
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertEquals("Length should match", count, randomAlphabeticString.length());
        for (int i = 0; i < count; i++) {
            boolean isAlpha = Character.isAlphabetic(randomAlphabeticString.charAt(i));
            assertTrue("Character should be alphabetic", isAlpha);
            assertNotSame("Same character repeated", randomAlphabeticString.charAt(i), randomAlphabeticString.charAt(i + 1));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals("Length should match", count, randomAlphanumericString.length());
        for (int i = 0; i < count; i++) {
            boolean isAlphanumeric = Character.isAlphabetic(randomAlphanumericString.charAt(i)) || Character.isDigit(randomAlphanumericString.charAt(i));
            assertTrue("Character should be alphanumeric", isAlphanumeric);
            assertNotSame("Same character repeated", randomAlphanumericString.charAt(i), randomAlphanumericString.charAt(i + 1));
        }
    }

    @Test
    public void testRandomNumeric() {
        int count = 10;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertEquals("Length should match", count, randomNumericString.length());
        for (int i = 0; i < count; i++) {
            boolean isDigit = Character.isDigit(randomNumericString.charAt(i));
            assertTrue("Character should be numeric", isDigit);
            assertNotSame("Same character repeated", randomNumericString.charAt(i), randomNumericString.charAt(i + 1));
        }
    }

    @Test
    public void testRandomWithRange() {
        int count = 10;
        int start = 65;
        int end = 90;
        String randomString = RandomStringUtils.random(count, start, end, false, false, null, RANDOM);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            int randomChar = randomString.charAt(i);
            assertTrue("Character should be within range", (randomChar >= start && randomChar <= end));
            assertNotSame("Same character repeated", randomString.charAt(i), randomString.charAt(i + 1));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        int count = 10;
        String randomString = RandomStringUtils.random(count, true, true, null, RANDOM);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            boolean isAlphanumeric = Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i));
            assertTrue("Character should be alphanumeric", isAlphanumeric);
            assertNotSame("Same character repeated", randomString.charAt(i), randomString.charAt(i + 1));
        }
    }

    @Test
    public void testRandomWithCustomCharacterSet() {
        int count = 10;
        String characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(count, characterSet.toCharArray(), RANDOM);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            char randomChar = randomString.charAt(i);
            assertTrue("Character should be in the set", characterSet.indexOf(randomChar) != -1);
            assertNotSame("Same character repeated", randomString.charAt(i), randomString.charAt(i + 1));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRandomCallCountNegative() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRandomCallEmptyCharacterSet() {
        String emptyCharacterSet = "";
        RandomStringUtils.random(1, emptyCharacterSet.toCharArray(), RANDOM);
    }
}