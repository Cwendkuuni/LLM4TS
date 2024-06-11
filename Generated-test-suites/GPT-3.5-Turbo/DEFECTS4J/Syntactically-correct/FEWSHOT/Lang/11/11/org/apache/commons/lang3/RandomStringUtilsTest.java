package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RandomStringUtilsTest {

  @Test
  public void testRandom() {
    int count = 10;
    String randomString = RandomStringUtils.random(count);
    assertEquals(count, randomString.length());
  }

  @Test
  public void testRandomAscii() {
    int count = 10;
    String randomString = RandomStringUtils.randomAscii(count);
    assertEquals(count, randomString.length());
    assertTrue(randomString.matches("[\\x20-\\x7E]+"));
  }

  @Test
  public void testRandomAlphabetic() {
    int count = 10;
    String randomString = RandomStringUtils.randomAlphabetic(count);
    assertEquals(count, randomString.length());
    assertTrue(randomString.matches("[A-Za-z]+"));
  }

  @Test
  public void testRandomAlphanumeric() {
    int count = 10;
    String randomString = RandomStringUtils.randomAlphanumeric(count);
    assertEquals(count, randomString.length());
    assertTrue(randomString.matches("[A-Za-z0-9]+"));
  }

  @Test
  public void testRandomNumeric() {
    int count = 10;
    String randomString = RandomStringUtils.randomNumeric(count);
    assertEquals(count, randomString.length());
    assertTrue(randomString.matches("[0-9]+"));
  }

  @Test
  public void testRandomWithCustomChars() {
    int count = 10;
    char[] chars = {'a', 'b', 'c'};
    String randomString = RandomStringUtils.random(count, chars);
    assertEquals(count, randomString.length());
    assertTrue(randomString.matches("[abc]+"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomNegativeCount() {
    int count = -10;
    RandomStringUtils.random(count);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomEmptyChars() {
    int count = 10;
    char[] chars = {};
    RandomStringUtils.random(count, chars);
  }

}