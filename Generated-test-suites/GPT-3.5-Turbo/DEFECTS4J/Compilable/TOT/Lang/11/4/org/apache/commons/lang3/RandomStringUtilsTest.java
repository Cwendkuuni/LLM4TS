package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {
  
  @Test
  public void testRandomCountPositive() {
    String randomString = RandomStringUtils.random(5);
    assertEquals(5, randomString.length());
  }
  
  @Test
  public void testRandomCountZero() {
    String randomString = RandomStringUtils.random(0);
    assertEquals(0, randomString.length());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testRandomCountNegative() {
    RandomStringUtils.random(-5);
  }
  
  @Test
  public void testRandomAsciiCountPositive() {
    String randomString = RandomStringUtils.randomAscii(5);
    assertEquals(5, randomString.length());
  }
  
  @Test
  public void testRandomAsciiCountZero() {
    String randomString = RandomStringUtils.randomAscii(0);
    assertEquals(0, randomString.length());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testRandomAsciiCountNegative() {
    RandomStringUtils.randomAscii(-5);
  }
  
  @Test
  public void testRandomAlphabeticCountPositive() {
    String randomString = RandomStringUtils.randomAlphabetic(5);
    assertEquals(5, randomString.length());
  }
  
  @Test
  public void testRandomAlphabeticCountZero() {
    String randomString = RandomStringUtils.randomAlphabetic(0);
    assertEquals(0, randomString.length());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testRandomAlphabeticCountNegative() {
    RandomStringUtils.randomAlphabetic(-5);
  }
  
  @Test
  public void testRandomAlphanumericCountPositive() {
    String randomString = RandomStringUtils.randomAlphanumeric(5);
    assertEquals(5, randomString.length());
  }
  
  @Test
  public void testRandomAlphanumericCountZero() {
    String randomString = RandomStringUtils.randomAlphanumeric(0);
    assertEquals(0, randomString.length());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testRandomAlphanumericCountNegative() {
    RandomStringUtils.randomAlphanumeric(-5);
  }
  
  @Test
  public void testRandomNumericCountPositive() {
    String randomString = RandomStringUtils.randomNumeric(5);
    assertEquals(5, randomString.length());
  }
  
  @Test
  public void testRandomNumericCountZero() {
    String randomString = RandomStringUtils.randomNumeric(0);
    assertEquals(0, randomString.length());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testRandomNumericCountNegative() {
    RandomStringUtils.randomNumeric(-5);
  }
  
  @Test
  public void testRandomCountLettersAndNumbers() {
    String randomString = RandomStringUtils.random(10, true, true);
    assertEquals(10, randomString.length());
    assertTrue(randomString.matches("[a-zA-Z0-9]+"));
  }
  
  @Test
  public void testRandomCountNoLettersOrNumbers() {
    String randomString = RandomStringUtils.random(10, false, false);
    assertEquals(10, randomString.length());
    assertTrue(randomString.matches("[^a-zA-Z0-9]+"));
  }
  
  @Test
  public void testRandomCountLettersOnly() {
    String randomString = RandomStringUtils.random(10, true, false);
    assertEquals(10, randomString.length());
    assertTrue(randomString.matches("[a-zA-Z]+"));
  }
  
  @Test
  public void testRandomCountNumbersOnly() {
    String randomString = RandomStringUtils.random(10, false, true);
    assertEquals(10, randomString.length());
    assertTrue(randomString.matches("[0-9]+"));
  }
  
  @Test
  public void testRandomFromCharsCountPositive() {
    String randomString = RandomStringUtils.random(5, new char[]{'a', 'b', 'c'});
    assertEquals(5, randomString.length());
    assertTrue(randomString.matches("[abc]+"));
  }
  
  @Test
  public void testRandomFromCharsCountZero() {
    String randomString = RandomStringUtils.random(0, new char[]{'a', 'b', 'c'});
    assertEquals(0, randomString.length());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testRandomFromCharsCountNegative() {
    RandomStringUtils.random(-5, new char[]{'a', 'b', 'c'});
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testRandomFromCharsEmptyArray() {
    RandomStringUtils.random(5, new char[]{});
  }

}