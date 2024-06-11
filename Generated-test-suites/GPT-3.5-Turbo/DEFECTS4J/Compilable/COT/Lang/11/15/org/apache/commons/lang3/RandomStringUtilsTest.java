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
    assertEquals(10, randomString.length());
  }
  
  @Test
  public void testRandomAlphabetic() {
    String randomString = RandomStringUtils.randomAlphabetic(10);
    assertEquals(10, randomString.length());
    assertTrue(randomString.matches("[a-zA-Z]+"));
  }
  
  @Test
  public void testRandomAlphanumeric() {
    String randomString = RandomStringUtils.randomAlphanumeric(10);
    assertEquals(10, randomString.length());
    assertTrue(randomString.matches("[a-zA-Z0-9]+"));
  }
  
  @Test
  public void testRandomNumeric() {
    String randomString = RandomStringUtils.randomNumeric(10);
    assertEquals(10, randomString.length());
    assertTrue(randomString.matches("[0-9]+"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomInvalidLength() {
    RandomStringUtils.random(-1);
  }

  @Test
  public void testRandomPositiveLength() {
    assertTrue(RandomStringUtils.random(1).length() > 0);
  }
  
  // Additional test cases for testing edge cases
  
  @Test
  public void testRandomEmptyString() {
    assertEquals("", RandomStringUtils.random(0));
  }
  
  @Test
  public void testRandomAsciiEmptyString() {
    assertEquals("", RandomStringUtils.randomAscii(0));
  }
  
  @Test
  public void testRandomAlphabeticEmptyString() {
    assertEquals("", RandomStringUtils.randomAlphabetic(0));
  }
  
  @Test
  public void testRandomAlphanumericEmptyString() {
    assertEquals("", RandomStringUtils.randomAlphanumeric(0));
  }
  
  @Test
  public void testRandomNumericEmptyString() {
    assertEquals("", RandomStringUtils.randomNumeric(0));
  }

}
