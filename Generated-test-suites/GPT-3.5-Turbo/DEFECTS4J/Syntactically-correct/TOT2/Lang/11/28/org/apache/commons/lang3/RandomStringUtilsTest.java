import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        Assert.assertNotNull(randomAsciiString);
        Assert.assertEquals(10, randomAsciiString.length());
        for (char c : randomAsciiString.toCharArray()) {
            Assert.assertTrue((int) c >= 32 && (int) c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        Assert.assertNotNull(randomAlphabeticString);
        Assert.assertEquals(10, randomAlphabeticString.length());
        for (char c : randomAlphabeticString.toCharArray()) {
            Assert.assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        Assert.assertNotNull(randomAlphanumericString);
        Assert.assertEquals(10, randomAlphanumericString.length());
        for (char c : randomAlphanumericString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        Assert.assertNotNull(randomNumericString);
        Assert.assertEquals(10, randomNumericString.length());
        for (char c : randomNumericString.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomEdgeCases() {
        // Test for edge cases
        String edgeCaseString = RandomStringUtils.random(-1);
        Assert.assertEquals("", edgeCaseString);

        try {
            edgeCaseString = RandomStringUtils.random(10, 100, 200, true, false, null, null);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException expected
        }
    }

    @Test
    public void testRandomExceptionHandling() {
        // Test for exception handling scenarios
        String exceptionString = RandomStringUtils.random(10, 56192, 56319, false, true);
        Assert.assertNotNull(exceptionString);
        Assert.assertEquals(10, exceptionString.length());
        for (char c : exceptionString.toCharArray()) {
            Assert.assertFalse((int) c >= 56192 && (int) c <= 56319);
        }
    }
}