import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 10;
        String result = RandomStringUtils.random(count);
        Assert.assertEquals(count, result.length());
    }

    @Test
    public void testRandomAscii() {
        int count = 10;
        String result = RandomStringUtils.randomAscii(count);
        Assert.assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            Assert.assertTrue(c >= 32 && c <= 127);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String result = RandomStringUtils.randomAlphabetic(count);
        Assert.assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            Assert.assertTrue(Character.isLetter(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String result = RandomStringUtils.randomAlphanumeric(count);
        Assert.assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        int count = 10;
        String result = RandomStringUtils.randomNumeric(count);
        Assert.assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        int count = 10;
        boolean letters = true;
        boolean numbers = true;
        String result = RandomStringUtils.random(count, letters, numbers);
        Assert.assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomWithStartAndEnd() {
        int count = 10;
        int start = 65;  // ASCII value of 'A'
        int end = 90;    // ASCII value of 'Z'
        String result = RandomStringUtils.random(count, start, end, true, false);
        Assert.assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            Assert.assertTrue(Character.isLetter(c));
            Assert.assertTrue(c >= start && c <= end);
        }
    }

    @Test
    public void testRandomWithChars() {
        int count = 10;
        char[] chars = {'A', 'B', 'C', 'D', 'E'};
        String result = RandomStringUtils.random(count, chars);
        Assert.assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            Assert.assertTrue(Character.isLetter(c));
            Assert.assertTrue(chars.contains(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        int count = -10;
        RandomStringUtils.random(count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        int count = 10;
        char[] chars = {};
        RandomStringUtils.random(count, chars);
    }
}