package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

  @Test
  public void testRandom() {
    String randomString = RandomStringUtils.random(5);
    assertEquals(5, randomString.length());
  }

  @Test
  public void testRandomAscii() {
    String randomAsciiString = RandomStringUtils.randomAscii(5);
    assertEquals(5, randomAsciiString.length());
    assertTrue(randomAsciiString.matches("[\\x20-\\x7F]+"));
  }

  @Test
  public void testRandomAlphabetic() {
    String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
    assertEquals(5, randomAlphabeticString.length());
    assertTrue(randomAlphabeticString.matches("[a-zA-Z]+"));
  }

  @Test
  public void testRandomAlphanumeric() {
    String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
    assertEquals(5, randomAlphanumericString.length());
    assertTrue(randomAlphanumericString.matches("[a-zA-Z0-9]+"));
  }

  @Test
  public void testRandomNumeric() {
    String randomNumericString = RandomStringUtils.randomNumeric(5);
    assertEquals(5, randomNumericString.length());
    assertTrue(randomNumericString.matches("[0-9]+"));
  }

  @Test
  public void testRandomWithLettersAndNumbers() {
    String randomString = RandomStringUtils.random(5, true, true);
    assertEquals(5, randomString.length());
    assertTrue(randomString.matches("[a-zA-Z0-9]+"));
  }

  @Test
  public void testRandomWithStartAndEnd() {
    String randomString = RandomStringUtils.random(5, 65, 90, true, false);
    assertEquals(5, randomString.length());
    assertTrue(randomString.matches("[A-Z]+"));
  }

  @Test
  public void testRandomWithChars() {
    String randomString = RandomStringUtils.random(5, new char[]{'a', 'b', 'c'});
    assertEquals(5, randomString.length());
    assertTrue(randomString.matches("[abc]+"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomWithNegativeCount() {
    RandomStringUtils.random(-5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomWithEmptyCharsArray() {
    RandomStringUtils.random(5, new char[]{});
  }

}