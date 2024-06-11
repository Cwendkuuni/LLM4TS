import static org.junit.Assert.*;
import java.util.Arrays;
import org.junit.Test;

public class RandomStringUtilsTest {

  @Test
  public void testRandom() {
    String randomString = RandomStringUtils.random(10);
    assertEquals(10, randomString.length());
    assertTrue(isValidRandomString(randomString.toCharArray()));
  }

  @Test
  public void testRandomAscii() {
    String randomString = RandomStringUtils.randomAscii(10);
    assertEquals(10, randomString.length());
    assertTrue(onlyAsciiCharacters(randomString.toCharArray()));
  }

  @Test
  public void testRandomAlphabetic() {
    String randomString = RandomStringUtils.randomAlphabetic(10);
    assertEquals(10, randomString.length());
    assertTrue(onlyAlphabeticCharacters(randomString.toCharArray()));
  }

  @Test
  public void testRandomAlphanumeric() {
    String randomString = RandomStringUtils.randomAlphanumeric(10);
    assertEquals(10, randomString.length());
    assertTrue(onlyAlphanumericCharacters(randomString.toCharArray()));
  }

  @Test
  public void testRandomNumeric() {
    String randomString = RandomStringUtils.randomNumeric(10);
    assertEquals(10, randomString.length());
    assertTrue(onlyNumericCharacters(randomString.toCharArray()));
  }

  @Test
  public void testRandomWithLettersAndNumbers() {
    String randomString = RandomStringUtils.random(10, true, true);
    assertEquals(10, randomString.length());
    assertTrue(isValidRandomString(randomString.toCharArray()));
    assertTrue(containsLetters(randomString.toCharArray()));
    assertTrue(containsNumbers(randomString.toCharArray()));
  }

  @Test
  public void testRandomWithStartAndEnd() {
    char[] chars = {'a', 'b', 'c', 'd', 'e'};
    String randomString = RandomStringUtils.random(10, 1, 4, false, false, chars);
    assertEquals(10, randomString.length());
    assertTrue(isValidRandomString(randomString.toCharArray()));
    assertTrue(onlySpecifiedChars(randomString.toCharArray(), chars));
  }

  @Test
  public void testRandomWithChars() {
    char[] chars = {'a', 'b', 'c', 'd', 'e'};
    String randomString = RandomStringUtils.random(10, chars);
    assertEquals(10, randomString.length());
    assertTrue(isValidRandomString(randomString.toCharArray()));
    assertTrue(onlySpecifiedChars(randomString.toCharArray(), chars));
  }

  @Test
  public void testRandomWithCharsAndRandom() {
    char[] chars = {'a', 'b', 'c', 'd', 'e'};
    String randomString = RandomStringUtils.random(10, chars, new Random(5));
    assertEquals(10, randomString.length());
    assertTrue(isValidRandomString(randomString.toCharArray()));
    assertTrue(onlySpecifiedChars(randomString.toCharArray(), chars));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomWithNegativeCount() {
    RandomStringUtils.random(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomWithEmptyCharsArray() {
    RandomStringUtils.random(10, new char[0]);
  }

  @Test
  public void testRandomWithString() {
    String chars = "abcde";
    String randomString = RandomStringUtils.random(10, chars);
    assertEquals(10, randomString.length());
    assertTrue(isValidRandomString(randomString.toCharArray()));
    assertTrue(onlySpecifiedChars(randomString.toCharArray(), chars.toCharArray()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomWithEmptyStringChars() {
    RandomStringUtils.random(10, "");
  }

  private boolean isValidRandomString(char[] chars) {
    return Arrays.stream(chars).allMatch(ch -> Character.isLetterOrDigit(ch));
  }

  private boolean onlyAsciiCharacters(char[] chars) {
    return Arrays.stream(chars).allMatch(ch -> ch >= 32 && ch <= 126);
  }

  private boolean onlyAlphabeticCharacters(char[] chars) {
    return Arrays.stream(chars).allMatch(ch -> Character.isLetter(ch));
  }

  private boolean onlyAlphanumericCharacters(char[] chars) {
    return Arrays.stream(chars).allMatch(ch -> Character.isLetterOrDigit(ch));
  }

  private boolean onlyNumericCharacters(char[] chars) {
    return Arrays.stream(chars).allMatch(ch -> Character.isDigit(ch));
  }

  private boolean containsLetters(char[] chars) {
    return Arrays.stream(chars).anyMatch(ch -> Character.isLetter(ch));
  }

  private boolean containsNumbers(char[] chars) {
    return Arrays.stream(chars).anyMatch(ch -> Character.isDigit(ch));
  }

  private boolean onlySpecifiedChars(char[] chars, char[] specifiedChars) {
    return Arrays.stream(chars).allMatch(ch -> Arrays.binarySearch(specifiedChars, ch) >= 0);
  }
}