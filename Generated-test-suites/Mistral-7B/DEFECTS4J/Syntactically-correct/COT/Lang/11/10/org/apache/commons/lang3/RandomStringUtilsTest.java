import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 5;
        String randomString = RandomStringUtils.random(count);
        Assert.assertEquals(count, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        int count = 5;
        String randomString = RandomStringUtils.randomAscii(count);
        Assert.assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Assert.assertTrue(randomString.charAt(i) >= 32 && randomString.charAt(i) <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 5;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        Assert.assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Assert.assertTrue(Character.isAlphabetic(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 5;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        Assert.assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Assert.assertTrue(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomNumeric() {
        int count = 5;
        String randomString = RandomStringUtils.randomNumeric(count);
        Assert.assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Assert.assertTrue(Character.isDigit(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomWithRange() {
        int count = 5;
        int start = 0;
        int end = 10;
        String randomString = RandomStringUtils.random(count, start, end, false, false, null, RANDOM);
        Assert.assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Assert.assertTrue(randomString.charAt(i) >= start && randomString.charAt(i) <= end);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharsArray() {
        RandomStringUtils.random(5, (char[]) null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomOutOfBounds() {
        int[] bounds = {0, -1};
        RandomStringUtils.random(5, bounds[0], bounds[1], false, false, null, RANDOM);
    }
}