import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

  @Test
  public void testRandom() {
    String randomString = RandomStringUtils.random(10);
    assertEquals(10, randomString.length());
  }

  @Test
  public void testRandomAscii() {
    String randomAsciiString = RandomStringUtils.randomAscii(10);
    assertEquals(10, randomAsciiString.length());
    for (char c : randomAsciiString.toCharArray()) {
      assertTrue(c >= 32 && c <= 127);
    }
  }

  @Test
  public void testRandomAlphabetic() {
    String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
    assertEquals(10, randomAlphabeticString.length());
    for (char c : randomAlphabeticString.toCharArray()) {
      assertTrue(Character.isAlphabetic(c));
    }
  }

  @Test
  public void testRandomAlphanumeric() {
    String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
    assertEquals(10, randomAlphanumericString.length());
    for (char c : randomAlphanumericString.toCharArray()) {
      assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
    }
  }

  @Test
  public void testRandomNumeric() {
    String randomNumericString = RandomStringUtils.randomNumeric(10);
    assertEquals(10, randomNumericString.length());
    for (char c : randomNumericString.toCharArray()) {
      assertTrue(Character.isDigit(c));
    }
  }

  @Test
  public void testRandomWithLettersAndNumbers() {
    String randomString = RandomStringUtils.random(10, true, true);
    assertEquals(10, randomString.length());
    for (char c : randomString.toCharArray()) {
      assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
    }
  }

  @Test
  public void testRandomWithCustomChars() {
    char[] customChars = {'a', 'b', 'c'};
    String randomString = RandomStringUtils.random(10, customChars);
    assertEquals(10, randomString.length());
    for (char c : randomString.toCharArray()) {
      assertTrue(c == 'a' || c == 'b' || c == 'c');
    }
  }

  @Test
  public void testRandomWithCustomCharsAndNumbers() {
    char[] customChars = {'a', 'b', 'c'};
    String randomString = RandomStringUtils.random(10, 0, customChars.length, false, true, customChars);
    assertEquals(10, randomString.length());
    for (char c : randomString.toCharArray()) {
      assertTrue(Character.isDigit(c) || c == 'a' || c == 'b' || c == 'c');
    }
  }

  @Test
  public void testRandomWithRandomObject() {
    char[] customChars = {'a', 'b', 'c'};
    String randomString = RandomStringUtils.random(10, 0, customChars.length, false, true, customChars, new Random(1));
    assertEquals(10, randomString.length());
    for (char c : randomString.toCharArray()) {
      assertTrue(Character.isDigit(c) || c == 'a' || c == 'b' || c == 'c');
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomWithNegativeCount() {
    RandomStringUtils.random(-10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomWithEmptyCharsArray() {
    char[] emptyCharsArray = {};
    RandomStringUtils.random(10, emptyCharsArray);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomWithEmptyCharsString() {
    RandomStringUtils.random(10, "");
  }
}