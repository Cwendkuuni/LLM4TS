import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String result = RandomStringUtils.random(10);
        Assert.assertNotNull(result);
        Assert.assertEquals(10, result.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-5);
    }

    @Test
    public void testRandomAscii() {
        String result = RandomStringUtils.randomAscii(15);
        Assert.assertNotNull(result);
        Assert.assertEquals(15, result.length());
        for (char c : result.toCharArray()) {
            Assert.assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String result = RandomStringUtils.randomAlphabetic(20);
        Assert.assertNotNull(result);
        Assert.assertEquals(20, result.length());
        for (char c : result.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String result = RandomStringUtils.randomAlphanumeric(25);
        Assert.assertNotNull(result);
        Assert.assertEquals(25, result.length());
        for (char c : result.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String result = RandomStringUtils.randomNumeric(30);
        Assert.assertNotNull(result);
        Assert.assertEquals(30, result.length());
        for (char c : result.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String result = RandomStringUtils.random(15, true, true);
        Assert.assertNotNull(result);
        Assert.assertEquals(15, result.length());
        for (char c : result.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomWithCustomCharacters() {
        String result = RandomStringUtils.random(10, new char[]{'A', 'B', 'C'});
        Assert.assertNotNull(result);
        Assert.assertEquals(10, result.length());
        for (char c : result.toCharArray()) {
            Assert.assertTrue(c == 'A' || c == 'B' || c == 'C');
        }
    }

    @Test
    public void testRandomWithInvalidCount() {
        String result = RandomStringUtils.random(0);
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharArray() {
        RandomStringUtils.random(5, new char[]{});
    }
}