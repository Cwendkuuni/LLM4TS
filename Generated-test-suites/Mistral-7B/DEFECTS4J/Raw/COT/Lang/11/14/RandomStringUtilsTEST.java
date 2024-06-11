import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

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
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        Assert.assertEquals(count, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            Assert.assertTrue(randomAsciiString.charAt(i) >= 32 && randomAsciiString.charAt(i) <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 5;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        Assert.assertEquals(count, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            Assert.assertTrue(Character.isAlphabetic(randomAlphabeticString.charAt(i)));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 5;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        Assert.assertEquals(count, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            Assert.assertTrue(Character.isLetterOrDigit(randomAlphanumericString.charAt(i)));
        }
    }

    @Test
    public void testRandomNumeric() {
        int count = 5;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        Assert.assertEquals(count, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            Assert.assertTrue(Character.isDigit(randomNumericString.charAt(i)));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        int count = -1;
        RandomStringUtils.random(count);
    }

    @Test
    public void testRandomWithChars() {
        int count = 5;
        char[] chars = {'a', 'b', 'c'};
        String randomString = RandomStringUtils.random(count, chars);
        Assert.assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Assert.assertTrue(Character.isLetter(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)) || Character.equals(randomString.charAt(i), ' '));
            Assert.assertArrayEquals(new char[]{'a', 'b', 'c'}, RandomStringUtils.random(3, chars));
        }
    }

    @Test
    public void testRandomWithStartAndEnd() {
        int count = 5;
        int start = 97;
        int end = 123;
        String randomString = RandomStringUtils.random(count, start, end, false, false, null, RANDOM);
        Assert.assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            Assert.assertTrue(randomString.charAt(i) >= start && randomString.charAt(i) <= end);
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomInvalidStartEnd() {
        int count = 5;
        int start = 0;
        int end = -1;
        RandomStringUtils.random(count, start, end, false, false, null, RANDOM);
    }

    @Test
    public void testRandomSurrogate() {
        int count = 1;
        Random random = new Random();
        String randomString = RandomStringUtils.random(count, 0, 0, false, false, null, random);
        Assert.assertEquals(1, randomString.length());
        char firstChar = randomString.charAt(0);
        if (Character.isSurrogate(firstChar)) {
            char secondChar = RandomStringUtils.random(1, 0, 0, false, false, null, random).charAt(0);
            randomString = RandomStringUtils.random(count + 2, firstChar, secondChar, false, false, null, random);
            Assert.assertEquals(count + 2, randomString.length());
            Assert.assertTrue(Character.isHighSurrogate(randomString.charAt(0)) && Character.isLowSurrogate(randomString.charAt(1)) && Character.isAlphabetic(randomString.charAt(2)));
        } else {
            Assert.fail("Expected surrogate pair, but got: " + randomString);
        }
    }

    @Test
    public void testRandomWithEmptyChars() {
        int count = 5;
        String emptyChars = "";
        try {
            RandomStringUtils.random(count, emptyChars.toCharArray());
            Assert.fail("Expected IllegalArgumentException, but got: " + RandomStringUtils.random(count, emptyChars.toCharArray()));
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}