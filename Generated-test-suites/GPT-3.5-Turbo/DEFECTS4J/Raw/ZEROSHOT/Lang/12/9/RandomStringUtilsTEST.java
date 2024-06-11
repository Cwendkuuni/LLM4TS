import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        Assert.assertEquals(5, randomString.length());
    }
    
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        Assert.assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            Assert.assertTrue(c >= 32 && c <= 127);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        Assert.assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            Assert.assertTrue(Character.isLetter(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        Assert.assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        Assert.assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            Assert.assertTrue(Character.isDigit(c));
        }
    }
    
    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(5, true, true);
        Assert.assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomWithStartAndEnd() {
        String randomString = RandomStringUtils.random(5, 'A', 'Z', false, false);
        Assert.assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            Assert.assertTrue(c >= 'A' && c <= 'Z');
        }
    }
    
    @Test
    public void testRandomWithChars() {
        char[] chars = {'A', 'B', 'C'};
        String randomString = RandomStringUtils.random(5, chars);
        Assert.assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            Assert.assertTrue(c == 'A' || c == 'B' || c == 'C');
        }
    }
    
    @Test
    public void testRandomWithSpecifiedRandom() {
        // Using a fixed seed to ensure consistent results
        Random random = new Random(123);
        String randomString1 = RandomStringUtils.random(5, false, false, random);
        String randomString2 = RandomStringUtils.random(5, false, false, random);
        Assert.assertEquals(randomString1, randomString2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyChars() {
        char[] chars = {};
        RandomStringUtils.random(5, chars);
    }
    
    @Test
    public void testRandomWithNullChars() {
        String randomString = RandomStringUtils.random(5, (char[]) null);
        Assert.assertEquals(5, randomString.length());
    }
    
    @Test
    public void testRandomWithNullStringChars() {
        String randomString = RandomStringUtils.random(5, (String) null);
        Assert.assertEquals(5, randomString.length());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyStringChars() {
        RandomStringUtils.random(5, "");
    }

}