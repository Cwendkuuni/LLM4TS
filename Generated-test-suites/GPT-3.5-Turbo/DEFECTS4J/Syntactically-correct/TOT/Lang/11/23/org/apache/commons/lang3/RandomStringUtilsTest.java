import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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
      assertTrue((int) c >= 32 && (int) c <= 127);
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
  public void testRandomWithInvalidCount() {
    try {
      RandomStringUtils.random(-1);
    } catch (IllegalArgumentException e) {
      assertEquals("Requested random string length -1 is less than 0.", e.getMessage());
    }
  }

  @Test
  public void testRandomWithEmptyCharsArray() {
    try {
      RandomStringUtils.random(10, new char[] {});
    } catch (IllegalArgumentException e) {
      assertEquals("The chars array must not be empty", e.getMessage());
    }
  }

}