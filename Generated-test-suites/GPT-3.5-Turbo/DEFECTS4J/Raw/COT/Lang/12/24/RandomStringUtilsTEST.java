import org.junit.Assert;
import org.junit.Test;

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
        for (char c : result.toCharArray()) {
            Assert.assertTrue(c >= 32 && c <= 127);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        String result = RandomStringUtils.randomAlphabetic(5);
        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        String result = RandomStringUtils.randomAlphanumeric(5);
        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        String result = RandomStringUtils.randomNumeric(5);
        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }
    
    @Test
    public void testRandomWithCustomCharset() {
        String charset = "abc123";
        String result = RandomStringUtils.random(5, charset);
        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            Assert.assertTrue(charset.indexOf(c) >= 0);
        }
    }
    
    @Test
    public void testRandomFromCharRange() {
        char start = 'a';
        char end = 'z';
        String result = RandomStringUtils.random(5, start, end, true, false);
        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.length());
        for (char c : result.toCharArray()) {
            Assert.assertTrue(c >= start && c <= end);
            Assert.assertTrue(Character.isAlphabetic(c));
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharset() {
        RandomStringUtils.random(5, "");
    }
    
    @Test
    public void testRandomWithNullCharset() {
        String result = RandomStringUtils.random(5, null);
        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.length());
    }
    
    @Test
    public void testRandomWithEmptyCharArray() {
        char[] emptyArray = {};
        String result = RandomStringUtils.random(5, emptyArray);
        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.length());
    }
    
    @Test
    public void testRandomWithNullCharArray() {
        String result = RandomStringUtils.random(5, (char[]) null);
        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.length());
    }
    
    // TODO: Test edge cases and exception handling scenarios
    
}