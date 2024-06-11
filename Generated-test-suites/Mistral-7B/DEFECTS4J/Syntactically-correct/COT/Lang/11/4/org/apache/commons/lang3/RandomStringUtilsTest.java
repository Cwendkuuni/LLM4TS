import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

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
        for (char c : randomString.toCharArray()) {
            assert(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-5);
    }

    @Test
    public void testRandomRange() {
        String randomString = RandomStringUtils.random(5, 48, 57);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(c >= 48 && c <= 57);
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomOutOfRange() {
        RandomStringUtils.random(1, 0, 1);
    }

    @Test
    public void testRandomWithChars() {
        String randomString = RandomStringUtils.random("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(randomString.indexOf(c) != -1);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyChars() {
        RandomStringUtils.random(5, (String) null);
    }

}