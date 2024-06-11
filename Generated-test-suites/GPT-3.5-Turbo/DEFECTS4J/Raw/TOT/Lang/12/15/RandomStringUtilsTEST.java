import org.junit.Assert;
import org.junit.Test;
import java.util.HashSet;

public class RandomStringUtilsTest {

    @Test
    public void testRandomAllCharacters() {
        String randomString = RandomStringUtils.random(10);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(c >= 32 && c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomWithStartAndEnd() {
        String randomString = RandomStringUtils.random(10, 'A', 'Z', true, false);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(c >= 'A' && c <= 'Z');
        }
    }

    @Test
    public void testRandomWithCharacterSet() {
        String randomString = RandomStringUtils.random(10, "abcdeABC123");
        Assert.assertNotNull(randomString);
        Assert.assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue("abcdeABC123".contains(String.valueOf(c)));
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

    @Test
    public void testRandomWithCustomRandomObject() {
        HashSet<String> randomStrings = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String randomString = RandomStringUtils.random(10, new Random());
            randomStrings.add(randomString);
        }
        Assert.assertEquals(100, randomStrings.size());
    }

}