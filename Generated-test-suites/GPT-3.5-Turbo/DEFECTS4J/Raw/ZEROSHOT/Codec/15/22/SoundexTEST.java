import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

  @Test
  public void testDifference() throws EncoderException {
    Soundex soundex = new Soundex();
    int difference = soundex.difference("hello", "hallo");
    assertEquals(2, difference);
  }

  @Test
  public void testEncodeObject() throws EncoderException {
    Soundex soundex = new Soundex();
    Object result = soundex.encode("hello");
    assertEquals("H400", result.toString());
  }

  @Test
  public void testEncodeString() {
    Soundex soundex = new Soundex();
    String result = soundex.encode("hello");
    assertEquals("H400", result);
  }

  @Test
  public void testSoundex() {
    Soundex soundex = new Soundex();
    String result = soundex.soundex("hello");
    assertEquals("H400", result);
  }
}