// Test case for clean method
import org.junit.Test;
import org.junit.Assert.*;

public class SoundexUtilsTest {

  @Test
  public void testClean() {
    String input = "A Bc 123 D! E fG";
    String expected = "A E";
    String result = SoundexUtils.clean(input);
    assertEquals(expected, result);
  }

  // Basic test for empty string
  @Test
  public void testCleanEmptyString() {
    String input = "";
    String expected = "";
    String result = SoundexUtils.clean(input);
    assertEquals(expected, result);
  }

  // Basic test for null input
  @Test(expected = NullPointerException.class)
  public void testCleanNullInput() {
    String input = null;
    SoundexUtils.clean(input);
  }

  

  // Test for strings with only letters
  @Test
  public void testCleanStringWithOnlyLetters() {
    String input = "abcdef";
    String expected = "ABCDEF";
    String result = SoundexUtils.clean(input);
    assertEquals(expected, result);
  }

  // Test for strings with only digits
  @Test
  public void testCleanStringWithOnlyDigits() {
    String input = "123456";
    String expected = "";
    String result = SoundexUtils.clean(input);
    assertEquals(expected, result);
  }

  // Test for strings with symbols
  @Test
  public void testCleanStringWithSymbols() {
    String input = "A@B%C&D*E#F_G!";
    String expected = "A";
    String result = SoundexUtils.clean(input);
    assertEquals(expected, result);
  }

  // Test for strings with different lengths
  @Test
  public void testCleanStringsWithDifferentLengths() {
    String input1 = "abcd";
    String input2 = "efghijkl";
    String expected = "ABCD";
    String result1 = SoundexUtils.clean(input1);
    String result2 = SoundexUtils.clean(input2);
    assertEquals(expected, result1);
    assertEquals(expected, result2);
  }

  // Test for strings with whitespaces
  @Test
  public void testCleanStringsWithWhitespaces() {
    String input1 = "abc def ghi";
    String expected = "ABC";
    String result1 = SoundexUtils.clean(input1);
    assertEquals(expected, result1);
  }

  // Test for strings with uppercase letters
  @Test
  public void testCleanStringsWithUppercaseLetters() {
    String input1 = "ABC DEF GHI";
    String expected = "ABC";
    String result1 = SoundexUtils.clean(input1);
    assertEquals(expected, result1);
  }

  
}