import static org.junit.Assert.*;
import org.junit.Test;

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
      assertTrue(c >= 32 && c <= 126);
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
  public void testRandomCustomChars() {
    char[] customChars = {'a', 'b', 'c'};
    String result = RandomStringUtils.random(5, customChars);
    assertEquals(5, result.length());
    for (char c : result.toCharArray()) {
      assertTrue(c == 'a' || c == 'b' || c == 'c');
    }
  }
  
  @Test
  public void testRandomCustomRange() {
    String result = RandomStringUtils.random(5, 65, 90, true, false);
    assertEquals(5, result.length());
    for (char c : result.toCharArray()) {
      assertTrue(c >= 65 && c <= 90);
    }
  }
  
  @Test
  public void testRandomWithSeed() {
    long seed = 12345L;
    Random random = new Random(seed);
    String result1 = RandomStringUtils.random(5, random);
    random = new Random(seed);
    String result2 = RandomStringUtils.random(5, random);
    assertEquals(result1, result2);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testRandomNegativeCount() {
    RandomStringUtils.random(-5);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testRandomEmptyChars() {
    char[] emptyChars = {};
    RandomStringUtils.random(5, emptyChars);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testRandomNullChars() {
    RandomStringUtils.random(5, (char[]) null);
  }
  
}