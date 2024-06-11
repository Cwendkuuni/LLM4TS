import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String generatedString = RandomStringUtils.random(10);
        Assert.assertEquals(10, generatedString.length());
    }

    @Test
    public void testRandomAscii() {
        String generatedString = RandomStringUtils.randomAscii(10);
        Assert.assertEquals(10, generatedString.length());
        Assert.assertTrue(generatedString.matches("[\\p{ASCII}]+"));
    }

    @Test
    public void testRandomAlphabetic() {
        String generatedString = RandomStringUtils.randomAlphabetic(10);
        Assert.assertEquals(10, generatedString.length());
        Assert.assertTrue(generatedString.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String generatedString = RandomStringUtils.randomAlphanumeric(10);
        Assert.assertEquals(10, generatedString.length());
        Assert.assertTrue(generatedString.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        String generatedString = RandomStringUtils.randomNumeric(10);
        Assert.assertEquals(10, generatedString.length());
        Assert.assertTrue(generatedString.matches("[0-9]+"));
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String generatedString = RandomStringUtils.random(10, true, true);
        Assert.assertEquals(10, generatedString.length());
        Assert.assertTrue(generatedString.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomWithLettersOnly() {
        String generatedString = RandomStringUtils.random(10, true, false);
        Assert.assertEquals(10, generatedString.length());
        Assert.assertTrue(generatedString.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomWithNumbersOnly() {
        String generatedString = RandomStringUtils.random(10, false, true);
        Assert.assertEquals(10, generatedString.length());
        Assert.assertTrue(generatedString.matches("[0-9]+"));
    }

    @Test
    public void testRandomWithCustomChars() {
        char[] customChars = {'a', 'b', 'c'};
        String generatedString = RandomStringUtils.random(10, customChars);
        Assert.assertEquals(10, generatedString.length());
        Assert.assertTrue(generatedString.matches("[abc]+"));
    }

    @Test
    public void testRandomWithInvalidCount() {
        try {
            String generatedString = RandomStringUtils.random(-1);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("Requested random string length"));
        }
    }

    @Test
    public void testRandomWithInvalidChars() {
        try {
            String generatedString = RandomStringUtils.random(10, "");
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("the string is empty"));
        }
    }
}