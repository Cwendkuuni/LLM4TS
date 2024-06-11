import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {
  
  @Test
  public void testDifference() throws EncoderException {
    Soundex soundex = new Soundex();
    int difference = soundex.difference("John", "Doe");
    assertEquals(0, difference);
  }
  
  @Test
  public void testEncodeObject() throws EncoderException {
    Soundex soundex = new Soundex();
    Object encoded = soundex.encode("John");
    assertEquals("J500", encoded);
  }
  
  @Test
  public void testEncodeString() {
    Soundex soundex = new Soundex();
    String encoded = soundex.encode("John");
    assertEquals("J500", encoded);
  }
  
  @Test
  public void testGetSoundexMapping() {
    Soundex soundex = new Soundex();
    char[] mapping = soundex.getSoundexMapping();
    assertTrue(mapping.length > 0);
  }
  
  @Test
  public void testSetMaxLength() {
    Soundex soundex = new Soundex();
    soundex.setMaxLength(6);
    assertEquals(6, soundex.getMaxLength());
  }
  
  @Test
  public void testSoundex() {
    Soundex soundex = new Soundex();
    String encoded = soundex.soundex("John");
    assertEquals("J500", encoded);
  }
}