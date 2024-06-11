import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

  @Test
  public void testClean() {
    // Typical use case: input string contains both letters and non-letter characters
    String str1 = "a1b2c3";
    String expected1 = "ABC";
    assertEquals(expected1, SoundexUtils.clean(str1));

    // Edge case: input string is empty
    String str2 = "";
    String expected2 = "";
    assertEquals(expected2, SoundexUtils.clean(str2));

    // Error scenario: input string is null
    String str3 = null;
    assertEquals(null, SoundexUtils.clean(str3));
  }

  @Test
  public void testDifference() throws EncoderException {
    // Typical use case: comparing two similar encoded strings
    StringEncoder encoder1 = new Soundex();
    String s11 = "hello";
    String s12 = "helo";
    int expected1 = 3;
    assertEquals(expected1, SoundexUtils.difference(encoder1, s11, s12));

    // Edge case: comparing two identical encoded strings
    StringEncoder encoder2 = new Soundex();
    String s21 = "hello";
    String s22 = "hello";
    int expected2 = 5;
    assertEquals(expected2, SoundexUtils.difference(encoder2, s21, s22));

    // Error scenario: encoder throws EncoderException
    StringEncoder encoder3 = new Soundex();
    String s31 = "hello";
    String s32 = "hi";
    assertEquals(0, SoundexUtils.difference(encoder3, s31, s32));
  }

  @Test
  public void testDifferenceEncoded() {
    // Typical use case: comparing two similar encoded strings
    String es1 = "H400";
    String es2 = "H405";
    int expected1 = 3;
    assertEquals(expected1, SoundexUtils.differenceEncoded(es1, es2));

    // Edge case: comparing two identical encoded strings
    String es3 = "H400";
    String es4 = "H400";
    int expected2 = 4;
    assertEquals(expected2, SoundexUtils.differenceEncoded(es3, es4));

    // Error scenario: one or both encoded strings are null
    String es5 = null;
    String es6 = "H400";
    assertEquals(0, SoundexUtils.differenceEncoded(es5, es6));

    String es7 = "H400";
    String es8 = null;
    assertEquals(0, SoundexUtils.differenceEncoded(es7, es8));

    String es9 = null;
    String es10 = null;
    assertEquals(0, SoundexUtils.differenceEncoded(es9, es10));
  }
}