package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {
  
  @Test
  public void testRandom() {
    int count = 10;
    String result = RandomStringUtils.random(count);
    assertEquals(count, result.length());
  }
  
  @Test
  public void testRandomAscii() {
    int count = 10;
    String result = RandomStringUtils.randomAscii(count);
    assertEquals(count, result.length());
    
    for(char c : result.toCharArray()) {
      assertTrue(c >= 32 && c <= 127);
    }
  }
  
  @Test
  public void testRandomAlphabetic() {
    int count = 10;
    String result = RandomStringUtils.randomAlphabetic(count);
    assertEquals(count, result.length());
    
    for(char c : result.toCharArray()) {
      assertTrue(Character.isLetter(c));
    }
  }
  
  @Test
  public void testRandomAlphanumeric() {
    int count = 10;
    String result = RandomStringUtils.randomAlphanumeric(count);
    assertEquals(count, result.length());
    
    for(char c : result.toCharArray()) {
      assertTrue(Character.isLetterOrDigit(c));
    }
  }
  
  @Test
  public void testRandomNumeric() {
    int count = 10;
    String result = RandomStringUtils.randomNumeric(count);
    assertEquals(count, result.length());
    
    for(char c : result.toCharArray()) {
      assertTrue(Character.isDigit(c));
    }
  }
  
  @Test
  public void testRandomWithLettersAndNumbers() {
    int count = 10;
    String result = RandomStringUtils.random(count, true, true);
    assertEquals(count, result.length());
    
    for(char c : result.toCharArray()) {
      assertTrue(Character.isLetterOrDigit(c));
    }
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testRandomWithNegativeCount() {
    int count = -1;
    RandomStringUtils.random(count);
  }
  
  @Test
  public void testRandomWithEmptyCharsArray() {
    int count = 10;
    char[] chars = new char[0];
    String result = RandomStringUtils.random(count, chars);
    assertEquals(count, result.length());
    
    // no characters should be generated
    assertEquals("", result);
  }
}