package org.apache.commons.lang3;
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
    String randomString = RandomStringUtils.randomAscii(10);
    for (char c : randomString.toCharArray()) {
      assertTrue((int) c >= 32 && (int) c <= 126);
    }
  }

  @Test
  public void testRandomAlphabetic() {
    String randomString = RandomStringUtils.randomAlphabetic(10);
    assertTrue(randomString.matches("[a-zA-Z]+"));
  }
  
  @Test
  public void testRandomAlphanumeric() {
    String randomString = RandomStringUtils.randomAlphanumeric(10);
    assertTrue(randomString.matches("[a-zA-Z0-9]+"));
  }
  
  @Test
  public void testRandomNumeric() {
    String randomString = RandomStringUtils.randomNumeric(10);
    assertTrue(randomString.matches("[0-9]+"));
  }

  @Test
  public void testRandomWithStartAndEndAndLetters() {
    String randomString = RandomStringUtils.random(10, 'a', 'z', true, false, null, null);
    for (char c : randomString.toCharArray()) {
      assertTrue((int) c >= 'a' && (int) c <= 'z');
    }
  }

  @Test
  public void testRandomWithStartAndEndAndNumbers() {
    String randomString = RandomStringUtils.random(10, '0', '9', false, true, null, null);
    for (char c : randomString.toCharArray()) {
      assertTrue((int) c >= '0' && (int) c <= '9');
    }
  }

  @Test
  public void testRandomWithChars() {
    String randomString = RandomStringUtils.random(10, new char[]{'a', 'b', 'c'});
    assertTrue(randomString.matches("[abc]+"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomWithNegativeCount() {
    RandomStringUtils.random(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomWithEmptyCharsArray() {
    RandomStringUtils.random(10, new char[]{});
  }

  @Test
  public void testRandomNullChars() {
    String randomString = RandomStringUtils.random(10, (char[]) null);
    assertNotNull(randomString);
    assertEquals(10, randomString.length());
  }
}