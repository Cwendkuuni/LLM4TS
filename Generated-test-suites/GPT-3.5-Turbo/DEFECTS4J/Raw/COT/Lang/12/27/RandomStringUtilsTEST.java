import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        Assert.assertEquals("Random string length is not 10", 10, randomString.length());
    }
    
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        for (char c : randomAsciiString.toCharArray()) {
            Assert.assertTrue("Non-ASCII character found", c >= 32 && c <= 126);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        for (char c : randomAlphabeticString.toCharArray()) {
            Assert.assertTrue("Non-alphabetic character found", Character.isAlphabetic(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        for (char c : randomAlphanumericString.toCharArray()) {
            Assert.assertTrue("Non-alphanumeric character found", Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        for (char c : randomNumericString.toCharArray()) {
            Assert.assertTrue("Non-numeric character found", Character.isDigit(c));
        }
    }
    
    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        Assert.assertEquals("Random string length is not 10", 10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue("Non-alphanumeric character found", Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomWithLetters() {
        String randomString = RandomStringUtils.random(10, true, false);
        Assert.assertEquals("Random string length is not 10", 10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue("Non-alphabetic character found", Character.isAlphabetic(c));
        }
    }
    
    @Test
    public void testRandomWithNumbers() {
        String randomString = RandomStringUtils.random(10, false, true);
        Assert.assertEquals("Random string length is not 10", 10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue("Non-numeric character found", Character.isDigit(c));
        }
    }
    
    @Test
    public void testRandomWithCustomCharSet() {
        char[] chars = {'A', 'B', 'C'};
        String randomString = RandomStringUtils.random(10, chars);
        Assert.assertEquals("Random string length is not 10", 10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue("Non-custom character found", c == 'A' || c == 'B' || c == 'C');
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharSet() {
        RandomStringUtils.random(10, new char[]{});
    }
}