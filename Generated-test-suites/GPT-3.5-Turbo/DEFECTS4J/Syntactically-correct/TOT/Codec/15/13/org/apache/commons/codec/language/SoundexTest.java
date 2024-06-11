import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.EncoderException;

public class SoundexTest {

  @Test
  public void testDifference() throws EncoderException {
    Soundex soundex = new Soundex();
    String s1 = "John";
    String s2 = "Johnn";
    int result = soundex.difference(s1, s2);
    Assert.assertEquals(3, result);
  }

  @Test
  public void testDifferenceWithEmptyString() throws EncoderException {
    Soundex soundex = new Soundex();
    String s1 = "";
    String s2 = "";
    int result = soundex.difference(s1, s2);
    Assert.assertEquals(0, result);
  }

  @Test(expected = EncoderException.class)
  public void testDifferenceWithNonStringInput() throws EncoderException {
    Soundex soundex = new Soundex();
    Object s1 = new Object();
    Object s2 = new Object();
    int result = soundex.difference(s1, s2);
  }

  @Test
  public void testEncode() throws EncoderException {
    Soundex soundex = new Soundex();
    String str = "John";
    Object result = soundex.encode(str);
    Assert.assertEquals("J500", result);
  }

  @Test
  public void testEncodeWithEmptyString() throws EncoderException {
    Soundex soundex = new Soundex();
    String str = "";
    Object result = soundex.encode(str);
    Assert.assertEquals("", result);
  }

  @Test(expected = EncoderException.class)
  public void testEncodeWithNonStringInput() throws EncoderException {
    Soundex soundex = new Soundex();
    Object str = new Object();
    Object result = soundex.encode(str);
  }

  @Test
  public void testSoundex() {
    Soundex soundex = new Soundex();
    String str = "John";
    String result = soundex.soundex(str);
    Assert.assertEquals("J500", result);
  }

  @Test
  public void testSoundexWithEmptyString() {
    Soundex soundex = new Soundex();
    String str = "";
    String result = soundex.soundex(str);
    Assert.assertEquals("", result);
  }

  @Test
  public void testSoundexWithNonAlphabeticCharacters() {
    Soundex soundex = new Soundex();
    String str = "Joh99n";
    String result = soundex.soundex(str);
    Assert.assertEquals("J500", result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSoundexWithNonMappedCharacter() {
    Soundex soundex = new Soundex();
    String str = "John>";
    String result = soundex.soundex(str);
  }
}