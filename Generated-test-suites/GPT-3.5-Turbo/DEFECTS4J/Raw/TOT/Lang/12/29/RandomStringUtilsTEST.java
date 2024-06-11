import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Typical use case
        String result = RandomStringUtils.random(10);
        Assert.assertEquals(10, result.length());

        // Edge case - zero length
        result = RandomStringUtils.random(0);
        Assert.assertEquals("", result);

        // Error scenario - negative length
        try {
            result = RandomStringUtils.random(-10);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception to be thrown
        }
    }

    @Test
    public void testRandomAscii() {
        // Typical use case
        String result = RandomStringUtils.randomAscii(10);
        Assert.assertEquals(10, result.length());

        // Edge case - zero length
        result = RandomStringUtils.randomAscii(0);
        Assert.assertEquals("", result);

        // Error scenario - negative length
        try {
            result = RandomStringUtils.randomAscii(-10);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception to be thrown
        }
    }

    @Test
    public void testRandomAlphabetic() {
        // Typical use case
        String result = RandomStringUtils.randomAlphabetic(10);
        Assert.assertEquals(10, result.length());

        // Edge case - zero length
        result = RandomStringUtils.randomAlphabetic(0);
        Assert.assertEquals("", result);

        // Error scenario - negative length
        try {
            result = RandomStringUtils.randomAlphabetic(-10);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception to be thrown
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        // Typical use case
        String result = RandomStringUtils.randomAlphanumeric(10);
        Assert.assertEquals(10, result.length());

        // Edge case - zero length
        result = RandomStringUtils.randomAlphanumeric(0);
        Assert.assertEquals("", result);

        // Error scenario - negative length
        try {
            result = RandomStringUtils.randomAlphanumeric(-10);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception to be thrown
        }
    }

    @Test
    public void testRandomNumeric() {
        // Typical use case
        String result = RandomStringUtils.randomNumeric(10);
        Assert.assertEquals(10, result.length());

        // Edge case - zero length
        result = RandomStringUtils.randomNumeric(0);
        Assert.assertEquals("", result);

        // Error scenario - negative length
        try {
            result = RandomStringUtils.randomNumeric(-10);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception to be thrown
        }
    }

    @Test
    public void testRandomWithCharacterSet() {
        // Typical use case
        String result = RandomStringUtils.random(10, "ABC123");
        Assert.assertEquals(10, result.length());

        // Edge case - zero length
        result = RandomStringUtils.random(0, "ABC123");
        Assert.assertEquals("", result);

        // Error scenario - negative length
        try {
            result = RandomStringUtils.random(-10, "ABC123");
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception to be thrown
        }

        // Error scenario - empty character set
        try {
            result = RandomStringUtils.random(10, "");
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception to be thrown
        }
    }
}