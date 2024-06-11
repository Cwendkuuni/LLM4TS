import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String random = RandomStringUtils.random(10);
        Assert.assertEquals(10, random.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAscii = RandomStringUtils.randomAscii(10);
        Assert.assertEquals(10, randomAscii.length());
        for (char c : randomAscii.toCharArray()) {
            Assert.assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabetic = RandomStringUtils.randomAlphabetic(10);
        Assert.assertEquals(10, randomAlphabetic.length());
        for (char c : randomAlphabetic.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(10);
        Assert.assertEquals(10, randomAlphanumeric.length());
        for (char c : randomAlphanumeric.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomNumeric = RandomStringUtils.randomNumeric(10);
        Assert.assertEquals(10, randomNumeric.length());
        for (char c : randomNumeric.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String random = RandomStringUtils.random(10, true, true);
        Assert.assertEquals(10, random.length());
        for (char c : random.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithLetters() {
        String random = RandomStringUtils.random(10, true, false);
        Assert.assertEquals(10, random.length());
        for (char c : random.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomWithNumbers() {
        String random = RandomStringUtils.random(10, false, true);
        Assert.assertEquals(10, random.length());
        for (char c : random.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithCustomChars() {
        String random = RandomStringUtils.random(10, new char[]{'A', 'B', 'C'});
        Assert.assertEquals(10, random.length());
        for (char c : random.toCharArray()) {
            Assert.assertTrue(c == 'A' || c == 'B' || c == 'C');
        }
    }

    @Test
    public void testRandomWithCustomCharRange() {
        String random = RandomStringUtils.random(10, 'A', 'Z', false, false);
        Assert.assertEquals(10, random.length());
        for (char c : random.toCharArray()) {
            Assert.assertTrue(c >= 'A' && c <= 'Z');
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyChars() {
        RandomStringUtils.random(10, "");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithMismatchedCharRange() {
        RandomStringUtils.random(10, 'Z', 'A', false, false);
    }

}