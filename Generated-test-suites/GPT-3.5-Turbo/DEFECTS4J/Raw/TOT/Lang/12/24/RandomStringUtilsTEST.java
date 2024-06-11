import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String random = RandomStringUtils.random(10);
        Assert.assertNotNull(random);
        Assert.assertEquals(10, random.length());
    }

    @Test
    public void testRandomAscii() {
        String random = RandomStringUtils.randomAscii(10);
        Assert.assertNotNull(random);
        Assert.assertEquals(10, random.length());
        for(char c : random.toCharArray()) {
            Assert.assertTrue((int)c >=32 && (int)c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String random = RandomStringUtils.randomAlphabetic(10);
        Assert.assertNotNull(random);
        Assert.assertEquals(10, random.length());
        for(char c : random.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String random = RandomStringUtils.randomAlphanumeric(10);
        Assert.assertNotNull(random);
        Assert.assertEquals(10, random.length());
        for(char c : random.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String random = RandomStringUtils.randomNumeric(10);
        Assert.assertNotNull(random);
        Assert.assertEquals(10, random.length());
        for(char c : random.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String random = RandomStringUtils.random(10, true, true);
        Assert.assertNotNull(random);
        Assert.assertEquals(10, random.length());
        for(char c : random.toCharArray()) {
            Assert.assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithCustomCharacterSet() {
        String random = RandomStringUtils.random(10, "!@#$%^", null);
        Assert.assertNotNull(random);
        Assert.assertEquals(10, random.length());
        for(char c : random.toCharArray()) {
            Assert.assertTrue(c == '!' || c == '@' || c == '#' || c == '$' || c == '%' || c == '^');
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharacterSet() {
        RandomStringUtils.random(10, "");
    }
}