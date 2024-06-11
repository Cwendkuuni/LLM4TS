import static org.junit.Assert.*;
import org.junit.Test;

public class SoundexTest {
  
  @Test
  public void testSoundexEncode() {
    Soundex soundex = new Soundex();
    String encoded = soundex.encode("Hello");
    assertEquals("H400", encoded);
  }
  
  @Test
  public void testSoundexDifference() throws EncoderException {
    Soundex soundex = new Soundex();
    int difference = soundex.difference("Hello", "Hallo");
    assertEquals(3, difference);
  }
  
  @Test
  public void testSoundexMap() {
    Soundex soundex = new Soundex();
    char mappedChar = soundex.map('A');
    assertEquals('0', mappedChar);
  }
  
  @Test
  public void testSoundexMaxLength() {
    Soundex soundex = new Soundex();
    assertEquals(4, soundex.getMaxLength());
  }
  
  @Test
  public void testSoundexSoundexMapping() {
    Soundex soundex = new Soundex();
    char[] mapping = soundex.getSoundexMapping();
    assertNotNull(mapping);
  }
  
  @Test
  public void testSoundexSetMaxLength() {
    Soundex soundex = new Soundex();
    soundex.setMaxLength(5);
    assertEquals(5, soundex.getMaxLength());
  }

}