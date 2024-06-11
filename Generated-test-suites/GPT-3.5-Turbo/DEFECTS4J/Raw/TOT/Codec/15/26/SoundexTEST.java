import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {
  
  @Test
  public void testEncode_ValidInput_ReturnsEncodedString() {
    Soundex soundex = new Soundex();
    String encoded = soundex.encode("Apache");
    assertEquals("A120", encoded);
  }
  
  @Test
  public void testEncode_NullInput_ReturnsNull() {
    Soundex soundex = new Soundex();
    String encoded = soundex.encode(null);
    assertNull(encoded);
  }
  
  @Test
  public void testEncode_EmptyInput_ReturnsEmptyString() {
    Soundex soundex = new Soundex();
    String encoded = soundex.encode("");
    assertEquals("", encoded);
  }
  
  @Test
  public void testEncode_SingleCharacterInput_ReturnsSameCharacter() {
    Soundex soundex = new Soundex();
    String encoded = soundex.encode("A");
    assertEquals("A", encoded);
  }
  
  @Test
  public void testEncode_AllUpperAndLowercaseInput_ReturnsEncodedStringWithUppercase() {
    Soundex soundex = new Soundex();
    String encoded = soundex.encode("apache");
    assertEquals("A120", encoded);
  }
  
  @Test
  public void testEncode_InputWithNonEnglishCharacters_ReturnsEncodedStringWithoutNonEnglishCharacters() {
    Soundex soundex = new Soundex();
    String encoded = soundex.encode("Äpfel");
    assertEquals("A124", encoded);
  }
  
  @Test
  public void testDifference_IdenticalStrings_ReturnsFour() throws EncoderException {
    Soundex soundex = new Soundex();
    int difference = soundex.difference("Apache", "Apache");
    assertEquals(4, difference);
  }
  
  @Test
  public void testDifference_SimilarStrings_ReturnsGreaterThanZero() throws EncoderException {
    Soundex soundex = new Soundex();
    int difference = soundex.difference("Apache", "Appache");
    assertTrue(difference > 0);
  }
  
  @Test
  public void testDifference_DifferentStrings_ReturnsZero() throws EncoderException {
    Soundex soundex = new Soundex();
    int difference = soundex.difference("Apache", "Code");
    assertEquals(0, difference);
  }
  
  @Test(expected = EncoderException.class)
  public void testDifference_NullString1_ThrowsEncoderException() throws EncoderException {
    Soundex soundex = new Soundex();
    soundex.difference(null, "Apache");
  }
  
  @Test(expected = EncoderException.class)
  public void testDifference_NullString2_ThrowsEncoderException() throws EncoderException {
    Soundex soundex = new Soundex();
    soundex.difference("Apache", null);
  }
  
  @Test
  public void testDifference_EmptyStrings_ReturnsZero() throws EncoderException {
    Soundex soundex = new Soundex();
    int difference = soundex.difference("", "");
    assertEquals(0, difference);
  }
}