import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {

  @Test
  public void testRandom() {
    String randomString = RandomStringUtils.random(10);
    Assert.assertNotNull(randomString);
    Assert.assertEquals(10, randomString.length());
  }

  @Test
  public void testRandomAscii() {
    String randomAsciiString = RandomStringUtils.randomAscii(10);
    Assert.assertNotNull(randomAsciiString);
    Assert.assertEquals(10, randomAsciiString.length());
    for (char c : randomAsciiString.toCharArray()) {
      Assert.assertTrue(c >= 32 && c <= 127);
    }
  }

  @Test
  public void testRandomAlphabetic() {
    String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
    Assert.assertNotNull(randomAlphabeticString);
    Assert.assertEquals(10, randomAlphabeticString.length());
    for (char c : randomAlphabeticString.toCharArray()) {
      Assert.assertTrue(Character.isAlphabetic(c));
    }
  }

  @Test
  public void testRandomAlphanumeric() {
    String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
    Assert.assertNotNull(randomAlphanumericString);
    Assert.assertEquals(10, randomAlphanumericString.length());
    for (char c : randomAlphanumericString.toCharArray()) {
      Assert.assertTrue(Character.isLetterOrDigit(c));
    }
  }

  @Test
  public void testRandomNumeric() {
    String randomNumericString = RandomStringUtils.randomNumeric(10);
    Assert.assertNotNull(randomNumericString);
    Assert.assertEquals(10, randomNumericString.length());
    for (char c : randomNumericString.toCharArray()) {
      Assert.assertTrue(Character.isDigit(c));
    }
  }

  @Test
  public void testRandomWithChars() {
    String randomString = RandomStringUtils.random(10, "abc123");
    Assert.assertNotNull(randomString);
    Assert.assertEquals(10, randomString.length());
    for (char c : randomString.toCharArray()) {
      Assert.assertTrue(c == 'a' || c == 'b' || c == 'c' || c == '1' || c == '2' || c == '3');
    }
  }

  @Test
  public void testRandomWithCharsAndRange() {
    String randomString = RandomStringUtils.random(10, 'a', 'c', true, true);
    Assert.assertNotNull(randomString);
    Assert.assertEquals(10, randomString.length());
    for (char c : randomString.toCharArray()) {
      Assert.assertTrue(c == 'a' || c == 'b' || c == 'c');
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomNegativeCount() {
    RandomStringUtils.random(-10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomEmptyChars() {
    RandomStringUtils.random(10, "");
  }

  @Test
  public void testRandomNullChars() {
    String randomString = RandomStringUtils.random(10, null);
    Assert.assertNotNull(randomString);
    Assert.assertEquals(10, randomString.length());
  }

  @Test
  public void testRandomZeroCount() {
    String randomString = RandomStringUtils.random(0);
    Assert.assertNotNull(randomString);
    Assert.assertEquals(0, randomString.length());
  }

  @Test
  public void testRandomWithEmptyCharsAndRange() {
    String randomString = RandomStringUtils.random(10, 0, 0, false, false, null, RandomStringUtils.RANDOM);
    Assert.assertNotNull(randomString);
    Assert.assertEquals(10, randomString.length());
  }
}