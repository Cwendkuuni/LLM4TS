package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

  @Test
  public void testRandom() {
    String result = RandomStringUtils.random(5);
    assertEquals(5, result.length());
  }

  @Test
  public void testRandomAscii() {
    String result = RandomStringUtils.randomAscii(5);
    assertEquals(5, result.length());
    for (char c : result.toCharArray()) {
      assertTrue(c >= 32 && c <= 127);
    }
  }

  @Test
  public void testRandomAlphabetic() {
    String result = RandomStringUtils.randomAlphabetic(5);
    assertEquals(5, result.length());
    for (char c : result.toCharArray()) {
      assertTrue(Character.isLetter(c));
    }
  }

  @Test
  public void testRandomAlphanumeric() {
    String result = RandomStringUtils.randomAlphanumeric(5);
    assertEquals(5, result.length());
    for (char c : result.toCharArray()) {
      assertTrue(Character.isLetterOrDigit(c));
    }
  }

  @Test
  public void testRandomNumeric() {
    String result = RandomStringUtils.randomNumeric(5);
    assertEquals(5, result.length());
    for (char c : result.toCharArray()) {
      assertTrue(Character.isDigit(c));
    }
  }

  @Test
  public void testRandomWithOptions() {
    String result = RandomStringUtils.random(5, true, true);
    assertEquals(5, result.length());
    for (char c : result.toCharArray()) {
      assertTrue(Character.isLetterOrDigit(c));
    }
  }

  @Test
  public void testRandomWithCharRange() {
    String result = RandomStringUtils.random(5, 32, 127, false, false);
    assertEquals(5, result.length());
    for (char c : result.toCharArray()) {
      assertTrue(c >= 32 && c <= 127);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomNegativeCount() {
    RandomStringUtils.random(-5);
  }

  @Test
  public void testRandomEmptyChars() {
    String result = RandomStringUtils.random(5, new char[0]);
    assertEquals(5, result.length());
  }

  @Test
  public void testRandomWithChars() {
    String result = RandomStringUtils.random(5, new char[]{'a', 'b', 'c'});
    assertEquals(5, result.length());
    for (char c : result.toCharArray()) {
      assertTrue(c == 'a' || c == 'b' || c == 'c');
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomWithEmptyString() {
    RandomStringUtils.random(5, "");
  }

  @Test
  public void testRandomWithString() {
    String result = RandomStringUtils.random(5, "abc");
    assertEquals(5, result.length());
    for (char c : result.toCharArray()) {
      assertTrue(c == 'a' || c == 'b' || c == 'c');
    }
  }

}