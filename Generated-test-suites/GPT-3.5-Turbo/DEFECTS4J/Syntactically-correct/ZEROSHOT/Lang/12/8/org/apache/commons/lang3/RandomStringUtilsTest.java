import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

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

        for (char c : randomString.toCharArray()) {
            Assert.assertTrue((int) c >= 32 && (int) c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 5;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        Assert.assertEquals(count, randomString.length());

        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 5;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        Assert.assertEquals(count, randomString.length());

        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        int count = 5;
        String randomString = RandomStringUtils.randomNumeric(count);
        Assert.assertEquals(count, randomString.length());

        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomLetters() {
        int count = 5;
        boolean letters = true;
        boolean numbers = false;
        String randomString = RandomStringUtils.random(count, letters, numbers);
        Assert.assertEquals(count, randomString.length());

        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomNumbers() {
        int count = 5;
        boolean letters = false;
        boolean numbers = true;
        String randomString = RandomStringUtils.random(count, letters, numbers);
        Assert.assertEquals(count, randomString.length());

        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithChars() {
        int count = 5;
        int start = 97;  // 'a'
        int end = 122;  // 'z'
        boolean letters = true;
        boolean numbers = false;
        char[] chars = {'a', 'b', 'c', 'd', 'e'};
        String randomString = RandomStringUtils.random(count, start, end, letters, numbers, chars);
        Assert.assertEquals(count, randomString.length());

        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(c >= start && c <= end && (c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e'));
        }
    }

    @Test
    public void testRandomWithRandom() {
        int count = 5;
        int start = 97;  // 'a'
        int end = 122;  // 'z'
        boolean letters = true;
        boolean numbers = false;
        char[] chars = {'a', 'b', 'c', 'd', 'e'};
        Random random = new Random(1);
        String randomString = RandomStringUtils.random(count, start, end, letters, numbers, chars, random);
        Assert.assertEquals(count, randomString.length());

        String expected = "deecb";
        Assert.assertEquals(expected, randomString);
    }

    @Test
    public void testRandomString() {
        int count = 5;
        String chars = "abcde";
        String randomString = RandomStringUtils.random(count, chars);
        Assert.assertEquals(count, randomString.length());

        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(chars.contains(String.valueOf(c)));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringWithEmptyChars() {
        int count = 5;
        String chars = "";
        RandomStringUtils.random(count, chars);
    }

    @Test
    public void testRandomCharArray() {
        int count = 5;
        char[] chars = {'a', 'b', 'c', 'd', 'e'};
        String randomString = RandomStringUtils.random(count, chars);
        Assert.assertEquals(count, randomString.length());

        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e');
        }
    }

    @Test
    public void testRandomUnique() {
        int count = 10;
        boolean letters = true;
        boolean numbers = true;
        boolean unique = true;
        String randomString = RandomStringUtils.random(count, letters, numbers);
        Assert.assertEquals(count, randomString.length());

        Set<Character> charSet = new HashSet<>();
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue((Character.isLetter(c) || Character.isDigit(c)) && charSet.add(c));
        }
    }

}