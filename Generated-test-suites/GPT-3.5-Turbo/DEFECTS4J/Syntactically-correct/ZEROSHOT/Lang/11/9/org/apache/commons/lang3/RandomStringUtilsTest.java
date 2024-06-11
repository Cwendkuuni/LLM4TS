import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {
  
  @Test
  public void testRandom() {
    int count = 10;
    String random = RandomStringUtils.random(count);
    
    assertEquals(count, random.length());
  }
  
  @Test
  public void testRandomAscii() {
    int count = 10;
    String randomAscii = RandomStringUtils.randomAscii(count);
    
    assertEquals(count, randomAscii.length());
    
    for (char c : randomAscii.toCharArray()) {
      assertTrue(c >= 32 && c <= 127);
    }
  }
  
  @Test
  public void testRandomAlphabetic() {
    int count = 10;
    String randomAlphabetic = RandomStringUtils.randomAlphabetic(count);
    
    assertEquals(count, randomAlphabetic.length());
    
    for (char c : randomAlphabetic.toCharArray()) {
      assertTrue(Character.isAlphabetic(c));
    }
  }
  
  @Test
  public void testRandomAlphanumeric() {
    int count = 10;
    String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(count);
    
    assertEquals(count, randomAlphanumeric.length());
    
    for (char c : randomAlphanumeric.toCharArray()) {
      assertTrue(Character.isLetterOrDigit(c));
    }
  }
  
  @Test
  public void testRandomNumeric() {
    int count = 10;
    String randomNumeric = RandomStringUtils.randomNumeric(count);
    
    assertEquals(count, randomNumeric.length());
    
    for (char c : randomNumeric.toCharArray()) {
      assertTrue(Character.isDigit(c));
    }
  }
  
  @Test
  public void testRandomLettersAndNumbers() {
    int count = 10;
    String randomLettersAndNumbers = RandomStringUtils.random(count, true, true);
    
    assertEquals(count, randomLettersAndNumbers.length());
    
    for (char c : randomLettersAndNumbers.toCharArray()) {
      assertTrue(Character.isLetterOrDigit(c));
    }
  }
  
  @Test
  public void testRandomRange() {
    int count = 10;
    int start = 32;
    int end = 127;
    String randomRange = RandomStringUtils.random(count, start, end, false, false);
    
    assertEquals(count, randomRange.length());
    
    for (char c : randomRange.toCharArray()) {
      assertTrue(c >= start && c <= end);
    }
  }
  
  @Test
  public void testRandomChars() {
    int count = 10;
    char[] chars = "abcdef".toCharArray();
    String randomChars = RandomStringUtils.random(count, chars);
    
    assertEquals(count, randomChars.length());
    
    for (char c : randomChars.toCharArray()) {
      boolean found = false;
      for (char ch : chars) {
        if (ch == c) {
          found = true;
          break;
        }
      }
      assertTrue(found);
    }
  }
  
  @Test
  public void testRandomString() {
    int count = 10;
    String chars = "abcdef";
    String randomString = RandomStringUtils.random(count, chars);
    
    assertEquals(count, randomString.length());
    
    for (char c : randomString.toCharArray()) {
      assertTrue(chars.contains(String.valueOf(c)));
    }
  }
  
  @Test
  public void testRandomWithRandomObject() {
    int count = 10;
    char[] chars = "abcdef".toCharArray();
    Random random = new Random();
    String randomChars = RandomStringUtils.random(count, chars, random);
    
    assertEquals(count, randomChars.length());
    
    for (char c : randomChars.toCharArray()) {
      boolean found = false;
      for (char ch : chars) {
        if (ch == c) {
          found = true;
          break;
        }
      }
      assertTrue(found);
    }
  }
  
  @Test
  public void testRandomWithInvalidCount() {
    try {
      String random = RandomStringUtils.random(-1);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      // exception expected
    }
  }
  
  @Test
  public void testRandomWithEmptyChars() {
    try {
      String random = RandomStringUtils.random(10, 32, 127, false, false, new char[0]);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      // exception expected
    }
  }
  
  @Test
  public void testRandomWithInvalidRange() {
    try {
      String random = RandomStringUtils.random(10, 127, 32, false, false);
      fail("Expected ArrayIndexOutOfBoundsException to be thrown");
    } catch (ArrayIndexOutOfBoundsException e) {
      // exception expected
    }
  }

}