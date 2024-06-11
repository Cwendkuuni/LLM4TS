import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RandomStringUtilsTest {

    // Random
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
            Assert.assertTrue((int) c >= 32 && (int) c <= 127);
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
        int start = 32;
        int end = 127;
        boolean letters = false;
        boolean numbers = false;
        String result = RandomStringUtils.random(count, start, end, letters, numbers);

        Assert.assertEquals(count, result.length());

        for (char c : result.toCharArray()) {
            Assert.assertTrue((int) c >= start && (int) c <= end);
        }
    }

    @Test
    public void testRandomWithChars() {
        int count = 10;
        char[] chars = { 'a', 'b', 'c', 'd', 'e' };
        String result = RandomStringUtils.random(count, chars);

        Assert.assertEquals(count, result.length());

        for (char c : result.toCharArray()) {
            boolean found = false;
            for (char ch : chars) {
                if (ch == c) {
                    found = true;
                    break;
                }
            }
            Assert.assertTrue(found);
        }
    }

    @Test
    public void testRandomWithRandomInstance() {
        int count = 10;
        int start = 32;
        int end = 127;
        boolean letters = false;
        boolean numbers = false;
        char[] chars = null;
        Random random = new Random();
        String result = RandomStringUtils.random(count, start, end, letters, numbers, chars, random);

        Assert.assertEquals(count, result.length());

        for (char c : result.toCharArray()) {
            Assert.assertTrue((int) c >= start && (int) c <= end);
        }
    }

    @Test
    public void testRandomWithEmptyChars() {
        int count = 10;
        char[] chars = {};
        int start = 32;
        int end = 127;
        boolean letters = false;
        boolean numbers = false;

        try {
            RandomStringUtils.random(count, start, end, letters, numbers, chars);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

}