
import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {

    // random
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
    }

    // randomAscii
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        Assert.assertNotNull(randomAsciiString);
        Assert.assertEquals(10, randomAsciiString.length());

        for (char c : randomAsciiString.toCharArray()) {
            Assert.assertTrue(c >= 32 && c <= 127);
        }
    }

    // randomAlphabetic
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        Assert.assertNotNull(randomAlphabeticString);
        Assert.assertEquals(10, randomAlphabeticString.length());

        for (char c : randomAlphabeticString.toCharArray()) {
            Assert.assertTrue(Character.isLetter(c));
        }
    }

    // randomAlphanumeric
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        Assert.assertNotNull(randomAlphanumericString);
        Assert.assertEquals(10, randomAlphanumericString.length());

        for (char c : randomAlphanumericString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    // randomNumeric
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        Assert.assertNotNull(randomNumericString);
        Assert.assertEquals(10, randomNumericString.length());

        for (char c : randomNumericString.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }

    // random with letters and numbers
    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());

        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    // random with start and end
    @Test
    public void testRandomWithStartAndEnd() {
        String randomString = RandomStringUtils.random(10, 10, 20, true, true);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());

        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    // random with char array
    @Test
    public void testRandomWithCharArray() {
        char[] chars = {'a', 'b', 'c', '1', '2', '3'};
        String randomString = RandomStringUtils.random(10, chars);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());

        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(new String(chars).indexOf(c) >= 0);
        }
    }

    // random with source of randomness
    @Test
    public void testRandomWithSourceOfRandomness() {
        Random random = new Random();
        String randomString = RandomStringUtils.random(10, 0, 0, true, true, null, random);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());

        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    // random with empty char array
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharArray() {
        char[] emptyChars = {};
        RandomStringUtils.random(10, emptyChars);
    }

    // random with negative count
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-10);
    }

    // random with empty string
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyString() {
        RandomStringUtils.random(10, "");
    }
}
