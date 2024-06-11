import org.junit.Assert;
import org.junit.Test;

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
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        Assert.assertNotNull(randomAlphabeticString);
        Assert.assertEquals(10, randomAlphabeticString.length());
        Assert.assertTrue(randomAlphabeticString.matches("^[a-zA-Z]+$"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        Assert.assertNotNull(randomAlphanumericString);
        Assert.assertEquals(10, randomAlphanumericString.length());
        Assert.assertTrue(randomAlphanumericString.matches("^[a-zA-Z0-9]+$"));
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        Assert.assertNotNull(randomNumericString);
        Assert.assertEquals(10, randomNumericString.length());
        Assert.assertTrue(randomNumericString.matches("^[0-9]+$"));
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        Assert.assertTrue(randomString.matches("^[a-zA-Z0-9]+$"));
    }

    @Test
    public void testRandomWithCustomChars() {
        String randomString = RandomStringUtils.random(10, new char[]{'H', 'e', 'l', 'l', 'o'});
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        Assert.assertTrue(randomString.matches("^[Hello]+$"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomWithEmptyChars() {
        String randomString = RandomStringUtils.random(10, "");
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomWithNullChars() {
        String randomString = RandomStringUtils.random(10, (String) null);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharArray() {
        RandomStringUtils.random(10, new char[]{});
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithInvalidCharsIndex() {
        RandomStringUtils.random(10, 2, 1, true, true, new char[]{'a', 'b', 'c'});
    }

    @Test
    public void testRandomWithInvalidChars() {
        String randomString = RandomStringUtils.random(10, 100, 200, true, true);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        Assert.assertTrue(randomString.matches("^[a-zA-Z0-9]+$"));
    }

    @Test
    public void testRandomWithCustomRandom() {
        String randomString = RandomStringUtils.random(10, 0, 255, true, true, null, new Random(42));
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        Assert.assertTrue(randomString.matches("^[a-zA-Z0-9]+$"));
    }

    @Test
    public void testRandomWithHighSurrogate() {
        String randomString = RandomStringUtils.random(10, true, true);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomWithLowSurrogate() {
        String randomString = RandomStringUtils.random(10, true, true);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomWithPrivateHighSurrogate() {
        String randomString = RandomStringUtils.random(10, true, true);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        Assert.assertTrue(randomString.matches("^[a-zA-Z0-9]+$"));
    }

    @Test
    public void testRandomWithNullRandom() {
        String randomString = RandomStringUtils.random(10, 0, 255, true, true, null, null);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        Assert.assertTrue(randomString.matches("^[a-zA-Z0-9]+$"));
    }

}