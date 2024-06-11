import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class SoundexUtilsTest {
  
  @Test
  public void testCleanWithNullString() {
    String result = SoundexUtils.clean(null);
    assertNull(result);
  }
  
  @Test
  public void testCleanWithEmptyString() {
    String result = SoundexUtils.clean("");
    assertEquals("", result);
  }
  
  @Test
  public void testCleanWithLowerCaseLetters() {
    String input = "abcdefghijklmnopqrstuvwxyz";
    String result = SoundexUtils.clean(input);
    assertEquals(input.toUpperCase(), result);
  }

  @Test
  public void testCleanWithUpperCaseLetters() {
    String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String result = SoundexUtils.clean(input);
    assertEquals(input, result);
  }
  
  @Test
  public void testCleanWithNumbers() {
    String input = "1234567890";
    String result = SoundexUtils.clean(input);
    assertEquals("", result);
  }
  
  @Test
  public void testDifferenceWithEqualStrings() throws EncoderException {
    StringEncoder encoder = new Soundex();
    String s1 = "hello";
    String s2 = "hello";
    int result = SoundexUtils.difference(encoder, s1, s2);
    assertEquals(4, result);
  }
  
  @Test
  public void testDifferenceWithDifferentStrings() throws EncoderException {
    StringEncoder encoder = new Soundex();
    String s1 = "hello";
    String s2 = "world";
    int result = SoundexUtils.difference(encoder, s1, s2);
    assertEquals(1, result);
  }
  
  @Test
  public void testDifferenceWithNullEncodedStrings() {
    String es1 = null;
    String es2 = null;
    int result = SoundexUtils.differenceEncoded(es1, es2);
    assertEquals(0, result);
  }
  
  @Test
  public void testDifferenceWithDifferentLengthEncodedStrings() {
    String es1 = "ABCD";
    String es2 = "ABCDE";
    int result = SoundexUtils.differenceEncoded(es1, es2);
    assertEquals(4, result);
  }
  
  @Test
  public void testDifferenceWithEqualLengthEncodedStrings() {
    String es1 = "ABCD";
    String es2 = "ABCD";
    int result = SoundexUtils.differenceEncoded(es1, es2);
    assertEquals(4, result);
  }
  
  @Test
  public void testDifferenceWithDifferentEncodedStrings() {
    String es1 = "ABCD";
    String es2 = "DCBA";
    int result = SoundexUtils.differenceEncoded(es1, es2);
    assertEquals(0, result);
  }
  
}