package org.apache.commons.lang3;
import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue((int) c >= 32 && (int) c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithChars() {
        char[] chars = {'a', 'b', 'c'};
        String randomString = RandomStringUtils.random(5, chars);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(c == 'a' || c == 'b' || c == 'c');
        }
    }

    @Test
    public void testRandomWithRange() {
        String randomString = RandomStringUtils.random(5, 'A', 'Z', true, false);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue((int) c >= 65 && (int) c <= 90);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphabeticWithNegativeCount() {
        RandomStringUtils.randomAlphabetic(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphanumericWithNegativeCount() {
        RandomStringUtils.randomAlphanumeric(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNumericWithNegativeCount() {
        RandomStringUtils.randomNumeric(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyChars() {
        RandomStringUtils.random(5, new char[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNullChars() {
        RandomStringUtils.random(5, (char[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidRange() {
        RandomStringUtils.random(5, 'Z', 'A', true, false);
    }

    @Test
    public void testRandomWithNullString() {
        String randomString = RandomStringUtils.random(5, (String) null);
        Assert.assertNotNull(randomString);
        Assert.assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomWithEmptyString() {
        String randomString = RandomStringUtils.random(5, "");
        Assert.assertNotNull(randomString);
        Assert.assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomWithCustomString() {
        String randomString = RandomStringUtils.random(5, "abc");
        Assert.assertNotNull(randomString);
        Assert.assertEquals(5, randomString.length());
        for (char c : randomString.toCharArray()) {
            Assert.assertTrue(c == 'a' || c == 'b' || c == 'c');
        }
    }
}