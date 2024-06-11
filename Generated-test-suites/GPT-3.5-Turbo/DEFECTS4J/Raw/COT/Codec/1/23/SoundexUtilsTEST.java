import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {
  
  @Test
  public void testClean_nullString() {
    assertNull(SoundexUtils.clean(null));
  }
  
  @Test
  public void testClean_emptyString() {
    assertEquals("", SoundexUtils.clean(""));
  }
  
  @Test
  public void testClean_noLetters() {
    assertEquals("", SoundexUtils.clean("1234"));
  }
  
  @Test
  public void testClean_onlyLetters() {
    assertEquals("ABC", SoundexUtils.clean("abc"));
  }
  
  @Test
  public void testClean_mixedLettersAndNumbers() {
    assertEquals("A2C4", SoundexUtils.clean("a2c4"));
  }
  
  @Test
  public void testDifference_sameEncoder() throws EncoderException {
    StringEncoder encoder = new DummyEncoder();
    assertEquals(0, SoundexUtils.difference(encoder, "ABC", "DEF"));
    assertEquals(4, SoundexUtils.difference(encoder, "ABC", "ABC"));
    assertEquals(2, SoundexUtils.difference(encoder, "ABC", "ADE"));
  }
  
  @Test(expected = EncoderException.class)
  public void testDifference_encoderException() throws EncoderException {
    StringEncoder encoder = new EncoderWithException();
    SoundexUtils.difference(encoder, "ABC", "DEF");
  }
  
  @Test
  public void testDifferenceEncoded_nullStrings() {
    assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    assertEquals(0, SoundexUtils.differenceEncoded(null, "ABC"));
    assertEquals(0, SoundexUtils.differenceEncoded("ABC", null));
  }
  
  @Test
  public void testDifferenceEncoded_differentLengths() {
    assertEquals(0, SoundexUtils.differenceEncoded("ABC", "DEF"));
    assertEquals(2, SoundexUtils.differenceEncoded("ABC", "ADE"));
    assertEquals(3, SoundexUtils.differenceEncoded("ABC", "ABCG"));
  }
  
  private static class DummyEncoder implements StringEncoder {
    public String encode(String str) {
      return str;
    }
  }
  
  private static class EncoderWithException implements StringEncoder {
    public String encode(String str) throws EncoderException {
      throw new EncoderException();
    }
  }
}