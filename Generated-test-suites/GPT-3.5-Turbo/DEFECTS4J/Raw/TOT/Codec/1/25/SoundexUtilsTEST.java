import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {
  
  @Test
  public void testClean() {
    // Typical use case
    String str1 = "AbcD";
    assertEquals("ABCD", SoundexUtils.clean(str1));
    
    // Edge case: empty string
    String str2 = "";
    assertEquals("", SoundexUtils.clean(str2));
    
    // Error scenario: null string
    String str3 = null;
    assertNull(SoundexUtils.clean(str3));
  }
  
  @Test
  public void testDifference() {
    // Typical use case
    StringEncoder encoder = new Soundex();

    String s1 = "AbcD";
    String s2 = "Abcd";
    assertEquals(3, SoundexUtils.difference(encoder, s1, s2));
    
    // Edge case: identical strings
    String s3 = "AbcD";
    String s4 = "AbcD";
    assertEquals(4, SoundexUtils.difference(encoder, s3, s4));
    
    // Error scenario: null encoder
    String s5 = "AbcD";
    String s6 = "Abcd";
    try {
      SoundexUtils.difference(null, s5, s6);
      fail("Expected EncoderException");
    } catch (EncoderException e) {
      // Exception expected
    }
  }
  
  @Test
  public void testDifferenceEncoded() {
    // Typical use case
    String es1 = "A123";
    String es2 = "B123";
    assertEquals(0, SoundexUtils.differenceEncoded(es1, es2));
    
    // Edge case: identical encoded strings
    String es3 = "A123";
    String es4 = "A123";
    assertEquals(4, SoundexUtils.differenceEncoded(es3, es4));
    
    // Error scenario: null encoded string
    String es5 = null;
    String es6 = "B123";
    assertEquals(0, SoundexUtils.differenceEncoded(es5, es6));
  }
}