import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;

public class RandomStringUtilsTest {

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
  public void testRandomWithParams() {
    int count = 10;
    int start = 32;
    int end = 127;
    boolean letters = true;
    boolean numbers = true;
    char[] chars = null;

    String result = RandomStringUtils.random(count, start, end, letters, numbers, chars);
    Assert.assertEquals(count, result.length());
    for (char c : result.toCharArray()) {
      Assert.assertTrue((int) c >= start && (int) c <= end);
    }
  }

  @Test
  public void testRandomWithCustomChars() {
    int count = 10;
    char[] chars = "abc123".toCharArray();

    String result = RandomStringUtils.random(count, chars);
    Assert.assertEquals(count, result.length());
    for (char c : result.toCharArray()) {
      Assert.assertTrue(Arrays.asList(chars).contains(c));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomWithNegativeCount() {
    int count = -10;
    RandomStringUtils.random(count);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomWithEmptyChars() {
    int count = 10;
    char[] chars = new char[0];
    RandomStringUtils.random(count, chars);
  }

  @Test
  public void testRandomWithSpecialChars() {
    int count = 10;
    String specialChars = "!@#$%^&*()_+=-{}[]|':;<>?,.";
    char[] chars = specialChars.toCharArray();

    String result = RandomStringUtils.random(count, chars);
    Assert.assertEquals(count, result.length());
    for (char c : result.toCharArray()) {
      Assert.assertTrue(specialChars.contains(String.valueOf(c)));
    }
  }

  @Test
  public void testRandomWithNullChars() {
    int count = 10;
    String result = RandomStringUtils.random(count, (char[]) null);
    Assert.assertEquals(count, result.length());
  }

  @Test
  public void testRandomWithNullCharsAndNullRandom() {
    int count = 10;
    Random random = null;
    String result = RandomStringUtils.random(count, 0, 0, false, false, null, random);
    Assert.assertEquals(count, result.length());
  }

  @Test
  public void testRandomWithAllParamsNull() {
    int count = 10;
    String result = RandomStringUtils.random(count, null);
    Assert.assertEquals(count, result.length());
  }
}