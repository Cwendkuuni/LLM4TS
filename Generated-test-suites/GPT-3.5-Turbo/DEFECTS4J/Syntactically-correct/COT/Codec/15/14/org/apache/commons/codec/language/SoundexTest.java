import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;

import static org.junit.Assert.*;

public class SoundexTest {
  
  private Soundex soundex = new Soundex();
  
  @Test
  public void testEncode() {
    String encoded = soundex.encode("Test");
    assertEquals("T230", encoded);
  }
  
  @Test
  public void testEncodeWithNull() {
    String encoded = soundex.encode(null);
    assertNull(encoded);
  }
  
  @Test
  public void testEncodeWithEmptyString() {
    String encoded = soundex.encode("");
    assertEquals("", encoded);
  }
  
  @Test
  public void testEncodeWithNonAlphabeticCharacters() {
    String encoded = soundex.encode("123 Test!");
    assertEquals("T230", encoded);
  }
  
  @Test(expected = EncoderException.class)
  public void testEncodeWithNonStringInput() throws EncoderException {
    soundex.encode(123);
  }
  
  @Test
  public void testDifference() throws EncoderException {
    int difference = soundex.difference("Test", "Tast");
    assertEquals(3, difference);
  }
  
  @Test
  public void testDifferenceWithNull() throws EncoderException {
    int difference = soundex.difference(null, "Tast");
    assertEquals(0, difference);
  }
  
  @Test
  public void testDifferenceWithEmptyString() throws EncoderException {
    int difference = soundex.difference("", "Tast");
    assertEquals(0, difference);
  }
  
  @Test
  public void testDifferenceWithNonAlphabeticCharacters() throws EncoderException {
    int difference = soundex.difference("123Test!", "123Tast!");
    assertEquals(3, difference);
  }
  
  @Test(expected = EncoderException.class)
  public void testDifferenceWithNonStringInput() throws EncoderException {
    soundex.difference(123, "Tast");
  }
}