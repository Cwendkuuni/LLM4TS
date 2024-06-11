import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assert(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assert(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assert(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assert(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithSpecifiedLengthAndCharacterSet() {
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assert(Character.isLetterOrDigit(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeLength() {
        RandomStringUtils.random(-5, 0, 0, false, false, null, RANDOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterSet() {
        RandomStringUtils.random(5, new char[0]);
    }

    @Test
    public void testRandomWithCustomRandom() {
        String randomString = new String(new char[10]);
        Random customRandom = new Random();
        String customRandomString = RandomStringUtils.random(5, 0, 52, false, false, randomString.toCharArray(), customRandom);
        assertNotNull(customRandomString);
        assertEquals(5, customRandomString.length());
        for (int i = 0; i < customRandomString.length(); i++) {
            char c = customRandomString.charAt(i);
            assert(c >= 0 && c <= 52);
        }
    }
}