import org.junit.Assert;
import org.junit.Test;
import java.util.Random;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String result = RandomStringUtils.random(5);
        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.length());
    }

    @Test
    public void testRandomAscii() {
        String result = RandomStringUtils.randomAscii(5);
        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.length());

        for(char c : result.toCharArray()) {
            Assert.assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String result = RandomStringUtils.randomAlphabetic(5);
        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.length());

        for(char c : result.toCharArray()) {
            Assert.assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String result = RandomStringUtils.randomAlphanumeric(5);
        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.length());

        for(char c : result.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String result = RandomStringUtils.randomNumeric(5);
        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.length());

        for(char c : result.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String result = RandomStringUtils.random(5, true, true);
        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.length());

        for(char c : result.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomWithStartAndEnd() {
        String result = RandomStringUtils.random(5, 97, 122, true, false);
        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.length());

        for(char c : result.toCharArray()) {
            Assert.assertTrue(c >= 97 && c <= 122);
        }
    }

    @Test
    public void testRandomWithCharArray() {
        char[] chars = {'a', 'b', 'c'};
        String result = RandomStringUtils.random(5, chars);
        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.length());

        for(char c : result.toCharArray()) {
            Assert.assertTrue(c == 'a' || c == 'b' || c == 'c');
        }
    }
}