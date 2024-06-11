package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class RandomStringUtilsTest {
  
  @Test
  public void testRandom() {
    assertEquals("", RandomStringUtils.random(0));
    assertEquals(10, RandomStringUtils.random(10).length());
  }
  
  @Test
  public void testRandomAscii() {
    assertEquals("", RandomStringUtils.randomAscii(0));
    assertEquals(10, RandomStringUtils.randomAscii(10).length());
  }
  
  @Test
  public void testRandomAlphabetic() {
    assertEquals("", RandomStringUtils.randomAlphabetic(0));
    assertEquals(10, RandomStringUtils.randomAlphabetic(10).length());
  }
  
  @Test
  public void testRandomAlphanumeric() {
    assertEquals("", RandomStringUtils.randomAlphanumeric(0));
    assertEquals(10, RandomStringUtils.randomAlphanumeric(10).length());
  }
  
  @Test
  public void testRandomNumeric() {
    assertEquals("", RandomStringUtils.randomNumeric(0));
    assertEquals(10, RandomStringUtils.randomNumeric(10).length());
  }
  
  @Test
  public void testRandomWithSet() {
    assertEquals("", RandomStringUtils.random(0, "abc"));
    assertEquals(10, RandomStringUtils.random(10, "abc").length());
  }
  
  @Test
  public void testRandomWithCharArray() {
    assertEquals("", RandomStringUtils.random(0, 'a', 'b', 'c'));
    assertEquals(10, RandomStringUtils.random(10, 'a', 'b', 'c').length());
  }
  
}