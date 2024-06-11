import org.apache.commons.lang3.RandomStringUtils;
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

        for (char c : randomAsciiString.toCharArray()) {
            Assert.assertTrue(Character.isISOControl(c) || Character.isDigit(c) || Character.isLetter(c) || Character.isWhitespace(c) || Character.isUpperCase(c) || Character.isLowerCase(c));
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        Assert.assertNotNull(randomAlphabeticString);
        Assert.assertEquals(10, randomAlphabeticString.length());

        for (char c : randomAlphabeticString.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c) || Character.isWhitespace(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        Assert.assertNotNull(randomAlphanumericString);
        Assert.assertEquals(10, randomAlphanumericString.length());

        for (char c : randomAlphanumericString.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || Character.isWhitespace(c));
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
    public void testRandomWithRange() {
        String randomString = RandomStringUtils.random(10, 'a', 'z');
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());

        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c) || Character.isWhitespace(c));
        }
    }

    @Test
    public void testRandomWithCustomCharset() {
        String customCharset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+{}|:\"<>?,./;' ";
        String randomString = RandomStringUtils.random(10, customCharset.toCharArray());
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());

        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(customCharset.indexOf(c) != -1);
        }
    }
}