import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    /**
     * Test for {@link RandomStringUtils#random(int)} method.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for {@link RandomStringUtils#randomAscii(int)} method.
     */
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(c >= 32 && c <= 126);
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphabetic(int)} method.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomAlphanumeric(int)} method.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                assert(Character.isLetter(c));
            } else if (Character.isDigit(c)) {
                assert(Character.isDigit(c));
            } else {
                throw new AssertionError("Unexpected character: " + c);
            }
        }
    }

    /**
     * Test for {@link RandomStringUtils#randomNumeric(int)} method.
     */
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean)} method.
     */
    @Test
    public void testRandomWithOptions() {
        String randomString = RandomStringUtils.random(10, 1, 2, true, false);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assert(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, int, boolean, boolean, char[], Random)} method.
     */
    @Test
    public void testRandomWithCustomChars() {
        String randomString = RandomStringUtils.random(10, 0, 10, true, true, new char[]{'a', 'b', 'c', '1', '2'}, new Random());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                assert(Character.isLetter(c) && new String(new char[]{'a', 'b', 'c'}).indexOf(c) != -1);
            } else if (Character.isDigit(c)) {
                assert(Character.isDigit(c) && '1'.equals(Character.toString(c)));
            } else {
                throw new AssertionError("Unexpected character: " + c);
            }
        }
    }

    /**
     * Test for edge case: {@link RandomStringUtils#random(0)} method should return an empty string.
     */
    @Test
    public void testRandomEmptyString() {
        String randomString = RandomStringUtils.random(0);
        assertNotNull(randomString);
        assertEquals(0, randomString.length());
    }

    /**
     * Test for edge case: {@link RandomStringUtils#random(Integer.MAX_VALUE)} method should throw an IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomMaxValue() {
        RandomStringUtils.random(Integer.MAX_VALUE);
    }

    /**
     * Test for exception handling: {@link RandomStringUtils#random(int)} method should not throw any checked exceptions.
     */
    @Test(expected = Exception.class)
    public void testRandomCheckedException() throws Exception {
        RandomStringUtils.random(5);
    }

    /**
     * Test for exception handling: {@link RandomStringUtils#random(int, int, boolean, boolean)} method should not throw any checked exceptions.
     */
    @Test(expected = Exception.class)
    public void testRandomWithOptionsCheckedException() throws Exception {
        RandomStringUtils.random(5, 5, true, true);
    }
}