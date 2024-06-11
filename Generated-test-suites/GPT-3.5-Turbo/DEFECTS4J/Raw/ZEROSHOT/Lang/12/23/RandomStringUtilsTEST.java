import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String result = RandomStringUtils.random(10);
        Assert.assertNotNull(result);
        Assert.assertEquals(10, result.length());
    }

    @Test
    public void testRandomAscii() {
        String result = RandomStringUtils.randomAscii(10);
        Assert.assertNotNull(result);
        Assert.assertEquals(10, result.length());
        Assert.assertTrue(result.matches("[\\x20-\\x7E]+"));
    }

    @Test
    public void testRandomAlphabetic() {
        String result = RandomStringUtils.randomAlphabetic(10);
        Assert.assertNotNull(result);
        Assert.assertEquals(10, result.length());
        Assert.assertTrue(result.matches("[A-Za-z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String result = RandomStringUtils.randomAlphanumeric(10);
        Assert.assertNotNull(result);
        Assert.assertEquals(10, result.length());
        Assert.assertTrue(result.matches("[A-Za-z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        String result = RandomStringUtils.randomNumeric(10);
        Assert.assertNotNull(result);
        Assert.assertEquals(10, result.length());
        Assert.assertTrue(result.matches("[0-9]+"));
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String result = RandomStringUtils.random(10, true, true);
        Assert.assertNotNull(result);
        Assert.assertEquals(10, result.length());
        Assert.assertTrue(result.matches("[A-Za-z0-9]+"));
    }

    @Test
    public void testRandomWithStartAndEnd() {
        String result = RandomStringUtils.random(10, 65, 90, true, false);
        Assert.assertNotNull(result);
        Assert.assertEquals(10, result.length());
        Assert.assertTrue(result.matches("[A-Z]+"));
    }

    @Test
    public void testRandomWithCustomChars() {
        String result = RandomStringUtils.random(10, new char[]{'A', 'B', 'C'});
        Assert.assertNotNull(result);
        Assert.assertEquals(10, result.length());
        Assert.assertTrue(result.matches("[ABC]+"));
    }

    @Test
    public void testRandomWithInvalidCount() {
        try {
            RandomStringUtils.random(-10);
            Assert.fail("IllegalArgumentException should be thrown for negative count");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testRandomWithInvalidChars() {
        try {
            RandomStringUtils.random(10, new char[]{});
            Assert.fail("IllegalArgumentException should be thrown for empty chars array");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}