import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Typical use case
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());

        // Edge case: count = 0
        randomString = RandomStringUtils.random(0);
        assertEquals(0, randomString.length());

        // Error scenario: negative count
        try {
            randomString = RandomStringUtils.random(-5);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testRandomAscii() {
        // Typical use case
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());

        // Edge case: count = 0
        randomString = RandomStringUtils.randomAscii(0);
        assertEquals(0, randomString.length());

        // Error scenario: negative count
        try {
            randomString = RandomStringUtils.randomAscii(-5);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        // Typical use case
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomString.length());

        // Edge case: count = 0
        randomString = RandomStringUtils.randomAlphabetic(0);
        assertEquals(0, randomString.length());

        // Error scenario: negative count
        try {
            randomString = RandomStringUtils.randomAlphabetic(-5);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        // Typical use case
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomString.length());

        // Edge case: count = 0
        randomString = RandomStringUtils.randomAlphanumeric(0);
        assertEquals(0, randomString.length());

        // Error scenario: negative count
        try {
            randomString = RandomStringUtils.randomAlphanumeric(-5);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testRandomNumeric() {
        // Typical use case
        String randomString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomString.length());

        // Edge case: count = 0
        randomString = RandomStringUtils.randomNumeric(0);
        assertEquals(0, randomString.length());

        // Error scenario: negative count
        try {
            randomString = RandomStringUtils.randomNumeric(-5);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testRandomChars() {
        // Typical use case
        String randomString = RandomStringUtils.random(10, new char[]{'a', 'b', 'c'});
        assertEquals(10, randomString.length());

        // Typical use case with empty chars array
        randomString = RandomStringUtils.random(10, new char[]{});
        assertEquals(10, randomString.length());

        // Edge case: count = 0
        randomString = RandomStringUtils.random(0, new char[]{});
        assertEquals(0, randomString.length());

        // Error scenario: negative count
        try {
            randomString = RandomStringUtils.random(-5, new char[]{'a', 'b', 'c'});
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        // Error scenario: null chars array
        try {
            randomString = RandomStringUtils.random(5, null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}